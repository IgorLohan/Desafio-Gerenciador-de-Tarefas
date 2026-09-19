# Gerenciador de Tarefas

API REST de um gerenciador de tarefas, feita em **Java + Spring Boot**, para a disciplina de Programação Web Backend.

A ideia deste README é explicar o projeto do jeito que a gente entende quando está começando no backend: o que cada parte faz, como subir o banco e como testar no Insomnia ou no Postman.

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

Os pacotes são separados **por camada**, como o enunciado pede:

| Pacote | Papel |
|---|---|
| `controller` | Porta de entrada da API. Só recebe o HTTP e chama o service. |
| `service` | Regras de negócio. |
| `repository` | Acesso ao banco (find, save, delete). |
| `domain` | Entidades, enums e a exceção de recurso não encontrado. |
| `dto` | JSON que a API recebe (`*Request`) e devolve (`*Response`). |

O controller **não** conversa com o repositório. A injeção é pelo construtor (`@RequiredArgsConstructor`), sem `@Autowired` em campo.

Para testar todas as operações, use:

- `Gerenciador-Tarefas.postman_collection.json` — importe no Postman (Import)
- `api-tarefas.http` — abra no IntelliJ e clique em Run em cada request

Por que Request e Response separados da entidade? Para não expor o banco direto na API e mandar só o que o cliente precisa.

Enums da tarefa:

- **Status:** `NOVA`, `EM_ANDAMENTO`, `CONCLUIDA`, `CANCELADA`
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

A URL deve ficar só `http://localhost:8080/projetos`, sem `?nome=...` no final.

## Como testar no Postman

O Postman funciona igual ao Insomnia: manda HTTP para a API e mostra a resposta. A diferença é só o visual das abas.

O erro mais comum no começo: colocar o JSON em **Params**. Isso também dá `400`.

Faça assim em todo `POST` e `PUT`:

1. Abra o Postman e clique em **New** → **HTTP Request** (ou no `+` para uma request nova)
2. Em cima, escolha o método (`POST`, `GET`, `PUT` ou `DELETE`)
3. Cole a URL, por exemplo `http://localhost:8080/projetos`
4. Abra a aba **Body** (não Params e não Authorization)
5. Marque **raw**
6. No seletor da direita, escolha **JSON** (não Text)
   - Isso já coloca o header `Content-Type: application/json`
7. Cole o JSON na caixa
8. Deixe a aba **Params** vazia
9. Clique em **Send**

A URL, no campo de cima, deve ficar só `http://localhost:8080/projetos`. Se aparecer `?nome=` ou `?{`, o JSON foi para o lugar errado.

Para `GET` e `DELETE`, não precisa de Body. Só método + URL + **Send**.

Cadastre **projeto** e **responsável** primeiro. Depois use os `id` que voltaram para criar a tarefa.

Os JSONs abaixo servem no Insomnia e no Postman. Também dá para importar `Gerenciador-Tarefas.postman_collection.json` ou abrir `api-tarefas.http`. Os bodies extras estão em `exemplos-api.json`.

### Projeto

`POST http://localhost:8080/projetos`

```json
{
  "nome": "Projeto Web",
  "descricao": "Atividade de Programação Web Backend"
}
```

### Responsável

`POST http://localhost:8080/responsaveis`

```json
{
  "nome": "Igor Lima",
  "email": "igor@email.com"
}
```

### Tarefa

`POST http://localhost:8080/tarefas`

Troque `projetoId` e `responsavelId` pelos ids reais. `responsavelId` pode ser omitido. O `status` nasce como `NOVA` e a data de criação é preenchida pelo sistema.

```json
{
  "titulo": "Montar relatório",
  "descricao": "Relatório semanal do projeto",
  "prioridade": "ALTA",
  "prazo": "2026-09-25",
  "projetoId": 1,
  "responsavelId": 1
}
```

## Endpoints

A base é sempre `http://localhost:8080`.

### Projeto — `/projetos`

| Método | URL | Sucesso |
|---|---|---|
| POST | `/projetos` | 201 |
| GET | `/projetos` | 200 |
| GET | `/projetos/1` | 200 |
| PUT | `/projetos/1` | 200 |
| DELETE | `/projetos/1` | 204 |

### Responsável — `/responsaveis`

| Método | URL | Sucesso |
|---|---|---|
| POST | `/responsaveis` | 201 |
| GET | `/responsaveis` | 200 |
| GET | `/responsaveis/1` | 200 |
| PUT | `/responsaveis/1` | 200 |
| DELETE | `/responsaveis/1` | 204 |

### Tarefa — `/tarefas`

| Método | URL | Sucesso | Falha |
|---|---|---|---|
| POST | `/tarefas` | 201 + Location | — |
| GET | `/tarefas` | 200 | — |
| GET | `/tarefas/1` | 200 | 404 |
| PUT | `/tarefas/1` | 200 | 404 |
| DELETE | `/tarefas/1` | 204 | 404 |

Filtros opcionais na listagem:

```
GET http://localhost:8080/tarefas?status=NOVA
GET http://localhost:8080/tarefas?status=NOVA&projetoId=1
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
- **`400 Bad Request` no Insomnia/Postman:** JSON foi para Params em vez do Body, o Body não está em **raw + JSON**, ou falta campo obrigatório (`nome`, `titulo`, `projetoId`).
- **`404`:** o id não existe.
- **`409` ao deletar projeto/responsável:** ainda existe tarefa ligada a ele.
