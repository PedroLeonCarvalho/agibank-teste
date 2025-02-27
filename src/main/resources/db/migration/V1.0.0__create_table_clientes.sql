CREATE TABLE clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(13),
    endereco VARCHAR(255) NOT NULL,
    saldo DECIMAL(18,2) NOT NULL CHECK (saldo >= 0),

    CONSTRAINT chk_nome_minimo CHECK (CHAR_LENGTH(nome) >= 3),
    CONSTRAINT chk_telefone CHECK (CHAR_LENGTH(telefone) BETWEEN 11 AND 13),

    PRIMARY KEY (id)
);