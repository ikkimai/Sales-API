# Sales API

API REST em Spring Boot para criar vendas e consultar um resumo de vendas por vendedor em um periodo informado.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- JUnit e MockMvc

## Pre-requisitos

- Java 17 ou superior instalado
- Terminal aberto na raiz do projeto, onde ficam os arquivos `pom.xml`, `mvnw` e `mvnw.cmd`

## Como executar

Linux/macOS:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponivel em `http://localhost:8080`.

Ao abrir `http://localhost:8080` no navegador, uma interface simples permite criar vendas e consultar o resumo por periodo.

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

Os comandos abaixo apenas executam os testes automatizados e encerram o processo. Eles nao iniciam a API para navegacao.

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```
# Sales API

API REST desenvolvida em Spring Boot para gerenciamento de vendas e consulta de resumo por vendedor em um período informado.

> Foi incluída uma interface web simples apenas para facilitar a visualização e testes da API.

---

## 🚀 Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database (em memória)
- JUnit e MockMvc

---

## 📋 Pré-requisitos

- Java 17 ou superior instalado
- Terminal aberto na raiz do projeto (onde está o `pom.xml`)

---

## ▶️ Como executar

### Linux/macOS

```bash
./mvnw spring-boot:run