CREATE TABLE acessos
(
    id           UUID NOT NULL,
    usuario_id   VARCHAR(255),
    ambiente_id  VARCHAR(255),
    ativado      BOOLEAN,
    created_at   TIMESTAMP,
    tipo_usuario VARCHAR(255),
    CONSTRAINT pk_acessos PRIMARY KEY (id)
);

CREATE TABLE ambientes
(
    id         UUID NOT NULL,
    nome       VARCHAR(255),
    created_at TIMESTAMP,
    topic      VARCHAR(255),
    messagem   VARCHAR(255),
    CONSTRAINT pk_ambientes PRIMARY KEY (id)
);

CREATE TABLE broker_config
(
    id         VARCHAR(255) NOT NULL,
    created_at VARCHAR(255),
    ip_address VARCHAR(255),
    port       VARCHAR(255),
    username   VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_brokerconfig PRIMARY KEY (id)
);

CREATE TABLE historico
(
    id         VARCHAR(255) NOT NULL,
    nome       VARCHAR(255),
    created_at VARCHAR(255),
    perfil     VARCHAR(255),
    ambiente   VARCHAR(255),
    mensagem   VARCHAR(255),
    CONSTRAINT pk_historico PRIMARY KEY (id)
);

CREATE TABLE tipo_perfil
(
    id         UUID NOT NULL,
    nome       VARCHAR(255),
    created_at TIMESTAMP,
    CONSTRAINT pk_tipoperfil PRIMARY KEY (id)
);

CREATE TABLE usuarios
(
    uuid          VARCHAR(255) NOT NULL,
    nome_completo VARCHAR(255),
    email_ufpe    VARCHAR(255),
    created_at    VARCHAR(255),
    superuser     BOOLEAN,
    CONSTRAINT pk_usuarios PRIMARY KEY (uuid)
);