package br.ufrn.ct.cronos.cronos;

import br.ufrn.ct.cronos.api.model.input.*;
import br.ufrn.ct.cronos.domain.model.*;
import br.ufrn.ct.cronos.domain.repository.*;
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

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroTurmaIT {
    @Autowired
    private Flyway flyway;

    @LocalServerPort
    private int port;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private PredioRepository predioRepository;

    private PerfilSalaTurma perfilSalaTurmaDomainObject;

    private Departamento departamentoDomainObject;

    private Periodo periodoDomainObject;

    private Predio predioDomainObject;

    private Turma turmaDomainObject;

    TurmaInput turmaInput;

    int quantidadeDeTurmasCriadas;

    private static final int ID_INEXISTENTE = 100;

    private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

    @BeforeEach
    public void setUp() {
        // para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/turmas";

        flyway.migrate();

        init();
    }

    private void init() {
        turmaDomainObject = new Turma();
        turmaInput = new TurmaInput();

        criarPerfilSalaTurma();
        criarDepartamento();
        criarPeriodo();
        criarPredio();

        turmaDomainObject.setHorario("24M12");
        turmaDomainObject.setDocente("Samuel Felix");
        turmaDomainObject.setNomeDisciplina("Programação Concorrente");
        turmaDomainObject.setCodigoDisciplina("DCA1201");
        turmaDomainObject.setLocal("Bloco A");
        turmaDomainObject.setSala("A1");
        turmaDomainObject.setCapacidade(45);
        turmaDomainObject.setNumero("001");
        turmaDomainObject.setAlunosMatriculados(20);
        turmaDomainObject.setDistribuir(false);
        turmaDomainObject.setPerfil(perfilSalaTurmaDomainObject);
        turmaDomainObject.setPredio(predioDomainObject);
        turmaDomainObject.setPeriodo(periodoDomainObject);
        turmaDomainObject.setDepartamento(departamentoDomainObject);

        turmaRepository.save(turmaDomainObject);

        quantidadeDeTurmasCriadas++;
    }

    /**** TESTE DE REQUISIÇÕES DO TIPO POST ****/

    @Test
    public void deveAtribuirId_QuandoCadastrarTurmaComDadosCorretos() {
        criaTurmaInputComDadosCorretos();

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(turmaInput)
        .when()
            .post()
        .then()
            .body("id", notNullValue())
            .body("horario", equalTo(turmaInput.getHorario()))
            .body("docente", equalTo(turmaInput.getDocente()))
            .body("nomeDisciplina", equalTo(turmaInput.getNomeDisciplina()))
            .body("codigoDisciplina", equalTo(turmaInput.getCodigoDisciplina()))
            .body("capacidade", equalTo(turmaInput.getCapacidade()))
            .body("numero", equalTo(turmaInput.getNumero()))
            .body("alunosMatriculados", equalTo(turmaInput.getAlunosMatriculados()))
            .body("distribuir", equalTo(turmaInput.getDistribuir()))
            .body("perfil", notNullValue())
            .body("predio", notNullValue())
            .body("periodo", notNullValue())
            .body("departamento", notNullValue())
            .statusCode(HttpStatus.CREATED.value());
    }

    // TODO VALIDAR NOME DO TESTE
    @Test
    public void deveFalhar_QuandoCadastrarTurmaComMesmaPrimaryKey() {
        criaTurmaInputComMesmoCodigoHorarioNumeroEPeriodo();

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(turmaInput)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
    }

    /**** TESTE DE REQUISIÇÕES DO TIPO PUT ****/
    @Test
    public void deveRetornarCodigo200_QuandoAtualizarPredioComDadosCorretos(){
        criaTurmaInputAPartirDoDomainObjectAlterandoOProfessor();

        given()
            .pathParam("turmaId", turmaDomainObject.getId())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(turmaInput)
        .when()
            .put("/{turmaId}")
        .then()
            .statusCode(HttpStatus.OK.value());

    }
    // TODO validar nome do teste
    @Test
    public void deveFalhar_QuandoAtualizarTurmaParaUmaPrimaryKeyJaExistente() {
        Turma turmaDomainObjectAuxiliar = retornaNovoTurmaObjectSalvo();

        criaTurmaInputAPartirDoDomainObjectAlterandoParaPrimaryKeyJaExistente(turmaDomainObjectAuxiliar);

        given()
            .pathParam("turmaId", turmaDomainObject.getId())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(turmaInput)
        .when()
            .put("/{turmaId}")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));;
    }

    /**** TESTE DE REQUISIÇÕES DO TIPO DELETE ****/
    @Test
    public void deveRetornarSucesso_QuandoExcluirTurmaComSucesso() {
        given()
            .pathParam("turmaid", turmaDomainObject.getId())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .delete("/{turmaid}")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deveFalhar_QuandoExcluirTurmaComIdInexistente() {
        given()
            .pathParam("turmaid", ID_INEXISTENTE)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .delete("/{turmaid}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    /**** TESTE DE REQUISIÇÕES DO TIPO GET ****/
    @Test
    public void deveRetornarQuantidadeCorretaDeTurmas_QuandoConsultarTurmas() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(quantidadeDeTurmasCriadas));
    }

    private void criaTurmaInputAPartirDoDomainObjectAlterandoParaPrimaryKeyJaExistente(Turma turmaDomainObjectAuxiliar) {
        PerfilSalaTurmaIdInput perfilSalaTurmaIdInput = new PerfilSalaTurmaIdInput();
        PredioIdInput predioIdInput = new PredioIdInput();
        PeriodoIdInput periodoIdInput = new PeriodoIdInput();
        DepartamentoIdInput departamentoIdInput= new DepartamentoIdInput();

        perfilSalaTurmaIdInput.setId(perfilSalaTurmaDomainObject.getId());
        predioIdInput.setId(predioDomainObject.getId());
        periodoIdInput.setId(periodoDomainObject.getId());
        departamentoIdInput.setId(departamentoDomainObject.getId());

        turmaInput.setHorario(turmaDomainObjectAuxiliar.getHorario());
        turmaInput.setDocente(turmaDomainObject.getDocente());
        turmaInput.setNomeDisciplina(turmaDomainObject.getNomeDisciplina());
        turmaInput.setCodigoDisciplina(turmaDomainObjectAuxiliar.getCodigoDisciplina());
        turmaInput.setCapacidade(turmaDomainObject.getCapacidade());
        turmaInput.setNumero(turmaDomainObjectAuxiliar.getNumero());
        turmaInput.setAlunosMatriculados(turmaDomainObject.getAlunosMatriculados());
        turmaInput.setDistribuir(turmaDomainObject.getDistribuir());
        turmaInput.setPerfil(perfilSalaTurmaIdInput);
        turmaInput.setPredio(predioIdInput);
        turmaInput.setPeriodo(periodoIdInput);
        turmaInput.setDepartamento(departamentoIdInput);
    }

    private void criarPredio() {
        predioDomainObject = new Predio();
        predioDomainObject.setNome("Setor de Aulas IV");
        predioDomainObject.setDescricao("Setor de Aulas IV");

        predioRepository.save(predioDomainObject);
    }

    private void criarPeriodo() {
        periodoDomainObject = new Periodo();

        Short anoPeriodoTeste = 2022;
        Short valorPeriodoTeste = 1;

        periodoDomainObject.setNome("teste 01");
        periodoDomainObject.setDescricao("objeto de teste");
        periodoDomainObject.setDataInicio(LocalDate.of(2022, 8, 9));
        periodoDomainObject.setDataTermino(LocalDate.of(2022, 9, 22));
        periodoDomainObject.setIsPeriodoLetivo(true);
        periodoDomainObject.setAno(anoPeriodoTeste);
        periodoDomainObject.setIsPeriodoLetivo(true);
        periodoDomainObject.setNumero(valorPeriodoTeste);

        periodoRepository.save(periodoDomainObject);
    }

    private void criarDepartamento() {
        departamentoDomainObject = new Departamento();

        departamentoDomainObject.setNome("Departamento de Engenharia da Computação e Automoção");
        departamentoDomainObject.setDescricao("Departamento de Engenharia");
        departamentoDomainObject.setIdSigaa(15L);

        departamentoRepository.save(departamentoDomainObject);
    }

    private void criarPerfilSalaTurma() {
        perfilSalaTurmaDomainObject = new PerfilSalaTurma();

        perfilSalaTurmaDomainObject.setNome("Regular");
        perfilSalaTurmaDomainObject.setDescricao("Aulas para turmas regulares");


        perfilSalaTurmaRepository.save(perfilSalaTurmaDomainObject);
    }

    private TurmaInput criaTurmaInputComDadosCorretos() {


        PerfilSalaTurmaIdInput perfilSalaTurmaIdInput = new PerfilSalaTurmaIdInput();
        PredioIdInput predioIdInput = new PredioIdInput();
        PeriodoIdInput periodoIdInput = new PeriodoIdInput();
        DepartamentoIdInput departamentoIdInput= new DepartamentoIdInput();

        perfilSalaTurmaIdInput.setId(perfilSalaTurmaDomainObject.getId());
        predioIdInput.setId(predioDomainObject.getId());
        periodoIdInput.setId(periodoDomainObject.getId());
        departamentoIdInput.setId(departamentoDomainObject.getId());

        turmaInput.setHorario("24M34");
        turmaInput.setDocente("Diogo Pinheiro");
        turmaInput.setNomeDisciplina("Programação Concorrente");
        turmaInput.setCodigoDisciplina("DCA1301");
        turmaInput.setCapacidade(45);
        turmaInput.setNumero("002");
        turmaInput.setAlunosMatriculados(20);
        turmaInput.setDistribuir(false);
        turmaInput.setPerfil(perfilSalaTurmaIdInput);
        turmaInput.setPredio(predioIdInput);
        turmaInput.setPeriodo(periodoIdInput);
        turmaInput.setDepartamento(departamentoIdInput);

        return turmaInput;
    }

    private void criaTurmaInputComMesmoCodigoHorarioNumeroEPeriodo() {
        PerfilSalaTurmaIdInput perfilSalaTurmaIdInput = new PerfilSalaTurmaIdInput();
        PredioIdInput predioIdInput = new PredioIdInput();
        PeriodoIdInput periodoIdInput = new PeriodoIdInput();
        DepartamentoIdInput departamentoIdInput= new DepartamentoIdInput();

        perfilSalaTurmaIdInput.setId(perfilSalaTurmaDomainObject.getId());
        predioIdInput.setId(predioDomainObject.getId());
        periodoIdInput.setId(periodoDomainObject.getId());
        departamentoIdInput.setId(departamentoDomainObject.getId());

        turmaInput.setHorario("24M12");
        turmaInput.setDocente("Samuel Felix");
        turmaInput.setNomeDisciplina("Programação Concorrente");
        turmaInput.setCodigoDisciplina("DCA1201");
        turmaInput.setCapacidade(45);
        turmaInput.setNumero("001");
        turmaInput.setAlunosMatriculados(20);
        turmaInput.setDistribuir(false);
        turmaInput.setPerfil(perfilSalaTurmaIdInput);
        turmaInput.setPredio(predioIdInput);
        turmaInput.setPeriodo(periodoIdInput);
        turmaInput.setDepartamento(departamentoIdInput);
    }

    private void criaTurmaInputAPartirDoDomainObjectAlterandoOProfessor() {
        PerfilSalaTurmaIdInput perfilSalaTurmaIdInput = new PerfilSalaTurmaIdInput();
        PredioIdInput predioIdInput = new PredioIdInput();
        PeriodoIdInput periodoIdInput = new PeriodoIdInput();
        DepartamentoIdInput departamentoIdInput= new DepartamentoIdInput();

        perfilSalaTurmaIdInput.setId(perfilSalaTurmaDomainObject.getId());
        predioIdInput.setId(predioDomainObject.getId());
        periodoIdInput.setId(periodoDomainObject.getId());
        departamentoIdInput.setId(departamentoDomainObject.getId());

        turmaInput.setHorario(turmaDomainObject.getHorario());
        turmaInput.setDocente("Samuel Cavalcanti");
        turmaInput.setNomeDisciplina(turmaDomainObject.getNomeDisciplina());
        turmaInput.setCodigoDisciplina(turmaDomainObject.getCodigoDisciplina());
        turmaInput.setCapacidade(turmaDomainObject.getCapacidade());
        turmaInput.setNumero(turmaDomainObject.getNumero());
        turmaInput.setAlunosMatriculados(turmaDomainObject.getAlunosMatriculados());
        turmaInput.setDistribuir(turmaDomainObject.getDistribuir());
        turmaInput.setPerfil(perfilSalaTurmaIdInput);
        turmaInput.setPredio(predioIdInput);
        turmaInput.setPeriodo(periodoIdInput);
        turmaInput.setDepartamento(departamentoIdInput);
    }

    private Turma retornaNovoTurmaObjectSalvo() {
        Turma novaTurmaDomainObject = new Turma();

        novaTurmaDomainObject.setHorario("35M12");
        novaTurmaDomainObject.setCodigoDisciplina("DCA1296");
        novaTurmaDomainObject.setNumero("003");
        novaTurmaDomainObject.setDocente("Samuel Felix");
        novaTurmaDomainObject.setNomeDisciplina("Programação Funcional");
        novaTurmaDomainObject.setLocal("Bloco A");
        novaTurmaDomainObject.setSala("A1");
        novaTurmaDomainObject.setCapacidade(45);
        novaTurmaDomainObject.setAlunosMatriculados(20);
        novaTurmaDomainObject.setDistribuir(false);
        novaTurmaDomainObject.setPerfil(perfilSalaTurmaDomainObject);
        novaTurmaDomainObject.setPredio(predioDomainObject);
        novaTurmaDomainObject.setPeriodo(periodoDomainObject);
        novaTurmaDomainObject.setDepartamento(departamentoDomainObject);

        turmaRepository.save(novaTurmaDomainObject);

        return novaTurmaDomainObject;
    }


}
