# Development Guide - OPDEE API

Guia para desenvolvedores que trabalham no projeto OPDEE-API.

## Configuração do Ambiente de Desenvolvimento

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **PostgreSQL 12+** (ou Docker)
- **IDE** (IntelliJ IDEA, VSCode, Eclipse)
- **Git**
- **Docker** (opcional, recomendado)

### Setup Inicial

#### 1. Clone do Repositório
```bash
git clone https://github.com/thsethub/OPDEE-API.git
cd OPDEE-API
```

#### 2. Configuração do Banco de Dados

**Opção A: Docker (Recomendado)**
```bash
# Subir PostgreSQL via Docker Compose
docker-compose up -d postgres

# Verificar se está rodando
docker-compose ps
```

**Opção B: PostgreSQL Local**
```bash
# Instalar PostgreSQL (Ubuntu/Debian)
sudo apt install postgresql postgresql-contrib

# Criar banco e usuário
sudo -u postgres createuser opdee
sudo -u postgres createdb opdee -O opdee
sudo -u postgres psql -c "ALTER USER opdee PASSWORD 'opdee';"
```

#### 3. Configuração da IDE

**IntelliJ IDEA:**
1. Abrir projeto Maven
2. Configurar SDK Java 17
3. Instalar plugins: Lombok, Spring Boot
4. Configurar code style (ver seção Padrões)

**VSCode:**
1. Instalar Extension Pack for Java
2. Instalar Spring Boot Extension Pack
3. Configurar settings.json (ver abaixo)

```json
// .vscode/settings.json
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "/path/to/java17"
        }
    ],
    "java.compile.nullAnalysis.mode": "automatic",
    "spring-boot.ls.problem.application-properties.enabled": true
}
```

#### 4. Executar a Aplicação

```bash
# Via Maven
./mvnw spring-boot:run

# Via IDE
# Executar OpdeeApplication.java

# Verificar se está funcionando
curl http://localhost:8080/api/ambiente
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/ufpe/opdee/
│   │   ├── OpdeeApplication.java          # Classe principal
│   │   ├── controllers/                   # Controladores REST
│   │   │   ├── AcessoController.java
│   │   │   ├── AmbienteController.java
│   │   │   ├── UsuarioController.java
│   │   │   ├── PerfilController.java
│   │   │   ├── HistoricoController.java
│   │   │   └── BrokerController.java
│   │   ├── models/                        # Entidades do domínio
│   │   │   ├── Usuario.java
│   │   │   ├── acesso/
│   │   │   │   ├── Acesso.java
│   │   │   │   ├── AcessoRequest.java
│   │   │   │   ├── AcessoResponse.java
│   │   │   │   └── AcessoUpdate.java
│   │   │   ├── ambiente/
│   │   │   │   ├── Ambiente.java
│   │   │   │   └── AmbienteRequest.java
│   │   │   ├── perfil/
│   │   │   │   ├── Perfil.java
│   │   │   │   ├── PerfilRequest.java
│   │   │   │   └── PerfilResponse.java
│   │   │   ├── historico/
│   │   │   │   ├── Historico.java
│   │   │   │   ├── HistoricoRequest.java
│   │   │   │   └── HistoricoResponse.java
│   │   │   └── broker/
│   │   │       ├── Broker.java
│   │   │       └── BrokerRequest.java
│   │   ├── repositories/                  # Repositórios JPA
│   │   │   ├── AcessoRepository.java
│   │   │   ├── AmbienteRepository.java
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── PerfilRepository.java
│   │   │   ├── HistoricoRepository.java
│   │   │   └── BrokerRepository.java
│   │   ├── services/                      # Serviços de negócio
│   │   │   ├── AcessoService.java
│   │   │   ├── AmbienteService.java
│   │   │   ├── UsuarioService.java
│   │   │   ├── PerfilService.java
│   │   │   ├── HistoricoService.java
│   │   │   └── BrokerService.java
│   │   └── config/                        # Configurações
│   │       ├── ModelMapperConfig.java
│   │       ├── WebSocketConfig.java
│   │       └── DatabaseConfig.java
│   └── resources/
│       ├── application.properties         # Configurações da aplicação
│       └── db/migration/                  # Scripts Flyway
│           ├── V001__Create_initial_tables.sql
│           └── V002__Insert_default_data.sql
└── test/                                  # Testes automatizados
    └── java/br/ufpe/opdee/
        ├── OpdeeApplicationTests.java
        ├── controllers/
        ├── services/
        └── repositories/
```

## Padrões de Desenvolvimento

### Convenções de Código

#### Nomenclatura
- **Classes**: PascalCase (`AcessoService`)
- **Métodos/Variáveis**: camelCase (`findById`)
- **Constantes**: UPPER_SNAKE_CASE (`MAX_ATTEMPTS`)
- **Pacotes**: lowercase (`br.ufpe.opdee.models`)

#### Anotações Lombok
```java
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "exemplo")
public class ExemploEntity {
    // campos...
}
```

#### Controllers
```java
@RestController
@RequestMapping("/api/exemplo")
public class ExemploController {
    
    private final ExemploService service;
    
    public ExemploController(ExemploService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<Exemplo>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @PostMapping
    public ResponseEntity<Exemplo> save(@RequestBody ExemploRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
}
```

#### Services
```java
@Service
public class ExemploService {
    
    private final ExemploRepository repository;
    private final ModelMapper modelMapper;
    
    public ExemploService(ExemploRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }
    
    public List<Exemplo> findAll() {
        return repository.findAll();
    }
    
    public Exemplo save(ExemploRequest request) {
        Exemplo entity = modelMapper.map(request, Exemplo.class);
        return repository.save(entity);
    }
}
```

#### Repositories
```java
@Repository
public interface ExemploRepository extends JpaRepository<Exemplo, UUID> {
    
    List<Exemplo> findByNomeContaining(String nome);
    
    @Query("SELECT e FROM Exemplo e WHERE e.ativo = true")
    List<Exemplo> findAllAtivos();
}
```

### Tratamento de Erros

#### Exception Handling
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_ARGUMENT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

### Validações

#### Bean Validation
```java
public class ExemploRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @Pattern(regexp = "^[0-9]+$", message = "Telefone deve conter apenas números")
    private String telefone;
}
```

#### Controller Validation
```java
@PostMapping
public ResponseEntity<Exemplo> save(@Valid @RequestBody ExemploRequest request) {
    return ResponseEntity.ok(service.save(request));
}
```

## Testing

### Configuração de Testes

#### application-test.properties
```properties
# H2 Database para testes
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# Flyway (desabilitado para testes)
spring.flyway.enabled=false

# Logging
logging.level.org.springframework.web=DEBUG
```

### Testes Unitários

#### Service Test
```java
@ExtendWith(MockitoExtension.class)
class ExemploServiceTest {
    
    @Mock
    private ExemploRepository repository;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private ExemploService service;
    
    @Test
    void deveBuscarTodosExemplos() {
        // Given
        List<Exemplo> exemplos = Arrays.asList(new Exemplo(), new Exemplo());
        when(repository.findAll()).thenReturn(exemplos);
        
        // When
        List<Exemplo> result = service.findAll();
        
        // Then
        assertThat(result).hasSize(2);
        verify(repository).findAll();
    }
}
```

### Testes de Integração

#### Controller Integration Test
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class ExemploControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ExemploRepository repository;
    
    @Test
    void deveRetornarTodosExemplos() {
        // Given
        repository.save(new Exemplo("Teste 1"));
        repository.save(new Exemplo("Teste 2"));
        
        // When
        ResponseEntity<Exemplo[]> response = restTemplate.getForEntity("/api/exemplo", Exemplo[].class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}
```

### Repository Tests
```java
@DataJpaTest
class ExemploRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ExemploRepository repository;
    
    @Test
    void deveBuscarPorNome() {
        // Given
        Exemplo exemplo = new Exemplo("Teste");
        entityManager.persistAndFlush(exemplo);
        
        // When
        List<Exemplo> result = repository.findByNomeContaining("Test");
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo("Teste");
    }
}
```

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=ExemploServiceTest

# Com coverage
./mvnw test jacoco:report

# Ver relatório
open target/site/jacoco/index.html
```

## Debugging

### Configuração de Debug

#### application-dev.properties
```properties
# Debug mode
debug=true
logging.level.br.ufpe.opdee=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Actuator for debugging
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

### Debug na IDE

**IntelliJ IDEA:**
1. Configurar Run Configuration
2. Debug mode: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`
3. Remote debug: localhost:5005

**VSCode:**
```json
// .vscode/launch.json
{
    "configurations": [
        {
            "type": "java",
            "name": "Debug OPDEE",
            "request": "launch",
            "mainClass": "br.ufpe.opdee.OpdeeApplication",
            "projectName": "OPDEE",
            "args": "--spring.profiles.active=dev"
        }
    ]
}
```

### Logs Úteis

```java
// Em Controllers
log.info("Recebendo request para criar acesso: {}", request);
log.debug("Usuário encontrado: {}", usuario);

// Em Services
log.warn("Tentativa de acesso inválido para usuário: {}", usuarioId);
log.error("Erro ao salvar acesso", exception);

// Query logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## Database Development

### Flyway Migrations

#### Criando Nova Migration
```bash
# Criar arquivo V003__Add_new_column.sql
# Localização: src/main/resources/db/migration/
```

```sql
-- V003__Add_new_column.sql
ALTER TABLE ambientes ADD COLUMN descricao VARCHAR(500);
UPDATE ambientes SET descricao = 'Descrição padrão' WHERE descricao IS NULL;
ALTER TABLE ambientes ALTER COLUMN descricao SET NOT NULL;
```

#### Executar Migrations
```bash
# Via Maven
./mvnw flyway:migrate

# Via aplicação (automático no startup)
./mvnw spring-boot:run
```

### Database Utils

#### Reset Database
```bash
# Resetar schema
./mvnw flyway:clean flyway:migrate

# Ou via Docker
docker-compose down -v
docker-compose up -d postgres
```

#### Seed Data
```sql
-- V999__Insert_test_data.sql (para desenvolvimento)
INSERT INTO usuarios (uuid, nome_completo, email_ufpe, superuser) VALUES 
('dev001', 'Desenvolvedor Teste', 'dev@ufpe.br', true);

INSERT INTO "tipoPerfil" (nome) VALUES 
('Desenvolvedor'), ('Teste');

INSERT INTO ambientes (nome, topic, mensagem) VALUES 
('Lab Dev', 'opdee/dev', 'Ambiente de desenvolvimento');
```

## Ferramentas de Desenvolvimento

### Maven Plugins Úteis

```bash
# Dependency analysis
./mvnw dependency:analyze

# Security check
./mvnw org.owasp:dependency-check-maven:check

# Code formatting
./mvnw com.spotify.fmt:fmt-maven-plugin:format

# Generate documentation
./mvnw javadoc:javadoc
```

### Docker para Desenvolvimento

#### docker-compose.dev.yml
```yaml
version: '3.9'
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: opdee
      POSTGRES_USER: opdee
      POSTGRES_PASSWORD: opdee
    ports:
      - "5432:5432"
    volumes:
      - postgres_dev_data:/var/lib/postgresql/data

  adminer:
    image: adminer
    ports:
      - "8081:8080"
    depends_on:
      - postgres

volumes:
  postgres_dev_data:
```

### API Testing

#### Postman Collection
```json
{
  "info": {
    "name": "OPDEE API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    }
  ],
  "item": [
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "url": "{{baseUrl}}/actuator/health"
      }
    }
  ]
}
```

#### cURL Examples
```bash
# Create user
curl -X POST http://localhost:8080/api/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "uuid": "test001",
    "nomeCompleto": "Usuário Teste",
    "emailUfpe": "test@ufpe.br",
    "superUser": false
  }'

# List environments
curl http://localhost:8080/api/ambiente
```

## Performance e Otimização

### Database Optimizations

#### Query Optimization
```java
// Use JPA Projections para queries específicas
public interface AcessoProjection {
    String getId();
    String getUsuarioNome();
    String getAmbienteNome();
}

@Query("SELECT a.id as id, u.nomeCompleto as usuarioNome, amb.nome as ambienteNome " +
       "FROM Acesso a JOIN a.usuario u JOIN a.ambiente amb")
List<AcessoProjection> findAllProjections();
```

#### Lazy Loading
```java
// Usar @EntityGraph para controlar fetch
@EntityGraph(attributePaths = {"usuario", "ambiente"})
@Query("SELECT a FROM Acesso a")
List<Acesso> findAllWithDetails();
```

### Application Performance

#### Caching
```java
@Service
public class CachingService {
    
    @Cacheable("ambientes")
    public List<Ambiente> findAllAmbientes() {
        return ambienteRepository.findAll();
    }
    
    @CacheEvict(value = "ambientes", allEntries = true)
    public Ambiente save(Ambiente ambiente) {
        return ambienteRepository.save(ambiente);
    }
}
```

#### Connection Pool
```properties
# HikariCP configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

## Git Workflow

### Branch Strategy
```
main (produção)
├── develop (desenvolvimento)
├── feature/nome-da-feature
├── bugfix/nome-do-bug
└── hotfix/nome-do-hotfix
```

### Commit Messages
```
feat: adicionar endpoint para listar usuários
fix: corrigir validação de email duplicado
docs: atualizar documentação da API
test: adicionar testes para AcessoService
refactor: extrair lógica de validação para classe separada
```

### Pre-commit Hooks

#### .gitpre-commit
```bash
#!/bin/sh
# Run tests before commit
./mvnw test
if [ $? -ne 0 ]; then
 echo "Tests must pass before commit!"
 exit 1
fi

# Check code style
./mvnw checkstyle:check
if [ $? -ne 0 ]; then
 echo "Code style check failed!"
 exit 1
fi
```

## Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com Banco
```bash
# Verificar se PostgreSQL está rodando
docker-compose ps postgres

# Ver logs do PostgreSQL
docker-compose logs postgres

# Testar conexão
psql -h localhost -U opdee -d opdee
```

#### 2. Erro de Build
```bash
# Limpar cache Maven
./mvnw clean

# Rebuild completo
./mvnw clean compile

# Verificar dependências
./mvnw dependency:tree
```

#### 3. Erro de Testes
```bash
# Executar teste específico com debug
./mvnw test -Dtest=ExemploTest -X

# Ver relatório de falhas
cat target/surefire-reports/TEST-*.xml
```

#### 4. Problemas de Performance
```bash
# Habilitar SQL logging
logging.level.org.hibernate.SQL=DEBUG

# JVM memory settings
export MAVEN_OPTS="-Xmx2g -Xms1g"

# Profile application
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## Contribuindo

### Checklist para Pull Requests

- [ ] Código segue padrões estabelecidos
- [ ] Testes unitários adicionados/atualizados
- [ ] Documentação atualizada
- [ ] Build local bem-sucedido
- [ ] Migrations de banco (se necessário)
- [ ] Performance avaliada
- [ ] Security review (se aplicável)

### Code Review

#### O que verificar:
- Lógica de negócio correta
- Tratamento de erros adequado
- Performance das queries
- Segurança (validações, sanitização)
- Testes cobrindo casos edge
- Documentação clara

---

## Recursos Adicionais

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

Para dúvidas específicas, consulte a documentação técnica ou entre em contato com a equipe de desenvolvimento.