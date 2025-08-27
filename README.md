# OPDEE-API

Backend da aplicação OPDEE de gerenciamento de trancas do Departamento de Engenharia Elétrica da UFPE.

## Sobre o Projeto

O OPDEE-API é um sistema backend desenvolvido em Spring Boot para gerenciamento de controle de acesso a ambientes do Departamento de Engenharia Elétrica da UFPE. O sistema permite controlar trancas eletrônicas através de integração IoT via MQTT, gerenciar usuários, perfis e manter um histórico de acessos.

## Arquitetura

### Stack Tecnológica
- **Java 17** - Linguagem de programação
- **Spring Boot 3.3.3** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados principal
- **H2** - Banco de dados para desenvolvimento/testes
- **Flyway** - Migração de banco de dados
- **MQTT** - Comunicação IoT
- **WebSocket** - Comunicação em tempo real
- **Lombok** - Redução de boilerplate
- **ModelMapper** - Mapeamento de objetos

### Modelo de Domínio

#### Entidades Principais

1. **Usuario** - Usuários do sistema
   - UUID como identificador
   - Nome completo e email UFPE
   - Flag de superusuário
   - Data de criação

2. **Ambiente** - Salas/ambientes físicos
   - UUID como identificador
   - Nome único do ambiente
   - Tópico MQTT para comunicação
   - Mensagem personalizada
   - Data de criação

3. **Acesso** - Controle de acesso
   - Relacionamento Usuario ↔ Ambiente
   - Status ativo/inativo
   - Tipo de usuário
   - Data de criação

4. **Perfil** - Perfis de usuário
   - UUID como identificador
   - Nome do perfil
   - Data de criação

5. **Historico** - Log de auditoria
   - Registro de acessos
   - Relacionamento com Usuario, Perfil e Ambiente
   - Mensagem de log
   - Data/hora do evento

6. **Broker** - Configuração MQTT
   - Endereço IP e porta
   - Credenciais de autenticação
   - Data de criação

## API Endpoints

### 🔐 Controle de Acesso (`/api/acesso`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/acesso` | Criar novo acesso |
| `GET` | `/api/acesso` | Listar todos os acessos |
| `GET` | `/api/acesso/{usuario}` | Buscar acessos por usuário |
| `GET` | `/api/acesso/ambiente/{ambiente}` | Buscar acessos por ambiente |
| `PUT` | `/api/acesso/{id}` | Alternar permissão de acesso |
| `PUT` | `/api/acesso/perfil/{id}` | Atualizar acesso |
| `DELETE` | `/api/acesso/{id}` | Remover acesso |

### 👤 Usuários (`/api/usuario`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/usuario` | Criar novo usuário |
| `GET` | `/api/usuario/{id}` | Buscar usuário por ID |
| `DELETE` | `/api/usuario/{id}` | Remover usuário |

### 🏢 Ambientes (`/api/ambiente`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/ambiente` | Criar novo ambiente |
| `GET` | `/api/ambiente` | Listar todos os ambientes |
| `GET` | `/api/ambiente/onlyOne/{id}` | Buscar ambiente por ID |
| `GET` | `/api/ambiente/{nome}` | Buscar ambiente por nome |
| `PUT` | `/api/ambiente/{id}` | Atualizar ambiente |
| `DELETE` | `/api/ambiente/{id}` | Remover ambiente |

### 👥 Perfis (`/api/perfil`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/perfil` | Criar novo perfil |
| `GET` | `/api/perfil` | Listar todos os perfis |
| `GET` | `/api/perfil/{id}` | Buscar perfil por ID |
| `GET` | `/api/perfil/nome/{nome}` | Buscar perfil por nome |
| `PUT` | `/api/perfil/{id}` | Atualizar perfil |
| `DELETE` | `/api/perfil/{id}` | Remover perfil |

### 📋 Histórico (`/api/historico`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/historico` | Criar novo registro |
| `GET` | `/api/historico` | Listar todo o histórico |
| `GET` | `/api/historico/{id}` | Buscar registro por ID |
| `DELETE` | `/api/historico/{id}` | Remover registro |

### 🔧 Broker MQTT (`/api/broker`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/broker` | Configurar broker |
| `GET` | `/api/broker` | Listar configurações |
| `GET` | `/api/broker/{id}` | Buscar configuração por ID |
| `PUT` | `/api/broker/{id}` | Atualizar configuração |
| `DELETE` | `/api/broker/{id}` | Remover configuração |

## Instalação e Configuração

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 12+ (para produção)
- Docker e Docker Compose (opcional)

### Configuração do Banco de Dados

#### PostgreSQL (Produção)
```bash
# Usando Docker Compose
docker-compose up -d

# Ou configurar manualmente:
# Criar banco de dados 'opdee'
# Usuário: opdee, Senha: opdee
```

#### H2 (Desenvolvimento)
```properties
# Descomentar no application.properties:
# spring.datasource.url=jdbc:h2:mem:testdb
# spring.datasource.driverClassName=org.h2.Driver
# spring.datasource.username=sa
# spring.datasource.password=password
# spring.h2.console.enabled=true
```

### Executando a Aplicação

#### Método 1: Maven
```bash
# Compilar
./mvnw clean compile

# Executar
./mvnw spring-boot:run
```

#### Método 2: Docker
```bash
# Subir banco de dados
docker-compose up -d

# Executar aplicação
./mvnw spring-boot:run
```

#### Método 3: IDE
- Importar projeto Maven
- Executar classe `OpdeeApplication.java`

### Configurações Principais

O arquivo `application.properties` contém as configurações:

```properties
# Aplicação
spring.application.name=OPDEE

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/opdee
spring.datasource.username=opdee
spring.datasource.password=opdee

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

## Integração MQTT

O sistema utiliza MQTT para comunicação com dispositivos IoT (trancas eletrônicas):

- **Configuração**: Através do endpoint `/api/broker`
- **Tópicos**: Definidos por ambiente em `Ambiente.topic`
- **Mensagens**: Personalizáveis através de `Ambiente.mensagem`

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
│   │   │   ├── ambiente/
│   │   │   ├── perfil/
│   │   │   ├── historico/
│   │   │   └── broker/
│   │   ├── repositories/                  # Repositórios JPA
│   │   ├── services/                      # Serviços de negócio
│   │   └── config/                        # Configurações
│   └── resources/
│       ├── application.properties         # Configurações da aplicação
│       └── db/migration/                  # Scripts Flyway
└── test/                                  # Testes automatizados
```

## Desenvolvimento

### Padrões de Código
- Utilizar Lombok para reduzir boilerplate
- Seguir convenções REST para APIs
- Implementar validações adequadas
- Manter histórico de auditoria

### Adicionando Novas Funcionalidades
1. Criar entidade em `models/`
2. Implementar repository em `repositories/`
3. Criar service em `services/`
4. Implementar controller em `controllers/`
5. Adicionar testes em `test/`

### Testes
```bash
# Executar todos os testes
./mvnw test

# Executar com relatório
./mvnw test jacoco:report
```

## Deploy

### Ambiente de Produção
1. Configurar PostgreSQL
2. Configurar variáveis de ambiente
3. Executar migrações Flyway
4. Deploy da aplicação

### Docker Production
```dockerfile
# Exemplo de Dockerfile para produção
FROM openjdk:17-jre-slim
COPY target/OPDEE-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 📚 Documentação Completa

Este README fornece uma visão geral do projeto. Para informações mais detalhadas, consulte a documentação específica:

### 📖 Documentos Técnicos

| Documento | Descrição | Link |
|-----------|-----------|------|
| **API Reference** | Documentação completa da API REST | [docs/API.md](docs/API.md) |
| **Database Schema** | Estrutura do banco de dados e relacionamentos | [docs/DATABASE.md](docs/DATABASE.md) |
| **Architecture Overview** | Visão geral da arquitetura do sistema | [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) |
| **Development Guide** | Guia para desenvolvedores | [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) |
| **Deployment Guide** | Instruções de deploy e configuração | [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) |

### 🚀 Quick Start Links

- **[Configuração de Desenvolvimento](docs/DEVELOPMENT.md#configuração-do-ambiente-de-desenvolvimento)** - Para começar a desenvolver
- **[API Endpoints](docs/API.md#api-endpoints)** - Lista completa dos endpoints
- **[Deploy com Docker](docs/DEPLOYMENT.md#deploy-com-docker)** - Deploy rápido
- **[Arquitetura do Sistema](docs/ARCHITECTURE.md#visão-geral-do-sistema)** - Entender o design

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

Para mais detalhes sobre desenvolvimento, consulte o [Development Guide](docs/DEVELOPMENT.md).

## Licença

Este projeto é desenvolvido para uso interno do Departamento de Engenharia Elétrica da UFPE.

## Contato

- **Departamento**: Engenharia Elétrica - UFPE
- **Repository**: [thsethub/OPDEE-API](https://github.com/thsethub/OPDEE-API)
- **Issues**: [GitHub Issues](https://github.com/thsethub/OPDEE-API/issues)

---

## Changelog

### v0.0.1-SNAPSHOT
- ✅ Implementação inicial do sistema
- ✅ CRUD completo para todas as entidades
- ✅ Integração MQTT para IoT
- ✅ Sistema de auditoria
- ✅ API REST documentada
- ✅ Documentação técnica completa