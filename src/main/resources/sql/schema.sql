CREATE TABLE users
(
    id         INT          NOT NULL AUTO_INCREMENT,
    username   VARCHAR(15)  NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL,
    name       VARCHAR(15)  NOT NULL,
    last_name  VARCHAR(25)  NOT NULL,
    department VARCHAR(50)  NOT NULL,
    salary     INT,
    age        TINYINT      NOT NULL,
    email      VARCHAR(50),
    enabled    TINYINT      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   INT         NOT NULL AUTO_INCREMENT,
    role VARCHAR(15) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id  INT         NOT NULL,
    username VARCHAR(15) NOT NULL,
    role_id  INT         NOT NULL,
    role     VARCHAR(15) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);