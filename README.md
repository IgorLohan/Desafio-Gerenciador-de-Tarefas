# Gerenciador de Tarefas

API REST de um gerenciador de tarefas, feita em **Java + Spring Boot**, para a disciplina de Programação Web Backend.

A ideia deste README é explicar o projeto do jeito que a gente entende quando está começando no backend: o que cada parte faz, como subir o banco e como testar no Insomnia.

## O que é backend?

O **frontend** é a tela (site, app). O **backend** é o servidor que fica atrás: recebe pedidos, aplica regras e grava/lê dados no banco.

Aqui não tem tela. A gente conversa com a API por HTTP, usando ferramentas como Insomnia ou Postman.

Fluxo básico:

```
Insomnia  →  Controller  →  Service  →  Repository  →  PostgreSQL
   JSON         URL            regra         SQL           banco
```

1. Você manda um JSON para uma URL.
2. O **Controller** recebe o pedido.
3. O **Service** aplica a regra de negócio (ex.: tarefa precisa de um projeto).
4. O **Repository** fala com o banco.
5. A API devolve JSON.

## O que este sistema faz?

Dá para cadastrar e gerenciar:

- **Projeto** — agrupa tarefas
- **Responsável** — quem pode executar a tarefa
- **Tarefa** — o item de trabalho

Regras do modelo:

- Toda tarefa **precisa** pertencer a um projeto.
- O responsável da tarefa é **opcional**.
- `status` e `prioridade` são enums (lista fechada de valores).
- `concluida_em` só é preenchido quando o status vira `CONCLUIDA`.

As partes opcionais do enunciado (etiqueta e comentário) **não** entram neste projeto.

## Tecnologias

- Java 25
- Spring Boot 4
- Spring Web (API REST)
- Spring Data JPA (acesso ao banco)
- Hibernate (cria/atualiza as tabelas a partir das classes)
- Validation (valida o JSON de entrada)
- Lombok (menos código repetido nas entidades)
- PostgreSQL (banco de dados)

## Como o código está organizado

Cada recurso (projeto, responsável, tarefa) tem as mesmas camadas. Isso é o padrão clássico de API Spring.

| Arquivo | Papel |
|---|---|
| `*Controller` | Porta de entrada da API. Define a URL e o método HTTP. |
| `*Service` / `*ServiceImpl` | Regras de negócio. |
| `*Repository` | Acesso ao banco (find, save, delete). |
| Entidade (`Projeto`, `Tarefa`, `Responsavel`) | Representa a tabela no banco. |
| `*CadastroRequest` | JSON que a API **recebe**. |
| `*Response` | JSON que a API **devolve**. |

Por que Request e Response separados da entidade? Para não expor o banco direto na API e mandar só o que o cliente precisa.

Enums da tarefa:

- **Status:** `PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDA`
- **Prioridade:** `BAIXA`, `MEDIA`, `ALTA`

## Como rodar o projeto

### 1. Subir o PostgreSQL

O serviço do PostgreSQL precisa estar **Running**. Se o pgAdmin der `connection timeout`, o banco está parado.

Crie o banco (no pgAdmin ou no `psql`):

```sql
CREATE DATABASE tarefas;
```

### 2. Conferir a conexão

No arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tarefas
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

A senha precisa ser a mesma do usuário `postgres` da sua máquina.

`spring.jpa.hibernate.ddl-auto=update` faz o Hibernate criar/atualizar as tabelas sozinho. Na primeira execução ele cria:

- `tb_projeto`
- `tb_responsavel`
- `tb_tarefa`

### 3. Iniciar a aplicação

No IntelliJ, rode a classe `TarefasApplication`.

Quando aparecer `Tomcat started on port 8080`, a API está no ar:

```
http://localhost:8080
```

## Como testar no Insomnia

O erro mais comum no começo: colocar o JSON na aba **Params**. Isso manda os dados na URL e a API responde `400`.

Faça assim em todo `POST` e `PUT`:

1. Método certo (`POST`, `GET`, `PUT` ou `DELETE`)
2. Cole a URL
3. Abra a aba **Body**
4. Escolha **JSON**
5. Cole o JSON
6. Deixe **Params** vazio
7. Envie

A URL deve ficar só `http://localhost:8080/projeto`, sem `?nome=...` no final.

Cadastre **projeto** e **responsável** primeiro. Depois use os `id` que voltaram para criar a tarefa.

### Projeto

`POST http://localhost:8080/projeto`

```json
{
  "nome": "Projeto Web",
  "descricao": "Atividade de Programação Web Backend"
}
```

### Responsável

`POST http://localhost:8080/responsavel`

```json
{
  "nome": "Igor Lima",
  "email": "igor@email.com"
}
```

### Tarefa

`POST http://localhost:8080/tarefa`

Troque `projetoId` e `responsavelId` pelos ids reais. `responsavelId` pode ser omitido.

```json
{
  "titulo": "Montar relatório",
  "descricao": "Relatório semanal do projeto",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "prazo": "2026-09-25",
  "projetoId": 1,
  "responsavelId": 1
}
```

## Endpoints

A base é sempre `http://localhost:8080`.

### Projeto — `/projeto`

| Método | URL | O que faz |
|---|---|---|
| POST | `/projeto` | Cadastra |
| GET | `/projeto` | Lista todos |
| GET | `/projeto/1` | Busca pelo id |
| PUT | `/projeto/1` | Atualiza |
| DELETE | `/projeto/1` | Exclui (só se não tiver tarefa) |

### Responsável — `/responsavel`

| Método | URL | O que faz |
|---|---|---|
| POST | `/responsavel` | Cadastra |
| GET | `/responsavel` | Lista todos |
| GET | `/responsavel/1` | Busca pelo id |
| PUT | `/responsavel/1` | Atualiza |
| DELETE | `/responsavel/1` | Exclui (só se não tiver tarefa) |

### Tarefa — `/tarefa`

| Método | URL | O que faz |
|---|---|---|
| POST | `/tarefa` | Cadastra |
| GET | `/tarefa` | Lista todas |
| GET | `/tarefa/1` | Busca pelo id |
| PUT | `/tarefa/1` | Atualiza |
| DELETE | `/tarefa/1` | Exclui |

Filtros opcionais na listagem:

```
GET http://localhost:8080/tarefa?status=PENDENTE&prioridade=ALTA&projetoId=1
```

## Relacionamento das tabelas

```
tb_projeto 1 ────── N tb_tarefa N ────── 1 tb_responsavel
              (obrigatório)         (opcional)
```

Uma tarefa sempre aponta para um projeto. Ela pode existir sem responsável.

## Dicas se der erro

- **`connection timeout` no pgAdmin:** o serviço do PostgreSQL está parado. Inicie o `postgresql-x64-18`.
- **API não sobe:** banco `tarefas` não existe, ou usuário/senha em `application.properties` está errado.
- **`400 Bad Request` no Insomnia:** JSON foi para Params em vez do Body, ou falta campo obrigatório (`nome`, `titulo`, `projetoId`).
- **`404`:** o id não existe.
- **`409` ao deletar projeto/responsável:** ainda existe tarefa ligada a ele.
