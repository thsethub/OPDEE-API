# Database Schema - OPDEE

Documentação do esquema do banco de dados do sistema OPDEE.

## Visão Geral

O banco de dados do OPDEE utiliza PostgreSQL como SGBD principal e segue um modelo relacional para gerenciar o controle de acesso a ambientes do Departamento de Engenharia Elétrica.

## Diagrama de Relacionamentos

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   usuarios  │    │   acessos   │    │  ambientes  │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ uuid (PK)   │◄──┤ usuario_id  │   ┌┤ id (PK)     │
│ nome_completo│    │ ambiente_id ├──►│ nome        │
│ email_ufpe  │    │ id (PK)     │    │ topic       │
│ created_at  │    │ ativado     │    │ mensagem    │
│ superuser   │    │ created_at  │    │ created_at  │
└─────────────┘    │ tipo_usuario│    └─────────────┘
                   └─────────────┘
      │                  │                  │
      │                  │                  │
      ▼                  ▼                  ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  historico  │    │  tipoPerfil │    │brokerConfig │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ id (PK)     │    │ id (PK)     │    │ id (PK)     │
│ usuario_id  │    │ nome        │    │ created_at  │
│ perfil_id   ├───►│ created_at  │    │ ip_address  │
│ ambiente_id │    └─────────────┘    │ port        │
│ mensagem    │                       │ username    │
│ created_at  │                       │ password    │
└─────────────┘                       └─────────────┘
```

## Tabelas

### 🧑‍🎓 usuarios
Armazena informações dos usuários do sistema.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `uuid` | VARCHAR | Identificador único do usuário | PRIMARY KEY |
| `nome_completo` | VARCHAR | Nome completo do usuário | NOT NULL |
| `email_ufpe` | VARCHAR | Email institucional da UFPE | NOT NULL, UNIQUE |
| `created_at` | TIMESTAMP | Data/hora de criação | NOT NULL |
| `superuser` | BOOLEAN | Flag de super usuário | NOT NULL, DEFAULT false |

**Índices:**
- PRIMARY KEY: `uuid`
- UNIQUE: `email_ufpe`

### 🏢 ambientes
Representa os ambientes físicos (salas, laboratórios) que possuem controle de acesso.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `id` | UUID | Identificador único do ambiente | PRIMARY KEY, AUTO-GENERATED |
| `nome` | VARCHAR | Nome único do ambiente | NOT NULL, UNIQUE |
| `created_at` | TIMESTAMP | Data/hora de criação | NOT NULL |
| `topic` | VARCHAR | Tópico MQTT para comunicação IoT | NOT NULL |
| `mensagem` | VARCHAR | Mensagem personalizada para o ambiente | NOT NULL |

**Índices:**
- PRIMARY KEY: `id`
- UNIQUE: `nome`
- INDEX: `topic` (para consultas MQTT)

### 🔐 acessos
Tabela de junção que define quais usuários têm acesso a quais ambientes.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `id` | UUID | Identificador único do acesso | PRIMARY KEY, AUTO-GENERATED |
| `usuario_id` | VARCHAR | Referência ao usuário | NOT NULL, FOREIGN KEY |
| `ambiente_id` | UUID | Referência ao ambiente | NOT NULL, FOREIGN KEY |
| `ativado` | BOOLEAN | Status ativo/inativo do acesso | NOT NULL, DEFAULT true |
| `created_at` | TIMESTAMP | Data/hora de criação | NOT NULL |
| `tipo_usuario` | VARCHAR | Tipo/categoria do usuário | NOT NULL |

**Relacionamentos:**
- FOREIGN KEY: `usuario_id` → `usuarios.uuid`
- FOREIGN KEY: `ambiente_id` → `ambientes.id`

**Índices:**
- PRIMARY KEY: `id`
- INDEX: `usuario_id` (consultas por usuário)
- INDEX: `ambiente_id` (consultas por ambiente)
- INDEX: `ativado` (filtro por status)

### 👥 tipoPerfil
Define os tipos de perfil de usuário no sistema.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `id` | UUID | Identificador único do perfil | PRIMARY KEY, AUTO-GENERATED |
| `nome` | VARCHAR | Nome do perfil | NOT NULL |
| `created_at` | TIMESTAMP | Data/hora de criação | NOT NULL |

**Índices:**
- PRIMARY KEY: `id`
- INDEX: `nome` (busca por nome)

### 📋 historico
Log de auditoria para rastreamento de ações no sistema.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `id` | UUID | Identificador único do registro | PRIMARY KEY, AUTO-GENERATED |
| `usuario_id` | VARCHAR | Referência ao usuário | NOT NULL, FOREIGN KEY |
| `perfil_id` | UUID | Referência ao perfil | NOT NULL, FOREIGN KEY |
| `ambiente_id` | UUID | Referência ao ambiente | NOT NULL, FOREIGN KEY |
| `created_at` | TIMESTAMP | Data/hora do evento | NOT NULL |
| `mensagem` | VARCHAR | Descrição da ação | NULL |

**Relacionamentos:**
- FOREIGN KEY: `usuario_id` → `usuarios.uuid`
- FOREIGN KEY: `perfil_id` → `tipoPerfil.id`
- FOREIGN KEY: `ambiente_id` → `ambientes.id`

**Índices:**
- PRIMARY KEY: `id`
- INDEX: `created_at` (consultas por período)
- INDEX: `usuario_id` (histórico por usuário)
- INDEX: `ambiente_id` (histórico por ambiente)

### 🔧 brokerConfig
Configuração do broker MQTT para comunicação IoT.

| Campo | Tipo | Descrição | Constraints |
|-------|------|-----------|-------------|
| `id` | UUID | Identificador único da configuração | PRIMARY KEY, AUTO-GENERATED |
| `created_at` | TIMESTAMP | Data/hora de criação | NOT NULL |
| `ip_address` | VARCHAR | Endereço IP do broker MQTT | NOT NULL |
| `port` | VARCHAR | Porta de conexão | NOT NULL |
| `username` | VARCHAR | Usuário para autenticação | NOT NULL |
| `password` | VARCHAR | Senha para autenticação | NOT NULL |

**Índices:**
- PRIMARY KEY: `id`

## Relacionamentos Detalhados

### 1. Usuario ↔ Acesso (1:N)
- Um usuário pode ter múltiplos acessos
- Cada acesso pertence a exatamente um usuário
- Cascade: Ao deletar usuário, deletar acessos relacionados

### 2. Ambiente ↔ Acesso (1:N)
- Um ambiente pode ter múltiplos acessos
- Cada acesso está relacionado a exatamente um ambiente
- Cascade: Ao deletar ambiente, deletar acessos relacionados

### 3. Usuario ↔ Historico (1:N)
- Um usuário pode ter múltiplos registros de histórico
- Cada registro pertence a exatamente um usuário
- Cascade: Ao deletar usuário, deletar histórico relacionado

### 4. Perfil ↔ Historico (1:N)
- Um perfil pode ter múltiplos registros de histórico
- Cada registro está relacionado a exatamente um perfil
- Cascade: Ao deletar perfil, deletar histórico relacionado

### 5. Ambiente ↔ Historico (1:N)
- Um ambiente pode ter múltiplos registros de histórico
- Cada registro está relacionado a exatamente um ambiente
- Cascade: Ao deletar ambiente, deletar histórico relacionado

## Scripts de Criação

### Criação das Tabelas

```sql
-- Tabela usuarios
CREATE TABLE usuarios (
    uuid VARCHAR(255) PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    email_ufpe VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    superuser BOOLEAN NOT NULL DEFAULT false
);

-- Tabela ambientes
CREATE TABLE ambientes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    topic VARCHAR(255) NOT NULL,
    mensagem VARCHAR(255) NOT NULL
);

-- Tabela tipoPerfil
CREATE TABLE "tipoPerfil" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela acessos
CREATE TABLE acessos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id VARCHAR(255) NOT NULL,
    ambiente_id UUID NOT NULL,
    ativado BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_usuario VARCHAR(255) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(uuid) ON DELETE CASCADE,
    FOREIGN KEY (ambiente_id) REFERENCES ambientes(id) ON DELETE CASCADE
);

-- Tabela historico
CREATE TABLE historico (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id VARCHAR(255) NOT NULL,
    perfil_id UUID NOT NULL,
    ambiente_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensagem VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(uuid) ON DELETE CASCADE,
    FOREIGN KEY (perfil_id) REFERENCES "tipoPerfil"(id) ON DELETE CASCADE,
    FOREIGN KEY (ambiente_id) REFERENCES ambientes(id) ON DELETE CASCADE
);

-- Tabela brokerConfig
CREATE TABLE "brokerConfig" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(255) NOT NULL,
    port VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

### Índices Adicionais

```sql
-- Índices para performance
CREATE INDEX idx_acessos_usuario_id ON acessos(usuario_id);
CREATE INDEX idx_acessos_ambiente_id ON acessos(ambiente_id);
CREATE INDEX idx_acessos_ativado ON acessos(ativado);

CREATE INDEX idx_historico_created_at ON historico(created_at);
CREATE INDEX idx_historico_usuario_id ON historico(usuario_id);
CREATE INDEX idx_historico_ambiente_id ON historico(ambiente_id);

CREATE INDEX idx_ambientes_topic ON ambientes(topic);
CREATE INDEX idx_perfil_nome ON "tipoPerfil"(nome);
```

## Dados de Exemplo

### Inserção de Dados de Teste

```sql
-- Usuários de exemplo
INSERT INTO usuarios (uuid, nome_completo, email_ufpe, superuser) VALUES
('admin001', 'Administrador Sistema', 'admin@ufpe.br', true),
('prof001', 'João Silva', 'joao.silva@ufpe.br', false),
('aluno001', 'Maria Santos', 'maria.santos@ufpe.br', false);

-- Perfis de exemplo
INSERT INTO "tipoPerfil" (nome) VALUES
('Administrador'),
('Professor'),
('Aluno'),
('Técnico');

-- Ambientes de exemplo
INSERT INTO ambientes (nome, topic, mensagem) VALUES
('Laboratório de Eletrônica', 'opdee/lab_eletronica', 'Acesso liberado - Lab Eletrônica'),
('Sala de Aula 101', 'opdee/sala_101', 'Acesso liberado - Sala 101'),
('Laboratório de Automação', 'opdee/lab_automacao', 'Acesso liberado - Lab Automação');

-- Configuração MQTT de exemplo
INSERT INTO "brokerConfig" (ip_address, port, username, password) VALUES
('192.168.1.100', '1883', 'opdee_user', 'opdee_pass');
```

## Consultas Úteis

### Consultas de Relatório

```sql
-- Listar todos os acessos ativos por ambiente
SELECT 
    a.nome as ambiente,
    u.nome_completo as usuario,
    ac.tipo_usuario,
    ac.created_at as data_acesso
FROM acessos ac
JOIN usuarios u ON ac.usuario_id = u.uuid
JOIN ambientes a ON ac.ambiente_id = a.id
WHERE ac.ativado = true
ORDER BY a.nome, u.nome_completo;

-- Histórico de acessos por período
SELECT 
    u.nome_completo as usuario,
    p.nome as perfil,
    a.nome as ambiente,
    h.mensagem,
    h.created_at
FROM historico h
JOIN usuarios u ON h.usuario_id = u.uuid
JOIN "tipoPerfil" p ON h.perfil_id = p.id
JOIN ambientes a ON h.ambiente_id = a.id
WHERE h.created_at >= '2024-01-01'
ORDER BY h.created_at DESC;

-- Usuários sem acesso a nenhum ambiente
SELECT u.uuid, u.nome_completo, u.email_ufpe
FROM usuarios u
LEFT JOIN acessos ac ON u.uuid = ac.usuario_id
WHERE ac.usuario_id IS NULL;

-- Ambientes mais acessados
SELECT 
    a.nome,
    COUNT(h.id) as total_acessos
FROM ambientes a
LEFT JOIN historico h ON a.id = h.ambiente_id
GROUP BY a.id, a.nome
ORDER BY total_acessos DESC;
```

### Consultas de Manutenção

```sql
-- Verificar integridade referencial
SELECT 'acessos com usuario inexistente' as tipo, COUNT(*) as quantidade
FROM acessos ac
LEFT JOIN usuarios u ON ac.usuario_id = u.uuid
WHERE u.uuid IS NULL

UNION ALL

SELECT 'acessos com ambiente inexistente' as tipo, COUNT(*) as quantidade
FROM acessos ac
LEFT JOIN ambientes a ON ac.ambiente_id = a.id
WHERE a.id IS NULL;

-- Limpeza de registros antigos de histórico (mais de 1 ano)
DELETE FROM historico 
WHERE created_at < CURRENT_DATE - INTERVAL '1 year';
```

## Migração e Versionamento

O projeto utiliza **Flyway** para controle de versão do banco de dados. Os scripts de migração devem ser criados em `src/main/resources/db/migration/` seguindo o padrão:

```
V001__Create_initial_tables.sql
V002__Add_indexes.sql
V003__Insert_default_data.sql
```

### Exemplo de Script de Migração

```sql
-- V001__Create_initial_tables.sql
CREATE TABLE usuarios (
    uuid VARCHAR(255) PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    email_ufpe VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    superuser BOOLEAN NOT NULL DEFAULT false
);

-- Outras tabelas...
```

## Backup e Recuperação

### Backup
```bash
# Backup completo
pg_dump -h localhost -U opdee -d opdee > backup_opdee_$(date +%Y%m%d).sql

# Backup apenas dados
pg_dump -h localhost -U opdee -d opdee --data-only > dados_opdee_$(date +%Y%m%d).sql
```

### Restauração
```bash
# Restaurar backup completo
psql -h localhost -U opdee -d opdee < backup_opdee_20240101.sql

# Restaurar apenas dados
psql -h localhost -U opdee -d opdee < dados_opdee_20240101.sql
```

## Monitoramento e Performance

### Queries de Monitoramento

```sql
-- Tamanho das tabelas
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Estatísticas de uso dos índices
SELECT 
    indexname,
    tablename,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

## Considerações de Segurança

1. **Senhas**: Implementar hash das senhas do broker MQTT
2. **Logs**: Logs de auditoria nunca devem ser deletados automaticamente
3. **Backup**: Backups devem ser criptografados
4. **Acesso**: Princípio do menor privilégio para usuários do banco
5. **Conexões**: Utilizar SSL para conexões em produção

---

*Última atualização: Versão atual do schema*