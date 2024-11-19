CREATE TABLE acessos
(
    id           UUID                        NOT NULL,
    usuario_id   VARCHAR(255),
    ambiente_id  UUID,
    ativado      BOOLEAN                     NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    tipo_usuario VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_acessos PRIMARY KEY (id)
);

CREATE TABLE ambientes
(
    id         UUID                        NOT NULL,
    nome       VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    topic      VARCHAR(255)                NOT NULL,
    mensagem   VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_ambientes PRIMARY KEY (id)
);

CREATE TABLE broker_config
(
    id         UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ip_address VARCHAR(255)                NOT NULL,
    port       VARCHAR(255)                NOT NULL,
    username   VARCHAR(255)                NOT NULL,
    password   VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_brokerconfig PRIMARY KEY (id)
);

CREATE TABLE historico
(
    id           UUID                        NOT NULL,
    usuario_uuid VARCHAR(255),
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    perfil_id    UUID,
    ambiente_id  UUID,
    mensagem     VARCHAR(255),
    CONSTRAINT pk_historico PRIMARY KEY (id)
);

CREATE TABLE tipo_perfil
(
    id         UUID                        NOT NULL,
    nome       VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tipoperfil PRIMARY KEY (id)
);

CREATE TABLE usuarios
(
    uuid          VARCHAR(255)                NOT NULL,
    nome_completo VARCHAR(255)                NOT NULL,
    email_ufpe    VARCHAR(255)                NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    superuser     BOOLEAN                     NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (uuid)
);

ALTER TABLE ambientes
    ADD CONSTRAINT uc_ambientes_nome UNIQUE (nome);

ALTER TABLE usuarios
    ADD CONSTRAINT uc_usuarios_email_ufpe UNIQUE (email_ufpe);

ALTER TABLE acessos
    ADD CONSTRAINT FK_ACESSOS_ON_AMBIENTE FOREIGN KEY (ambiente_id) REFERENCES ambientes (id);

ALTER TABLE acessos
    ADD CONSTRAINT FK_ACESSOS_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuarios (uuid);

ALTER TABLE historico
    ADD CONSTRAINT FK_HISTORICO_ON_AMBIENTE FOREIGN KEY (ambiente_id) REFERENCES ambientes (id);

ALTER TABLE historico
    ADD CONSTRAINT FK_HISTORICO_ON_PERFIL FOREIGN KEY (perfil_id) REFERENCES tipo_perfil (id);

ALTER TABLE historico
    ADD CONSTRAINT FK_HISTORICO_ON_USUARIO_UUID FOREIGN KEY (usuario_uuid) REFERENCES usuarios (uuid);

