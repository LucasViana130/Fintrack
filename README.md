<div align="center">

# 💳 FinTrack API

### Backend — API REST de Controle Financeiro Pessoal

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Railway](https://img.shields.io/badge/Deploy-Railway-purple?style=flat-square&logo=railway)](https://railway.app)

**[📖 Swagger UI](https://fintrack-production-39f1.up.railway.app/swagger-ui.html)** •
**[💻 Repositório Frontend](https://github.com/LucasViana130/fintrack-web)**

</div>

---

## 📌 Sobre o projeto

O **FinTrack API** é o backend de um sistema completo de controle financeiro pessoal. Ele expõe uma API REST segura que permite ao usuário gerenciar contas bancárias, registrar receitas e despesas, categorizar transações e visualizar relatórios financeiros detalhados.

> Este projeto é a camada de **backend**. O frontend React que consome esta API está em [fintrack-web](https://github.com/LucasViana130/fintrack-web).

### Problema que resolve

A maioria das pessoas não tem visibilidade real sobre para onde vai seu dinheiro. O FinTrack resolve isso oferecendo:

- Controle centralizado de múltiplas contas bancárias
- Categorização de gastos com categorias padrão + personalizadas
- Relatórios mensais com total de receitas, despesas e saldo
- Análise de gastos por categoria com percentuais
- Evolução financeira dos últimos 12 meses

---

## 🏗️ Arquitetura

```
Cliente (Frontend / Postman)
        │
        ▼
┌─────────────────────┐
│  Spring Security    │  ← Valida token JWT em toda requisição
│  JwtFilter          │
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│    Controllers      │  ← Recebe HTTP, valida campos (@Valid)
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│     Services        │  ← Regras de negócio, validações de domínio
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│    Repositories     │  ← Spring Data JPA + Criteria API
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│    PostgreSQL       │  ← Tabelas versionadas com Flyway
└─────────────────────┘
```

---

## 🛠️ Stack de tecnologias

| Tecnologia | Versão | Função |
|---|---|---|
| Java | 17 LTS | Linguagem principal |
| Spring Boot | 3.4.x | Framework web e configuração |
| Spring Security | 6.4 | Autenticação e autorização |
| Spring Data JPA | 3.4 | Abstração do banco de dados |
| PostgreSQL | 16 | Banco de dados relacional |
| Flyway | 10.x | Versionamento de banco de dados |
| JWT (auth0) | 4.4 | Tokens de autenticação stateless |
| MapStruct | 1.6.3 | Conversão Entity ↔ DTO |
| Lombok | 1.18 | Redução de código boilerplate |
| Swagger/OpenAPI | 3.0 | Documentação interativa da API |

---

## 📋 Endpoints da API

### 🔐 Autenticação (público)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/auth/register` | Cadastrar novo usuário |
| `POST` | `/api/auth/login` | Login e obtenção do token JWT |

### 👤 Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/users/me` | Perfil do usuário autenticado |
| `PUT` | `/api/users/me` | Atualizar nome ou senha |

### 🏦 Contas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/accounts` | Criar conta bancária |
| `GET` | `/api/accounts` | Listar contas com saldo calculado |
| `GET` | `/api/accounts/{id}` | Buscar conta por ID |
| `PUT` | `/api/accounts/{id}` | Editar conta |
| `DELETE` | `/api/accounts/{id}` | Desativar conta (soft delete) |

### 🏷️ Categorias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/categories` | Criar categoria personalizada |
| `GET` | `/api/categories` | Listar padrão + personalizadas |
| `PUT` | `/api/categories/{id}` | Editar categoria |
| `DELETE` | `/api/categories/{id}` | Desativar categoria |

### 💸 Transações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/transactions` | Registrar receita ou despesa |
| `GET` | `/api/transactions` | Listar com filtros e paginação |
| `GET` | `/api/transactions/{id}` | Buscar por ID |
| `PUT` | `/api/transactions/{id}` | Editar transação |
| `DELETE` | `/api/transactions/{id}` | Deletar transação |

**Filtros disponíveis:** `startDate`, `endDate`, `type`, `categoryId`, `accountId`, `page`, `size`

### 📊 Relatórios

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/reports/monthly-summary` | Resumo mensal (receitas, despesas, saldo) |
| `GET` | `/api/reports/expenses-by-category` | Gastos agrupados por categoria com % |
| `GET` | `/api/reports/balance-evolution` | Evolução do saldo nos últimos 12 meses |

---

## ⚙️ Como rodar localmente

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Clone o repositório

```bash
git clone https://github.com/LucasViana130/Fintrack.git
cd Fintrack
```

### 2. Crie o banco de dados

```sql
CREATE DATABASE fintrack;
```

### 3. Configure as credenciais

Edite `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    password: SUA_SENHA_POSTGRES

application:
  security:
    jwt:
      secret: SEU_JWT_SECRET_LONGO
```

> 💡 Para gerar um secret seguro: `openssl rand -base64 64`

### 4. Rode a aplicação

```bash
mvn spring-boot:run
```

O Flyway cria todas as tabelas automaticamente na primeira execução.

### 5. Acesse o Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Segurança

- **Autenticação JWT stateless** — token com expiração de 24h assinado com HMAC256
- **Senhas com BCrypt** — nunca armazenadas em texto puro
- **Isolamento de dados** — cada usuário acessa apenas seus próprios recursos
- **404 para recursos alheios** — nunca confirmamos a existência de dados de outros usuários (segurança OWASP)
- **CORS configurado** — apenas origens autorizadas podem consumir a API

---

## 🗄️ Banco de dados

### Migrations Flyway

| Migration | Descrição |
|-----------|-----------|
| `V1__create_users.sql` | Tabela de usuários |
| `V2__create_accounts.sql` | Tabela de contas financeiras |
| `V3__create_categories.sql` | Tabela de categorias |
| `V4__create_transactions.sql` | Tabela de transações |
| `V5__seed_default_categories.sql` | 14 categorias padrão do sistema |

> ⚠️ Nunca edite uma migration já executada. Para alterações, crie uma nova: `V6__descricao.sql`

---

## 📁 Estrutura de pastas

```
src/main/java/com/projeto/fintrack/
├── config/           → SecurityConfig, CorsConfig, OpenApiConfig
├── controller/       → 6 controllers REST
├── service/          → 7 services com regras de negócio
├── repository/       → 4 repositories + TransactionSpec
├── domain/
│   ├── entity/       → User, Account, Category, Transaction
│   ├── enums/        → AccountType, TransactionType
│   └── exception/    → Exceções de domínio customizadas
├── DTO/
│   ├── request/      → DTOs de entrada com validações
│   └── response/     → DTOs de saída
├── mapper/           → MapStruct (4 mappers)
├── security/         → JwtFilter, UserDetailsService
└── handler/          → GlobalExceptionHandler, ErrorResponse
```

---

## 🚀 Deploy

O backend está hospedado no **Railway** com PostgreSQL gerenciado.

**Variáveis de ambiente necessárias:**

| Variável | Descrição |
|----------|-----------|
| `DB_HOST` | Host do PostgreSQL |
| `DB_PORT` | Porta (5432) |
| `DB_NAME` | Nome do banco |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Secret para assinar tokens |
| `JWT_EXPIRATION` | Expiração em ms (86400000 = 24h) |

---

<div align="center">

**[🌐 API em produção](https://fintrack-production-39f1.up.railway.app/swagger-ui.html)** •
**[💻 Ver o Frontend](https://github.com/LucasViana130/fintrack-web)**

</div>
