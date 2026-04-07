# 💳 FinTrack API

API REST de controle financeiro pessoal desenvolvida com Spring Boot 3, Spring Security, JWT e PostgreSQL.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- MapStruct
- Lombok
- Swagger / OpenAPI 3

## ⚙️ Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

## 🛠️ Como rodar

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/fintrack-api.git
cd fintrack-api
```

### 2. Criar o banco de dados
```sql
CREATE DATABASE fintrack;
```

### 3. Configurar variáveis de ambiente
Crie um arquivo `src/main/resources/application-local.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fintrack
    username: postgres
    password: sua_senha

application:
  security:
    jwt:
      secret: seu-secret-jwt-muito-seguro-aqui
```

### 4. Rodar a aplicação
```bash
mvn spring-boot:run
```

O Flyway irá rodar as migrations automaticamente na primeira execução.

## 📖 Documentação

Swagger UI disponível em: `http://localhost:8080/swagger-ui.html`

## 🔐 Autenticação

Todos os endpoints (exceto `/api/auth/register` e `/api/auth/login`) exigem token JWT.

No Swagger, clique em **Authorize** e insira: `Bearer {seu_token}`

## 📋 Endpoints

### Auth
| Método | Endpoint            | Descrição          |
|--------|---------------------|--------------------|
| POST   | /api/auth/register  | Registrar usuário  |
| POST   | /api/auth/login     | Login e JWT        |

### Usuários
| Método | Endpoint     | Descrição                   |
|--------|--------------|-----------------------------|
| GET    | /api/users/me| Perfil do usuário logado    |
| PUT    | /api/users/me| Atualizar nome/senha        |

### Contas
| Método | Endpoint          | Descrição            |
|--------|-------------------|----------------------|
| POST   | /api/accounts     | Criar conta          |
| GET    | /api/accounts     | Listar contas        |
| GET    | /api/accounts/{id}| Buscar por ID        |
| PUT    | /api/accounts/{id}| Atualizar conta      |
| DELETE | /api/accounts/{id}| Desativar conta      |

### Categorias
| Método | Endpoint             | Descrição                  |
|--------|----------------------|----------------------------|
| POST   | /api/categories      | Criar categoria             |
| GET    | /api/categories      | Listar categorias           |
| GET    | /api/categories/{id} | Buscar por ID               |
| PUT    | /api/categories/{id} | Atualizar categoria         |
| DELETE | /api/categories/{id} | Desativar categoria         |

### Transações
| Método | Endpoint               | Descrição                        |
|--------|------------------------|----------------------------------|
| POST   | /api/transactions      | Criar transação                  |
| GET    | /api/transactions      | Listar com filtros e paginação   |
| GET    | /api/transactions/{id} | Buscar por ID                    |
| PUT    | /api/transactions/{id} | Atualizar transação              |
| DELETE | /api/transactions/{id} | Deletar transação                |

**Filtros disponíveis (query params):**
- `startDate` — ex: `2025-01-01`
- `endDate`   — ex: `2025-01-31`
- `type`      — `INCOME` ou `EXPENSE`
- `categoryId`
- `accountId`
- `page`      — padrão: 0
- `size`      — padrão: 10

### Relatórios
| Método | Endpoint                             | Descrição                          |
|--------|--------------------------------------|------------------------------------|
| GET    | /api/reports/monthly-summary         | Resumo mensal (year, month)        |
| GET    | /api/reports/expenses-by-category    | Gastos por categoria no período    |
| GET    | /api/reports/balance-evolution       | Evolução do saldo (12 meses)       |

## 🏗️ Arquitetura

```
controller  →  service  →  repository  →  PostgreSQL
                ↑
            domain (entity, enums, exception)
                ↑
            dto (request / response)
                ↑
            mapper (MapStruct)
```

## 📁 Estrutura de Pacotes

```
com.fintrack/
├── config/           — SecurityConfig, OpenApiConfig
├── controller/       — Endpoints REST
├── service/          — Regras de negócio
├── repository/       — Spring Data JPA
├── domain/
│   ├── entity/       — User, Account, Category, Transaction
│   ├── enums/        — AccountType, TransactionType
│   └── exception/    — Exceções de domínio
├── dto/
│   ├── request/      — DTOs de entrada
│   └── response/     — DTOs de saída
├── mapper/           — MapStruct mappers
├── security/         — JwtFilter, UserDetailsService
└── handler/          — GlobalExceptionHandler, ErrorResponse
```
