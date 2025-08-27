# API Documentation - OPDEE

Documentação detalhada da API REST do sistema OPDEE.

## Autenticação

**Nota**: A implementação atual não possui autenticação. Para ambiente de produção, recomenda-se implementar Spring Security com JWT ou OAuth2.

## Formatos de Dados

### Headers Padrão
```http
Content-Type: application/json
Accept: application/json
```

### Códigos de Status HTTP
- `200 OK` - Sucesso
- `201 Created` - Recurso criado
- `204 No Content` - Sucesso sem conteúdo
- `400 Bad Request` - Dados inválidos
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro interno

---

## 🔐 Controle de Acesso - `/api/acesso`

### POST `/api/acesso`
**Descrição**: Criar novo acesso para usuário em ambiente

**Request Body**:
```json
{
  "usuarioId": "string (UUID do usuário)",
  "ambienteId": "uuid (UUID do ambiente)",
  "tipoUsuario": "string (tipo do usuário)"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "usuario": {
    "uuid": "string",
    "nomeCompleto": "string",
    "emailUfpe": "string",
    "createdAt": "2024-01-01T00:00:00",
    "superUser": false
  },
  "ambiente": {
    "id": "uuid",
    "nome": "string",
    "topic": "string",
    "mensagem": "string",
    "createdAt": "2024-01-01T00:00:00"
  },
  "ativo": true,
  "tipoUsuario": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/acesso`
**Descrição**: Listar todos os acessos

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "usuario": { ... },
    "ambiente": { ... },
    "ativo": true,
    "tipoUsuario": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/acesso/{usuario}`
**Descrição**: Buscar acessos por usuário

**Path Parameters**:
- `usuario` (Usuario) - Objeto usuário completo

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "usuario": { ... },
    "ambiente": { ... },
    "ativo": true,
    "tipoUsuario": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/acesso/ambiente/{ambiente}`
**Descrição**: Buscar acessos por ambiente

**Path Parameters**:
- `ambiente` (Ambiente) - Objeto ambiente completo

**Response** `200 OK`:
```json
[
  {
    "usuario": {
      "uuid": "string",
      "nomeCompleto": "string",
      "emailUfpe": "string"
    },
    "acesso": {
      "id": "uuid",
      "ativo": true,
      "tipoUsuario": "string",
      "createdAt": "2024-01-01T00:00:00"
    }
  }
]
```

### PUT `/api/acesso/{id}`
**Descrição**: Alternar status ativo/inativo do acesso

**Path Parameters**:
- `id` (UUID) - ID do acesso

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "usuario": { ... },
  "ambiente": { ... },
  "ativo": false,
  "tipoUsuario": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### PUT `/api/acesso/perfil/{id}`
**Descrição**: Atualizar dados do acesso

**Path Parameters**:
- `id` (UUID) - ID do acesso

**Request Body**:
```json
{
  "tipoUsuario": "string",
  "ativo": true
}
```

### DELETE `/api/acesso/{id}`
**Descrição**: Remover acesso

**Path Parameters**:
- `id` (UUID) - ID do acesso

**Response** `204 No Content`

---

## 👤 Usuários - `/api/usuario`

### POST `/api/usuario`
**Descrição**: Criar novo usuário

**Request Body**:
```json
{
  "uuid": "string (ID único do usuário)",
  "nomeCompleto": "string",
  "emailUfpe": "string",
  "superUser": false
}
```

**Response** `200 OK`:
```json
{
  "uuid": "string",
  "nomeCompleto": "string",
  "emailUfpe": "string",
  "createdAt": "2024-01-01T00:00:00",
  "superUser": false
}
```

### GET `/api/usuario/{id}`
**Descrição**: Buscar usuário por ID

**Path Parameters**:
- `id` (string) - UUID do usuário

**Response** `200 OK`:
```json
{
  "uuid": "string",
  "nomeCompleto": "string",
  "emailUfpe": "string",
  "createdAt": "2024-01-01T00:00:00",
  "superUser": false
}
```

**Response** `204 No Content` - Quando usuário não encontrado

### DELETE `/api/usuario/{id}`
**Descrição**: Remover usuário

**Path Parameters**:
- `id` (string) - UUID do usuário

**Response** `204 No Content`

---

## 🏢 Ambientes - `/api/ambiente`

### POST `/api/ambiente`
**Descrição**: Criar novo ambiente

**Request Body**:
```json
{
  "nome": "string (único)",
  "topic": "string (tópico MQTT)",
  "mensagem": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "topic": "string",
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/ambiente`
**Descrição**: Listar todos os ambientes

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "nome": "string",
    "topic": "string",
    "mensagem": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/ambiente/onlyOne/{id}`
**Descrição**: Buscar ambiente por ID

**Path Parameters**:
- `id` (UUID) - ID do ambiente

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "topic": "string",
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/ambiente/{nome}`
**Descrição**: Buscar ambiente por nome

**Query Parameters**:
- `nome` (string) - Nome do ambiente

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "topic": "string",
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### PUT `/api/ambiente/{id}`
**Descrição**: Atualizar ambiente

**Path Parameters**:
- `id` (UUID) - ID do ambiente

**Request Body**:
```json
{
  "nome": "string",
  "topic": "string",
  "mensagem": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "topic": "string",
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### DELETE `/api/ambiente/{id}`
**Descrição**: Remover ambiente

**Path Parameters**:
- `id` (UUID) - ID do ambiente

**Response** `204 No Content`

---

## 👥 Perfis - `/api/perfil`

### POST `/api/perfil`
**Descrição**: Criar novo perfil

**Request Body**:
```json
{
  "nome": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/perfil`
**Descrição**: Listar todos os perfis

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "nome": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/perfil/{id}`
**Descrição**: Buscar perfil por ID

**Path Parameters**:
- `id` (UUID) - ID do perfil

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/perfil/nome/{nome}`
**Descrição**: Buscar perfil por nome

**Path Parameters**:
- `nome` (string) - Nome do perfil

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### PUT `/api/perfil/{id}`
**Descrição**: Atualizar perfil

**Path Parameters**:
- `id` (UUID) - ID do perfil

**Request Body**:
```json
{
  "nome": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "nome": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### DELETE `/api/perfil/{id}`
**Descrição**: Remover perfil

**Path Parameters**:
- `id` (UUID) - ID do perfil

**Response** `204 No Content`

---

## 📋 Histórico - `/api/historico`

### POST `/api/historico`
**Descrição**: Criar novo registro de histórico

**Request Body**:
```json
{
  "usuarioId": "string (UUID do usuário)",
  "perfilId": "uuid (UUID do perfil)",
  "ambienteId": "uuid (UUID do ambiente)",
  "mensagem": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "usuario": {
    "uuid": "string",
    "nomeCompleto": "string",
    "emailUfpe": "string"
  },
  "perfil": {
    "id": "uuid",
    "nome": "string"
  },
  "ambiente": {
    "id": "uuid",
    "nome": "string"
  },
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/historico`
**Descrição**: Listar todo o histórico

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "usuario": {
      "uuid": "string",
      "nomeCompleto": "string",
      "emailUfpe": "string"
    },
    "perfil": {
      "id": "uuid",
      "nome": "string"
    },
    "ambiente": {
      "id": "uuid",
      "nome": "string"
    },
    "mensagem": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/historico/{id}`
**Descrição**: Buscar registro por ID

**Path Parameters**:
- `id` (UUID) - ID do registro

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "usuario": { ... },
  "perfil": { ... },
  "ambiente": { ... },
  "mensagem": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### DELETE `/api/historico/{id}`
**Descrição**: Remover registro

**Path Parameters**:
- `id` (UUID) - ID do registro

**Response** `204 No Content`

---

## 🔧 Broker MQTT - `/api/broker`

### POST `/api/broker`
**Descrição**: Configurar broker MQTT

**Request Body**:
```json
{
  "ipAdress": "string (IP do broker)",
  "port": "string (porta)",
  "username": "string",
  "password": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "ipAdress": "string",
  "port": "string",
  "username": "string",
  "password": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### GET `/api/broker`
**Descrição**: Listar configurações de broker

**Response** `200 OK`:
```json
[
  {
    "id": "uuid",
    "ipAdress": "string",
    "port": "string",
    "username": "string",
    "password": "string",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### GET `/api/broker/{id}`
**Descrição**: Buscar configuração por ID

**Path Parameters**:
- `id` (UUID) - ID da configuração

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "ipAdress": "string",
  "port": "string",
  "username": "string",
  "password": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### PUT `/api/broker/{id}`
**Descrição**: Atualizar configuração

**Path Parameters**:
- `id` (UUID) - ID da configuração

**Request Body**:
```json
{
  "ipAdress": "string",
  "port": "string",
  "username": "string",
  "password": "string"
}
```

**Response** `200 OK`:
```json
{
  "id": "uuid",
  "ipAdress": "string",
  "port": "string",
  "username": "string",
  "password": "string",
  "createdAt": "2024-01-01T00:00:00"
}
```

### DELETE `/api/broker/{id}`
**Descrição**: Remover configuração

**Path Parameters**:
- `id` (UUID) - ID da configuração

**Response** `204 No Content`

---

## Exemplos de Uso

### Fluxo Típico: Criar Acesso para Usuário

1. **Criar Usuário**:
```bash
curl -X POST http://localhost:8080/api/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "uuid": "user123",
    "nomeCompleto": "João Silva",
    "emailUfpe": "joao.silva@ufpe.br",
    "superUser": false
  }'
```

2. **Criar Ambiente**:
```bash
curl -X POST http://localhost:8080/api/ambiente \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Laboratório 1",
    "topic": "opdee/lab1",
    "mensagem": "Acesso liberado para Lab 1"
  }'
```

3. **Criar Acesso**:
```bash
curl -X POST http://localhost:8080/api/acesso \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": "user123",
    "ambienteId": "uuid-do-ambiente",
    "tipoUsuario": "aluno"
  }'
```

### Integração MQTT

O sistema integra com dispositivos IoT através de MQTT:

1. Configure o broker através de `/api/broker`
2. Defina tópicos únicos para cada ambiente
3. O sistema publica mensagens nos tópicos quando acessos são ativados
4. Dispositivos escutam nos tópicos e executam ações (abrir/fechar trancas)

---

## Notas Técnicas

### Conversores de Tipo
- O sistema utiliza conversores automáticos para UUIDs
- Objetos complexos como `Usuario` e `Ambiente` são passados como parâmetros de path

### Validações
- Nomes de ambiente devem ser únicos
- Emails UFPE devem ser únicos
- UUIDs são gerados automaticamente para novas entidades

### Auditoria
- Todas as ações importantes são registradas na tabela `historico`
- Timestamps são gerados automaticamente no momento da criação

### Performance
- Relacionamentos utilizam lazy loading
- Queries são otimizadas com JPA
- Índices recomendados em campos de busca frequente

---

## Códigos de Erro Comuns

| Código | Descrição | Solução |
|--------|-----------|---------|
| 400 | Dados inválidos no request | Verificar formato JSON e campos obrigatórios |
| 404 | Recurso não encontrado | Verificar se ID existe na base |
| 500 | Erro interno | Verificar logs da aplicação |

## Testing

### Testando com cURL

```bash
# Health check (se implementado)
curl http://localhost:8080/actuator/health

# Listar ambientes
curl http://localhost:8080/api/ambiente

# Criar perfil
curl -X POST http://localhost:8080/api/perfil \
  -H "Content-Type: application/json" \
  -d '{"nome": "Professor"}'
```

### Testando com Postman

Importe a collection Postman (se disponível) ou utilize os exemplos de cURL acima para criar requests no Postman.