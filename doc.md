# Petx API - Documentação de Endpoints

Esta documentação detalha todos os endpoints da API Petx, incluindo métodos HTTP, rotas, e exemplos de dados recebidos/enviados.

---

## Autenticação (`/api/auth`)

### POST `/login`

**Descrição:** Autentica um usuário e retorna um token JWT.

**Corpo da requisição:**

```json
{
  "email": "admin@gmail.com",
  "password": "1234567"
}
```

---

### POST `/recuperar-token`

**Descrição:** Envia um e-mail com token de recuperação de senha.

**Corpo:**

```json
{
  "email": "vitoresmerio00@gmail.com"
}
```

---

### PUT `/nova-senha`

**Descrição:** Altera a senha informando token de recuperação.

**Corpo:**

```json
{
  "token": "<uuid-token>",
  "password": "novaSenha123"
}
```

---

## Usuários (`/api/usuarios`)

> Requer token Bearer JWT

### POST `/registrar`

**Descrição:** Registra novo usuário.

```json
{
  "name": "Nome do Usuário",
  "email": "email@exemplo.com",
  "password": "senha"
}
```

### GET `/listar`

Lista todos os usuários.

### GET `/{id}`

Busca um usuário pelo ID.

### GET `/{email}`

Busca um usuário pelo email.

### DELETE `/{id}`

Deleta um usuário pelo ID.

---

## Animais (`/api/animais`)

### POST `/`

Cadastra animal:

```json
{
  "chipId": "12345ABC",
  "nome": "Rex",
  ...
}
```

### PUT `/{id}`

Atualiza animal.

### GET `/chip/{chipId}`

Busca por chip.

### GET `/{id}`

Busca por ID.

### GET `/paged`

Busca com filtros paginados. Ex:

```
?nome=Rex&especie=CACHORRO&raca=VIRA-LATA&status=SAUDAVEL&page=0&size=10
```

### GET `/conjunto/{...}`

Consulta avançada por atributos.

### DELETE `/deletar/{id}`

Remove animal.

### POST `/obito`

Registra óbito:

```json
{
  "chipId": "u",
  "motivoObito": "cancer",
  "dataObito": "2024-04-12"
}
```

---

## Tutor (`/api/tutor`)

### POST `/`

Cadastra tutor:

```json
{
  "cpf": "12345678901",
  "nome": "João",
  "animalChips": ["x", "y"]
}
```

### GET `/todos`

Busca todos os tutores.

### GET `/id/{id}`

Busca por ID.

### GET `/cpf/{cpf}`

Busca por CPF.

### PATCH `/{cpf}`

Atualiza tutor.

### DELETE `/id/{id}`

Remove tutor.

---

## Produtos (`/api/produtos`)

### POST `/`

```json
{
  "tipoProduto": "RACAO",
  "nome": "RAC",
  "quantidade": 100
}
```

### PATCH `/{id}`

Atualiza produto.

### GET `/{id}`

Busca por ID.

### DELETE `/{id}`

Remove produto.

---

## Estoque (`/api/estoque`)

### GET `/filtro`

Busca com filtros.

### GET `/buscar/{id}`

Busca item do estoque por ID.

### GET `/racao`

Retorna quantidade de dias restantes de ração.

---

## Despesa (`/api/despesa`)

### POST `/`

```json
{
  "descricao": "Compra",
  "valor": 150.00,
  "dataPagamento": "2024-12-20"
}
```

### GET `/`

Lista despesas com filtros.

### PATCH `/{id}`

Atualiza despesa.

### DELETE `/{id}`

Deleta despesa.

---

## Consumo (`/api/consumo`)

### POST `/`

```json
{
  "porte": "GRANDE",
  "consumoDiario": 150.0
}
```

### PUT `/`

Atualiza dados de consumo.

### GET `/{porte}`

Busca por porte.

### DELETE `/{porte}`

Remove dados de consumo por porte.

---

## Boletim (`/api/boletim`)

### POST `/`

Cria boletim de ocorrência e animal associado.

### PATCH `/{id}`

Atualiza boletim e animal vinculado.

### GET `/{id}`

Busca boletim por ID.

### GET `/`

Lista boletins com paginação.

### DELETE `/{id}`

Remove boletim.

---

## Status da aplicação

### GET `/health/status`

Verifica se a API está online (em produção - Render).

### GET `/` (localhost)

Verifica se a aplicação local está online.

---

**Observações:**

- Todos os endpoints (exceto os de autenticação) requerem o uso de token Bearer no header de autorização.
- As respostas seguem o padrão JSON.
- Datas seguem o formato ISO (YYYY-MM-DD ou YYYY-MM-DDTHH\:MM\:SSZ).

