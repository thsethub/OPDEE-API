# Deployment Guide - OPDEE API

Guia completo para deploy da aplicação OPDEE em diferentes ambientes.

## Visão Geral

Este guia cobre as estratégias de deployment para a OPDEE-API em ambientes de desenvolvimento, homologação e produção.

## Pré-requisitos

### Infraestrutura Mínima

**Desenvolvimento:**
- 2 CPU cores
- 4GB RAM
- 20GB storage
- Java 17+
- PostgreSQL 12+

**Produção:**
- 4 CPU cores
- 8GB RAM
- 100GB storage SSD
- Java 17+
- PostgreSQL 14+
- Reverse proxy (Nginx)
- SSL certificate

## 🐳 Deploy com Docker

### Docker Compose - Completo

Crie o arquivo `docker-compose.prod.yml`:

```yaml
version: '3.9'

services:
  postgres:
    image: postgres:16-alpine
    container_name: opdee_postgres
    environment:
      POSTGRES_DB: opdee
      POSTGRES_USER: opdee
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U opdee"]
      interval: 30s
      timeout: 10s
      retries: 3

  opdee-api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: opdee_api
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/opdee
      SPRING_DATASOURCE_USERNAME: opdee
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: true
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    volumes:
      - app_logs:/app/logs

  nginx:
    image: nginx:alpine
    container_name: opdee_nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/nginx/ssl
    depends_on:
      - opdee-api
    restart: unless-stopped

volumes:
  postgres_data:
    driver: local
  app_logs:
    driver: local
```

### Dockerfile Otimizado

```dockerfile
# Multi-stage build
FROM maven:3.9-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jre-slim
WORKDIR /app

# Criar usuário não-root
RUN groupadd -r opdee && useradd -r -g opdee opdee

# Instalar dependências do sistema
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copiar JAR
COPY --from=builder /app/target/OPDEE-*.jar app.jar

# Criar diretórios
RUN mkdir -p /app/logs && chown -R opdee:opdee /app

# Definir usuário
USER opdee

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Configurações JVM
ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:+UseStringDeduplication"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### Configuração do Nginx

Arquivo `nginx.conf`:

```nginx
events {
    worker_connections 1024;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    # Logs
    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    # Gzip
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

    # Rate limiting
    limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;

    upstream opdee_backend {
        server opdee-api:8080;
    }

    server {
        listen 80;
        server_name your-domain.com;
        return 301 https://$server_name$request_uri;
    }

    server {
        listen 443 ssl http2;
        server_name your-domain.com;

        # SSL configuration
        ssl_certificate /etc/nginx/ssl/cert.pem;
        ssl_certificate_key /etc/nginx/ssl/key.pem;
        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384;

        # Security headers
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header Referrer-Policy "no-referrer-when-downgrade" always;
        add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;

        location / {
            limit_req zone=api burst=20 nodelay;
            
            proxy_pass http://opdee_backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # Timeouts
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }

        location /actuator/health {
            proxy_pass http://opdee_backend;
            access_log off;
        }
    }
}
```

### Deploy com Docker

```bash
# 1. Clonar repositório
git clone https://github.com/thsethub/OPDEE-API.git
cd OPDEE-API

# 2. Criar arquivo de ambiente
cat > .env << EOF
POSTGRES_PASSWORD=your_secure_password_here
EOF

# 3. Build e start
docker-compose -f docker-compose.prod.yml up --build -d

# 4. Verificar status
docker-compose -f docker-compose.prod.yml ps
docker-compose -f docker-compose.prod.yml logs -f opdee-api
```

## 🖥️ Deploy Manual (Bare Metal)

### 1. Preparação do Servidor

```bash
# Ubuntu/Debian
sudo apt update && sudo apt upgrade -y

# Instalar Java 17
sudo apt install openjdk-17-jdk -y

# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib -y

# Instalar Nginx
sudo apt install nginx -y

# Criar usuário para a aplicação
sudo useradd -r -s /bin/false opdee
sudo mkdir -p /opt/opdee
sudo chown opdee:opdee /opt/opdee
```

### 2. Configuração do Banco

```bash
# Acessar PostgreSQL
sudo -u postgres psql

-- Criar banco e usuário
CREATE DATABASE opdee;
CREATE USER opdee WITH ENCRYPTED PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE opdee TO opdee;
GRANT ALL ON SCHEMA public TO opdee;
\q

# Configurar pg_hba.conf
sudo vim /etc/postgresql/14/main/pg_hba.conf
# Adicionar: local opdee opdee md5

# Reiniciar PostgreSQL
sudo systemctl restart postgresql
```

### 3. Build da Aplicação

```bash
# No servidor de desenvolvimento ou CI/CD
git clone https://github.com/thsethub/OPDEE-API.git
cd OPDEE-API
./mvnw clean package -DskipTests

# Copiar JAR para servidor
scp target/OPDEE-*.jar user@server:/opt/opdee/opdee-api.jar
```

### 4. Configuração da Aplicação

Criar `application-production.properties`:

```properties
# Aplicação
server.port=8080
spring.application.name=OPDEE

# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/opdee
spring.datasource.username=opdee
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true

# Logging
logging.level.br.ufpe.opdee=INFO
logging.level.org.springframework.web=WARN
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.file.name=/opt/opdee/logs/opdee-api.log
logging.file.max-size=10MB
logging.file.max-history=10

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

### 5. Criar Serviço Systemd

Arquivo `/etc/systemd/system/opdee-api.service`:

```ini
[Unit]
Description=OPDEE API Service
After=network.target postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=opdee
Group=opdee
WorkingDirectory=/opt/opdee
ExecStart=/usr/bin/java -Xmx1g -Xms512m -XX:+UseG1GC \
    -Dspring.profiles.active=production \
    -jar /opt/opdee/opdee-api.jar
ExecStop=/bin/kill -15 $MAINPID
Restart=always
RestartSec=30
StandardOutput=journal
StandardError=journal

# Security
NoNewPrivileges=true
ProtectSystem=strict
ProtectHome=true
ReadWritePaths=/opt/opdee/logs

[Install]
WantedBy=multi-user.target
```

### 6. Inicializar Serviço

```bash
# Criar diretório de logs
sudo mkdir -p /opt/opdee/logs
sudo chown opdee:opdee /opt/opdee/logs

# Ativar e iniciar serviço
sudo systemctl daemon-reload
sudo systemctl enable opdee-api
sudo systemctl start opdee-api

# Verificar status
sudo systemctl status opdee-api
sudo journalctl -u opdee-api -f
```

## ☁️ Deploy na Cloud

### AWS EC2 + RDS

#### 1. Infraestrutura

```bash
# Criar RDS PostgreSQL
aws rds create-db-instance \
    --db-instance-identifier opdee-db \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --master-username opdee \
    --master-user-password YourPassword123 \
    --allocated-storage 20 \
    --vpc-security-group-ids sg-xxxxxxxxx

# Criar EC2
aws ec2 run-instances \
    --image-id ami-0c7217cdde317cfec \
    --count 1 \
    --instance-type t3.small \
    --key-name your-key \
    --security-group-ids sg-xxxxxxxxx \
    --user-data file://user-data.sh
```

#### 2. User Data Script

```bash
#!/bin/bash
yum update -y
yum install -y java-17-amazon-corretto docker
systemctl start docker
systemctl enable docker

# Instalar Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Deploy da aplicação
cd /opt
git clone https://github.com/thsethub/OPDEE-API.git
cd OPDEE-API
docker-compose -f docker-compose.prod.yml up -d
```

### Google Cloud Run

#### 1. Dockerfile para Cloud Run

```dockerfile
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/OPDEE-*.jar app.jar

# Configurar para Cloud Run
ENV PORT=8080
EXPOSE $PORT

CMD ["java", "-Xmx512m", "-jar", "app.jar", "--server.port=${PORT}"]
```

#### 2. Deploy

```bash
# Build e push
gcloud builds submit --tag gcr.io/PROJECT_ID/opdee-api

# Deploy
gcloud run deploy opdee-api \
    --image gcr.io/PROJECT_ID/opdee-api \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated \
    --set-env-vars="SPRING_PROFILES_ACTIVE=production" \
    --memory=1Gi \
    --cpu=1
```

## 🔄 CI/CD Pipeline

### GitHub Actions

Arquivo `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: opdee_test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        
    - name: Run tests
      run: |
        ./mvnw clean test
        ./mvnw jacoco:report
    
    - name: Upload coverage
      uses: codecov/codecov-action@v3

  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build application
      run: ./mvnw clean package -DskipTests
    
    - name: Build Docker image
      run: |
        docker build -t opdee-api:${{ github.sha }} .
        docker tag opdee-api:${{ github.sha }} opdee-api:latest
    
    - name: Deploy to production
      run: |
        # SSH para servidor e deploy
        echo "${{ secrets.DEPLOY_KEY }}" | tr -d '\r' | ssh-add -
        ssh ${{ secrets.DEPLOY_USER }}@${{ secrets.DEPLOY_HOST }} '
          cd /opt/opdee &&
          docker pull opdee-api:latest &&
          docker-compose down &&
          docker-compose up -d
        '
```

## 📊 Monitoramento e Logs

### Configuração de Logs

```yaml
# docker-compose.yml - Adicionar serviço de logs
  loki:
    image: grafana/loki:latest
    ports:
      - "3100:3100"
    volumes:
      - ./loki-config.yml:/etc/loki/local-config.yaml

  promtail:
    image: grafana/promtail:latest
    volumes:
      - /var/log:/var/log
      - ./promtail-config.yml:/etc/promtail/config.yml

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```

### Health Checks

Adicionar ao `application.properties`:

```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.health.db.enabled=true

# Metrics
management.metrics.export.prometheus.enabled=true
```

### Alertas Básicos

Script de monitoramento:

```bash
#!/bin/bash
# health-check.sh

URL="https://your-domain.com/actuator/health"
SLACK_WEBHOOK="your-slack-webhook"

if ! curl -f -s $URL > /dev/null; then
    curl -X POST -H 'Content-type: application/json' \
        --data '{"text":"🚨 OPDEE API está DOWN!"}' \
        $SLACK_WEBHOOK
fi
```

## 🔒 Segurança

### Configurações de Segurança

```properties
# application-production.properties

# Security headers
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict

# Disable banner
spring.main.banner-mode=off

# Error handling
server.error.include-stacktrace=never
server.error.include-message=never
```

### Firewall

```bash
# Ubuntu UFW
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow ssh
sudo ufw allow 80
sudo ufw allow 443
sudo ufw enable
```

## 🔧 Troubleshooting

### Problemas Comuns

1. **Erro de Conexão com Banco**:
```bash
# Verificar conectividade
telnet database-host 5432
# Verificar logs
docker logs opdee_postgres
```

2. **Erro de Memória**:
```bash
# Ajustar JVM
export JAVA_OPTS="-Xmx2g -Xms1g"
```

3. **Erro de Permissão**:
```bash
# Verificar proprietário
sudo chown -R opdee:opdee /opt/opdee
```

### Logs Importantes

```bash
# Application logs
sudo journalctl -u opdee-api -f

# Nginx logs
sudo tail -f /var/log/nginx/error.log

# PostgreSQL logs
sudo tail -f /var/log/postgresql/postgresql-14-main.log
```

## 📋 Checklist de Deploy

### Pré-Deploy
- [ ] Testes passando
- [ ] Backup do banco
- [ ] Verificar dependências
- [ ] Validar configurações
- [ ] Plano de rollback

### Deploy
- [ ] Build da aplicação
- [ ] Stop da aplicação antiga
- [ ] Deploy da nova versão
- [ ] Executar migrações
- [ ] Start da aplicação
- [ ] Verificar health checks

### Pós-Deploy
- [ ] Testes funcionais
- [ ] Verificar logs
- [ ] Monitorar métricas
- [ ] Comunicar sucesso
- [ ] Documentar mudanças

---

## Suporte

Para suporte com deployment, entre em contato com a equipe de desenvolvimento ou consulte os logs da aplicação para diagnóstico de problemas.