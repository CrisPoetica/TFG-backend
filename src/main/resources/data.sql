-- data.sql (idempotente)

-- 1. Usuario de prueba
INSERT INTO users (username, password, email, roles, first_login)
SELECT 'juan', '{bcrypt}$2a$10$7Dx...Hash', 'juan@example.com', 'ROLE_USER', TRUE
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'juan'
);

-- 2. Conversación de ejemplo
INSERT INTO conversations (user_id, started_at)
SELECT u.id, CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM conversations c
    WHERE c.user_id = u.id
  );

-- 3. Mensajes de ejemplo
-- Mensaje AI
INSERT INTO messages (conversation_id, sender, content, sent_at)
SELECT c.id, 'AI', '¡Hola, Juan! ¿En qué puedo ayudarte hoy?', CURRENT_TIMESTAMP
FROM conversations c
JOIN users u ON c.user_id = u.id
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM messages m
    WHERE m.conversation_id = c.id
      AND m.sender = 'AI'
      AND m.content = '¡Hola, Juan! ¿En qué puedo ayudarte hoy?'
  );

-- Mensaje USER
INSERT INTO messages (conversation_id, sender, content, sent_at)
SELECT c.id, 'USER', 'Quiero mejorar mi autoestima.', CURRENT_TIMESTAMP
FROM conversations c
JOIN users u ON c.user_id = u.id
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM messages m
    WHERE m.conversation_id = c.id
      AND m.sender = 'USER'
      AND m.content = 'Quiero mejorar mi autoestima.'
  );

-- 4. Plan semanal de prueba
INSERT INTO plan_weeks (user_id, week_start, generated_at)
SELECT u.id, DATE '2025-05-26', CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM plan_weeks pw
    WHERE pw.user_id = u.id
      AND pw.week_start = DATE '2025-05-26'
  );

-- 5. Tareas de ejemplo
-- Lunes
INSERT INTO tasks (plan_week_id, day_of_week, description, type, completed)
SELECT pw.id, 'Lunes', 'Escribir 3 cosas positivas de mi día', 'HÁBITO', FALSE
FROM plan_weeks pw
JOIN users u ON pw.user_id = u.id
WHERE u.username = 'juan'
  AND pw.week_start = DATE '2025-05-26'
  AND NOT EXISTS (
    SELECT 1 FROM tasks t
    WHERE t.plan_week_id = pw.id
      AND t.day_of_week = 'Lunes'
      AND t.description = 'Escribir 3 cosas positivas de mi día'
  );

-- Martes
INSERT INTO tasks (plan_week_id, day_of_week, description, type, completed)
SELECT pw.id, 'Martes', 'Leer artículo de desarrollo personal', 'META', FALSE
FROM plan_weeks pw
JOIN users u ON pw.user_id = u.id
WHERE u.username = 'juan'
  AND pw.week_start = DATE '2025-05-26'
  AND NOT EXISTS (
    SELECT 1 FROM tasks t
    WHERE t.plan_week_id = pw.id
      AND t.day_of_week = 'Martes'
      AND t.description = 'Leer artículo de desarrollo personal'
  );

-- 6. Hábito y log de ejemplo
INSERT INTO habits (user_id, name, description, created_at)
SELECT u.id, 'Meditación matutina', '5 minutos cada mañana', CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM habits h
    WHERE h.user_id = u.id
      AND h.name = 'Meditación matutina'
  );

INSERT INTO habit_logs (habit_id, log_date, done)
SELECT h.id, DATE '2025-05-26', FALSE
FROM habits h
JOIN users u ON h.user_id = u.id
WHERE u.username = 'juan'
  AND h.name = 'Meditación matutina'
  AND NOT EXISTS (
    SELECT 1 FROM habit_logs hl
    WHERE hl.habit_id = h.id
      AND hl.log_date = DATE '2025-05-26'
  );

-- 7. Meta de ejemplo
INSERT INTO goals (user_id, title, description, status, created_at)
SELECT u.id, 'Terminar curso de autoestima', 'Completar módulos 1 al 3', 'PENDING', CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM goals g
    WHERE g.user_id = u.id
      AND g.title = 'Terminar curso de autoestima'
  );

-- 8. Entrada de diario de ejemplo
INSERT INTO journal_entries (user_id, entry_date, content, created_at)
SELECT u.id, DATE '2025-05-26', 'Hoy me he sentido motivado tras nuestra charla.', CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'juan'
  AND NOT EXISTS (
    SELECT 1 FROM journal_entries j
    WHERE j.user_id = u.id
      AND j.entry_date = DATE '2025-05-26'
  );
