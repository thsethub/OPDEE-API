# OPDEE-API Documentation Index

Índice completo da documentação técnica do sistema OPDEE.

## 📋 Visão Geral

O OPDEE-API é um sistema backend desenvolvido em Spring Boot para gerenciamento de controle de acesso a ambientes do Departamento de Engenharia Elétrica da UFPE através de trancas eletrônicas conectadas via IoT.

## 📚 Documentação por Categoria

### 🔧 Para Desenvolvedores

| Documento | Descrição | Quando Usar |
|-----------|-----------|-------------|
| [Development Guide](DEVELOPMENT.md) | Configuração do ambiente, padrões de código, debugging | Ao começar a desenvolver no projeto |
| [Architecture Overview](ARCHITECTURE.md) | Design da aplicação, padrões arquiteturais, fluxos | Para entender como o sistema funciona |
| [API Reference](API.md) | Documentação completa dos endpoints REST | Ao integrar com a API ou criar clientes |

### 🗄️ Para Administradores de Banco

| Documento | Descrição | Quando Usar |
|-----------|-----------|-------------|
| [Database Schema](DATABASE.md) | Estrutura das tabelas, relacionamentos, consultas | Ao trabalhar com dados ou relatórios |

### 🚀 Para DevOps/Deploy

| Documento | Descrição | Quando Usar |
|-----------|-----------|-------------|
| [Deployment Guide](DEPLOYMENT.md) | Instruções de deploy, Docker, CI/CD | Ao fazer deploy em qualquer ambiente |

## 🎯 Cenários de Uso

### "Sou novo no projeto"
1. Leia o [README principal](../README.md) para visão geral
2. Configure ambiente seguindo [Development Guide](DEVELOPMENT.md)
3. Entenda a arquitetura em [Architecture Overview](ARCHITECTURE.md)

### "Preciso integrar com a API"
1. Consulte [API Reference](API.md) para endpoints
2. Veja exemplos de uso e códigos de resposta
3. Teste com cURL ou Postman

### "Vou fazer deploy"
1. Leia [Deployment Guide](DEPLOYMENT.md)
2. Escolha estratégia: Docker, Bare Metal ou Cloud
3. Configure ambiente e monitore

### "Preciso entender os dados"
1. Estude [Database Schema](DATABASE.md)
2. Veja relacionamentos entre entidades
3. Use consultas de exemplo

### "Quero contribuir"
1. Configure ambiente em [Development Guide](DEVELOPMENT.md)
2. Siga padrões de código estabelecidos
3. Escreva testes e documentação

## 🔍 Referência Rápida

### Endpoints Principais
- `GET /api/ambiente` - Listar ambientes
- `POST /api/acesso` - Criar acesso
- `PUT /api/acesso/{id}` - Alternar permissão
- `GET /api/historico` - Histórico de acessos

### Entidades Core
- **Usuario** - Usuários do sistema
- **Ambiente** - Salas/laboratórios  
- **Acesso** - Permissões de acesso
- **Historico** - Log de auditoria

### Tecnologias
- Spring Boot 3.3.3
- Java 17
- PostgreSQL
- MQTT (IoT)
- Docker

## 🆘 Troubleshooting

### Problemas Comuns

| Problema | Solução | Documento |
|----------|---------|-----------|
| Erro de build | Ver troubleshooting | [Development Guide](DEVELOPMENT.md#troubleshooting) |
| Erro de conexão DB | Verificar configuração | [Development Guide](DEVELOPMENT.md#configuração-do-banco-de-dados) |
| API retorna 500 | Verificar logs | [Deployment Guide](DEPLOYMENT.md#troubleshooting) |
| Performance lenta | Otimizar queries | [Architecture Overview](ARCHITECTURE.md#performance-e-escalabilidade) |

### Onde Buscar Ajuda

1. **Logs da aplicação**: `./mvnw spring-boot:run` ou `docker logs`
2. **Health check**: `GET /actuator/health`
3. **Database**: Consultas em [DATABASE.md](DATABASE.md)
4. **Issues**: [GitHub Issues](https://github.com/thsethub/OPDEE-API/issues)

## 📊 Status da Documentação

| Documento | Status | Última Atualização |
|-----------|--------|-------------------|
| README.md | ✅ Completo | Atual |
| API.md | ✅ Completo | Atual |
| DATABASE.md | ✅ Completo | Atual |
| ARCHITECTURE.md | ✅ Completo | Atual |
| DEVELOPMENT.md | ✅ Completo | Atual |
| DEPLOYMENT.md | ✅ Completo | Atual |

## 🔄 Atualizações

Esta documentação é mantida junto com o código. Quando houver mudanças:

1. **API changes** → Atualizar API.md
2. **Schema changes** → Atualizar DATABASE.md  
3. **New features** → Atualizar todos relevantes
4. **Deploy changes** → Atualizar DEPLOYMENT.md

## 📝 Convenções da Documentação

- **Markdown** para todos os documentos
- **Emojis** para categorização visual
- **Tabelas** para informações estruturadas
- **Code blocks** para exemplos
- **Links relativos** entre documentos

---

**Dica**: Mantenha este índice como bookmark para navegação rápida na documentação!

Última atualização: Versão atual do projeto