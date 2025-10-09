# AssertJ + TestNG Framework - Teste de Software

## 1. Descrição do Framework

AssertJ é uma biblioteca Java que fornece uma API fluente para escrever assertivas em testes. Desenvolvida para oferecer melhor legibilidade e expressividade comparada às assertivas tradicionais, o AssertJ permite criar testes mais descritivos e fáceis de entender.

O framework se destaca pela sua sintaxe intuitiva, que segue o padrão "Given-When-Then" de forma natural, oferecendo métodos encadeados que tornam as verificações mais próximas da linguagem natural. Com suporte robusto para diferentes tipos de dados (coleções, strings, números, objetos customizados), AssertJ facilita a validação de condições complexas em testes unitários e de integração.

Sua principal vantagem é a capacidade de fornecer mensagens de erro mais claras e detalhadas quando as assertivas falham, auxiliando significativamente no processo de debugging e manutenção de testes.

**TestNG** é o framework de testes utilizado neste projeto, oferecendo recursos avançados como agrupamento de testes, dependências entre testes, execução paralela e relatórios detalhados. A combinação AssertJ + TestNG proporciona uma solução robusta para criação e execução de testes em Java.

## 2. Categorização do Framework

### i) Técnicas de Teste

**Técnica de Caixa Preta com elementos de Caixa Branca**: Nos testes implementados, AssertJ é utilizado principalmente como caixa preta, mas com alguns aspectos de caixa branca:

**Caixa Preta**:

- Testamos os métodos públicos (`save()`, `findById()`, `findAll()`) sem conhecer a implementação interna
- Focamos nas entradas e saídas esperadas dos métodos
- Validamos comportamentos através de cenários de uso (usuário válido, ID inexistente, lista vazia)

**Elementos de Caixa Branca**:

- Utilizamos `extracting(User::getId)` para acessar propriedades específicas do objeto
- Verificamos estados internos como `getLogin()` e `getPassword()` após operações
- Aplicamos mocks para isolar dependências e controlar fluxos internos

### ii) Níveis de Teste

**Teste de Unidade**: Confirmado pelos testes implementados porque:

- Testamos isoladamente a classe `UserService` sem suas dependências reais
- Utilizamos `@Mock UserRepository` para isolar a unidade testada
- Cada teste foca em um método específico (`save`, `findById`, `findAll`)
- Validamos comportamentos individuais sem integração com banco de dados ou outros componentes
- Aplicamos `@InjectMocks` para injetar dependências mockadas na unidade sob teste

### iii) Tipos de Teste

**Teste Funcional**: Evidenciado pelos casos de teste implementados:

- **Teste de Funcionalidade Normal**: `saveUser_withValidUser_returnsSavedUser()` verifica se a função de salvar usuário funciona corretamente
- **Teste de Tratamento de Erro**: `findById_withInvalidId_throwsIllegalArgumentException()` valida o comportamento quando ID não existe
- **Teste de Condições Limite**: `findAll_withEmptySavedUsers_returnsEmptyList()` testa cenário de lista vazia
- **Teste de Múltiplos Dados**: `findAll_withSavedUsers_returnsAllUsers()` verifica funcionalidade com dados válidos

Cada teste valida se a funcionalidade atende aos requisitos esperados do ponto de vista do usuário, sem se preocupar com a estrutura interna do código.

## 3. Instalação/Integração

### Maven

Adicione as dependências no `pom.xml`:

```xml
<!-- TestNG dependency -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.8.0</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ for fluent assertions -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>

<!-- Mockito for mocking -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

### Gradle

Adicione no `build.gradle`:

```gradle
testImplementation 'org.testng:testng:7.8.0'
testImplementation 'org.assertj:assertj-core:3.24.2'
testImplementation 'org.mockito:mockito-core:5.5.0'
```

### Importações no Código

```java
// TestNG
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

// AssertJ
```

## 4. Estratégias de Teste e Casos de Teste

### Estratégia: Particionamento por Equivalência

**Aplicação no método `findAll()`:**

#### Partições Identificadas:

1. **Lista com elementos**: Repository retorna usuários
2. **Lista vazia**: Repository retorna lista vazia

#### Casos de Teste Derivados:

**Caso 1: Lista com Usuários**

```java
@Test
public void findAll_withSavedUsers_returnsAllUsers() {
    // Given: Repositório com dois usuários
    var firstUser = createUser();
    var secondUser = createUser();
    when(userRepository.findAll()).thenReturn(List.of(firstUser, secondUser));

    // When: Busca todos os usuários
    var result = userService.findAll();

    // Then: Retorna exatamente os usuários esperados
    assertThat(result)
            .hasSize(2)
            .containsExactlyInAnyOrder(firstUser, secondUser);
}
```

**Caso 2: Lista Vazia**

```java
@Test
public void findAll_withEmptySavedUsers_returnsEmptyList() {
    // Given: Repositório vazio
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    // When: Busca todos os usuários
    var result = userService.findAll();

    // Then: Retorna lista vazia mas não nula
    assertThat(result)
            .isNotNull()
            .isEmpty();
}
```

### Estratégia: Análise de Valor Limite

**Aplicação no método `findById()`:**

#### Valores Limite:

- **Válido**: ID existente no repositório
- **Inválido**: ID não existente (gera exceção)

#### Casos de Teste:

**Caso Válido:**

```java
@Test
public void findById_withSavedUser_returnsUser() {
    // Given: Usuário existente
    var expectedUser = createUser();
    var id = UUID.randomUUID();
    when(userRepository.findById(id)).thenReturn(expectedUser);

    // When/Then: Retorna o usuário correto
    var result = userService.findById(id);
    assertThat(result).isEqualTo(expectedUser);
}
```

**Caso Inválido:**

```java
@Test
public void findById_withInvalidId_throwsIllegalArgumentException() {
    // Given: ID inexistente
    var id = UUID.randomUUID();
    when(userRepository.findById(id))
        .thenThrow(new IllegalArgumentException("Usuario nao encontrado para o ID: " + id));

    // When/Then: Lança exceção esperada
    assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> userService.findById(id))
            .withMessage("Usuario nao encontrado para o ID: " + id);
}
```

### Configuração TestNG com Mockito

```java
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
```

### Recursos AssertJ + TestNG Utilizados:

- `@Test`: Anotação TestNG para métodos de teste
- `@BeforeMethod`: Configuração antes de cada teste
- `assertThat()`: Ponto de entrada principal do AssertJ
- `hasSize()`: Verificação de tamanho de coleções
- `containsExactlyInAnyOrder()`: Validação de conteúdo de listas
- `isEmpty()`: Verificação de coleção vazia
- `isNotNull()`: Validação de não nulidade
- `extracting()`: Extração de propriedades de objetos
- `assertThatExceptionOfType()`: Verificação de exceções específicas
- `isThrownBy()`: Validação de lançamento de exceção
- `withMessage()`: Verificação de mensagem de exceção
