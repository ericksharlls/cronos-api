# Cronos

## Sobre o projeto

Trata-se da API do sistema Cronos: Um sistema para gerenciamento de alocações de turmas e agendamentos diversos nos espaços físicos (salas de aula, laboratórios, dentre outros) do Setor de Aulas IV da Universidade Federal do Rio Grande do Norte.

## Tecnologias Usadas

O projeto utiliza as seguintes tecnologias:

- Linguagem de Programação Java, versão 11;
- Projetos do Ecossistema Spring como **Spring Boot**, **Spring Data JPA**, **Spring Web**, **Spring Web MVC**, **Spring Core**;
- **Lombok** para geração de código boilerplate;
- **Hibernate Validator** para validação dos dados de entrada da API;
- **JUnit 5**, **Rest Assured**, **Hamcrest** e **Mockito** para realização de testes de integração da API e testes unitários;
- **Flyway** para controle de versão do Banco de Dados;
- **Hibernate** como provedor JPA e framework de mapeamento objeto-relacional;
- **Ehcache** para caching de objetos e consultas com informações provenientes do banco de dados;
- **MySQL** como SGBD;
- **SpringDoc** para documentação da API no modelo do Swagger, seguindo a especificação do OpenAPI 3.

## Testes de integração

O comando abaixo deve ser executado para rodar todos os testes de integração do projeto:

`./mvnw verify -D DATASOURCE_USERNAME=${USER} -D DATASOURCE_PASSWORD=${PASSWORD}`

A seguir, o comando para rodar todos os testes de integração de uma classe específica do projeto:

`mvn -Dit.test=ClassName verify -D DATASOURCE_USERNAME=${USER} -D DATASOURCE_PASSWORD=${PASSWORD}`

Logo abaixo, o comando para rodar um método de testes específico de uma classe do projeto:

`mvn -Dit.test=ClassName#methodName verify -D DATASOURCE_USERNAME=${USER} -D DATASOURCE_PASSWORD=${PASSWORD}`

***ClassName*** é o nome da classe sem a entensão ***.java***  
***methodName*** é o nome de um método específico de uma classe de testes  
***${USER}*** e  ***${PASSWORD}*** devem ser substituídos, respectivamente, pelos usuário e senha de conexão com seu banco de dados local.

## Documentação da API

Após a execução do projeto, a documentação da API pode ser acessada pela URL a seguir:

`http://localhost:8080/swagger-ui/index.html`
