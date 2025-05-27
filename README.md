# Diario IA TFG

Este proyecto es un microservicio Spring Boot que ofrece varios módulos integrados con IA y funcionalidad de gestión personal:

* **Autenticación y usuarios**
* **Conversaciones IA**
* **Planificador semanal**
* **Hábitos**
* **Metas (Goals)**
* **Diario personal**

---

## Requisitos

* Java 17+
* Maven o Gradle
* Base de datos H2 (configurada automáticamente en `file` mode)

## Arrancar la aplicación

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/usuario/diario-ia-tfg.git
   cd diario-ia-tfg
   ```

2. Compilar y ejecutar con Maven:

   ```bash
   ./mvnw spring-boot:run
   ```

   o con Gradle:

   ```bash
   ./gradlew bootRun
   ```

3. La aplicación quedará escuchando en `http://localhost:8092/`.

---

## Endpoints

Todas las rutas están bajo el prefijo base `http://localhost:8092/api/v1` y requieren un token JWT (obtenido al hacer login).

### 1. Autenticación y Usuarios

* **Registro**

  ```http
  POST /auth/register
  ```

  **Body**:

  ```json
  {
    "username": "usuario",
    "password": "contraseña",
    "email": "email@dominio.com"
  }
  ```

* **Login**

  ```http
  POST /auth/login
  ```

  **Body**:

  ```json
  {
    "username": "usuario",
    "password": "contraseña"
  }
  ```

  **Response**:

  ```json
  { "token": "eyJhbGci..." }
  ```

* **Logout**

  ```http
  POST /auth/logout
  ```

  Enviar el token en header `Authorization: Bearer <token>`.

---

### 2. Conversaciones IA

* **Iniciar conversación**

  ```http
  POST /api/v1/ai/conversations
  ```

  **Extiende**:

  ```json
  { }
  ```

  **Response**:

  ```json
  {
    "id": 1,
    "startedAt": "2025-05-27T11:13:40.295424",
    "messages": []
  }
  ```

* **Enviar mensaje**

  ```http
  POST /api/v1/ai/conversations/{conversationId}/messages
  ```

  **Body**:

  ```json
  { "content": "¡Hola IA!" }
  ```

  **Response**:

  ```json
  {
    "id": 4,
    "sender": "AI",
    "content": "[MOCK AI] ...",
    "sentAt": "2025-..."
  }
  ```

* **Listar mensajes**

  ```http
  GET /api/v1/ai/conversations/{conversationId}/messages
  ```

---

### 3. Planificador semanal

* **Generar plan**

  ```http
  POST /api/v1/plans
  ```

  **Body**:

  ```json
  { "weekStart": "2025-05-26" }
  ```

  **Response**:

  ```json
  {
    "id": 3,
    "weekStart": "2025-05-26",
    "generatedAt": "2025-05-27T11:20:00",
    "tasks": [
      { "id": 10, "dayOfWeek": "Lunes", "description": "...", "type": "HÁBITO", "completed": false },
      ...
    ]
  }
  ```

* **Obtener plan actual**

  ```http
  GET /api/v1/plans/current
  ```

* **Marcar tarea**

  ```http
  PUT /api/v1/plans/{planId}/tasks/{taskId}
  ```

  **Body**:

  ```json
  { "completed": true }
  ```

---

### 4. Hábitos

* **Generar hábitos IA**

  ```http
  POST /api/v1/habits/generate
  ```

  **Body**:

  ```json
  { }
  ```

  **Crea 3–5 hábitos basados en el contexto de la conversación y los guarda.**

* **Listar hábitos**

  ```http
  GET /api/v1/habits
  ```

* **Registrar log**

  ```http
  POST /api/v1/habits/{habitId}/logs
  ```

  **Body**:

  ```json
  { "logDate": "2025-05-27", "done": true }
  ```

---

### 5. Metas (Goals)

* **Generar metas IA**

  ```http
  POST /api/v1/goals/generate
  ```

  **Body**:

  ```json
  { }
  ```

  **Crea 3–5 objetivos SMART en base al contexto y los guarda.**

* **Listar metas**

  ```http
  GET /api/v1/goals
  ```

* **Actualizar estado**

  ```http
  PUT /api/v1/goals/{goalId}
  ```

  **Body**:

  ```json
  { "status": "IN_PROGRESS" }
  ```

---

### 6. Diario personal

* **Crear entrada**

  ```http
  POST /api/v1/journal
  ```

  **Body**:

  ```json
  { "entryDate": "2025-05-27", "content": "Hoy hice ejercicio..." }
  ```

* **Listar entradas**

  ```http
  GET /api/v1/journal
  ```

* **Obtener una entrada**

  ```http
  GET /api/v1/journal/{id}
  ```

* **Actualizar entrada**

  ```http
  PUT /api/v1/journal/{id}
  ```

  **Body** igual a POST.

* **Borrar entrada**

  ```http
  DELETE /api/v1/journal/{id}
  ```

---

