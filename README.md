# Sales API

API REST em Spring Boot para criar vendas e consultar um resumo de vendas por vendedor em um periodo informado.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- JUnit e MockMvc

## Como executar

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API fica disponivel em `http://localhost:8080`.

## Endpoints

### Criar venda

`POST /sales`

```json
{
  "saleDate": "2026-04-22",
  "value": 125.50,
  "sellerId": 10,
  "sellerName": "Ana Souza"
}
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "saleDate": "2026-04-22",
  "value": 125.50,
  "sellerId": 10,
  "sellerName": "Ana Souza"
}
```

### Listar vendedores por periodo

`GET /sellers?startDate=2026-04-20&endDate=2026-04-22`

Resposta `200 OK`:

```json
[
  {
    "sellerName": "Ana Souza",
    "totalSales": 2,
    "dailySalesAverage": 0.67
  }
]
```

`totalSales` representa a quantidade de vendas do vendedor no periodo. `dailySalesAverage` e calculada por `totalSales / quantidade de dias do periodo`, considerando as datas inicial e final.

## Testes

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```
