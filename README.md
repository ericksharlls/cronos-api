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
- **MySQL** como SGBD.

## Testes de integração

O comando abaixo deve ser executado para rodar os testes de integração do projeto:

`./mvnw verify -D DATASOURCE_USERNAME=${USER} -D DATASOURCE_PASSWORD=${PASSWORD}`

***${USER}*** e  ***${PASSWORD}*** devem ser substituídos, respectivamente, pelos usuário e senha de conexão com seu banco de dados local.