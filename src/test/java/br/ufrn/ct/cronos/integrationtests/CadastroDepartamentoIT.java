package br.ufrn.ct.cronos.integrationtests;

import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.repository.DepartamentoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroDepartamentoIT {

    @Autowired
    private Flyway flyway;

    @LocalServerPort
    private int port;

    @Autowired
    DepartamentoRepository departamentoRepository;

    Departamento departamentoDomainObjectTerceiroCadastrado;

    @BeforeEach
    public void setup () {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/departamentos";

        flyway.migrate();

        init();
    }

    public void init() {
        Departamento departamentoDomainObject01 = preparaDepartamentoDomainObject("Escola de Saúde",
                                                                                "divide o prédio com o departamento de enfermagem",
                                                                                         15L);

        departamentoRepository.save(departamentoDomainObject01);

        Departamento departamentoDomainObject02 = preparaDepartamentoDomainObject("Departamento de Pedagogia",
                                                                                "Localizado no setor 2",
                                                                                          16L);
        departamentoRepository.save(departamentoDomainObject02);

        departamentoDomainObjectTerceiroCadastrado = preparaDepartamentoDomainObject("Departamento de Ciências Biológicas",
                                                                                     "CB",
                                                                                     17L);

        departamentoRepository.save(departamentoDomainObjectTerceiroCadastrado);
    }

    public Departamento preparaDepartamentoDomainObject (String nome, String descricao, Long idSigaa) {
        Departamento departamentoDomainObject = new Departamento();

        departamentoDomainObject.setNome(nome);
        departamentoDomainObject.setDescricao(descricao);
        departamentoDomainObject.setIdSigaa(idSigaa);

        return departamentoDomainObject;
    }

    @Test
    public void deveRetornaPaginacaoCorreta_QuandoRealizarBusca () {
            given()
                .queryParam("size", "1")
                .queryParam("page", "2")
                .accept(ContentType.JSON)
            .when()
                 .get()
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.nome", hasItem(equalTo(departamentoDomainObjectTerceiroCadastrado.getNome())));
    }


}
