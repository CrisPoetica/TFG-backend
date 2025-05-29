-- 1. Usuarios y autenticación
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(100) UNIQUE,
    roles       VARCHAR(100) NOT NULL DEFAULT 'ROLE_USER',
    first_login BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Conversaciones y mensajes
CREATE TABLE IF NOT EXISTS conversations (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    started_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS messages (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT NOT NULL,
    sender          VARCHAR(10) NOT NULL, -- 'USER' o 'AI'
    content         CLOB    NOT NULL,
    sent_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id)
);

-- 3. Plan semanal y tareas
CREATE TABLE IF NOT EXISTS plan_weeks (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    week_start   DATE    NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS tasks (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_week_id  BIGINT NOT NULL,
    day_of_week   VARCHAR(10) NOT NULL,   -- 'Lunes', 'Martes',...
    description   VARCHAR(255) NOT NULL,
    type          VARCHAR(20)  NOT NULL,  -- 'HÁBITO','META','OTRO'
    completed     BOOLEAN      NOT NULL DEFAULT FALSE,
    FOREIGN KEY (plan_week_id) REFERENCES plan_weeks(id)
);

-- 4. Seguimiento de hábitos y metas
CREATE TABLE IF NOT EXISTS habits (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(255),
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS habit_logs (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    habit_id  BIGINT NOT NULL,
    log_date  DATE    NOT NULL,
    done      BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (habit_id) REFERENCES habits(id)
);

CREATE TABLE IF NOT EXISTS goals (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    title       VARCHAR(150) NOT NULL,
    description TEXT,
    status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING', -- 'PENDING','IN_PROGRESS','DONE'
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 6. Seguimiento de ánimo
CREATE TABLE IF NOT EXISTS mood_entries (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    entry_date  DATE    NOT NULL,
    mood        VARCHAR(20) NOT NULL,
    notes       VARCHAR(1000),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- 5. Diario personal
CREATE TABLE IF NOT EXISTS journal_entries (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    entry_date DATE    NOT NULL,
    content    CLOB    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
