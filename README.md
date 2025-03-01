# Agibank Teste Técnico

## Requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

Git

Docker

Java 17+

Maven

## Clonando o repositório
```
git clone git@github.com:PedroLeonCarvalho/agibank-teste.git
cd agibank-teste
```
## Configurando o Banco de Dados

### 1. Baixando a imagem do MySQL
```
docker pull mysql:latest
```
### 2. Iniciando o container do MySQL
```
docker run --name mysql_agibank -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8.0
```
### 3. Acessando o container MySQL
```
docker exec -it mysql_agibank mysql -u root -p
```
(Senha: root)

### 4. Criando os bancos de dados
```
CREATE DATABASE agibank_teste_tecnico;
CREATE DATABASE tests_agibank_teste_tecnico;
```
## Configurando o application.properties

Abra o arquivo src/main/resources/application.properties e altere as seguintes configurações:
```
spring.datasource.url=jdbc:mysql://localhost:3306/agibank_teste_tecnico
spring.datasource.username=root
spring.datasource.password=root
```
Se desejar alterar o usuário e senha do banco, modifique os valores correspondentes.

## Rodando o Projeto

### 1. Compilar e rodar a aplicação
```
mvn clean install
mvn spring-boot:run
```
A aplicação estará disponível em http://localhost:8080

## Documentação da API

Após iniciar a aplicação, acesse a documentação do Swagger:
```
http://localhost:8080/swagger-ui.html
```
### Agora seu ambiente está pronto! 🚀
![2025-03-01_12-09](https://github.com/user-attachments/assets/d9484f0b-5daf-435c-b277-eb66ec0efd49)


