package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.notNull;

import java.time.LocalDate;

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

import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.api.model.input.PeriodoIdInput;
import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//@SpringBootTest //fornece as funcionalidades do Spring Boot nos testes
@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroFeriadoIT {
	
	@Autowired
	private Flyway flyway;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private PeriodoRepository periodoRepository;
	
	private Feriado feriadoDomainObject;
	private FeriadoInput feriadoInput;
	private Periodo periodoDomainObject;
	private int contadorDeFeriadosSalvos;
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int FERIADO_ID_INEXISTENTE = 81;
	private static final int PERIODO_ID_INEXISTENTE = 19;
	
	@BeforeEach
	public void setup () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/feriados";

		flyway.migrate();
		
		init();
	}

	private void init() {
		salvaPeriodoDomainObjectNoBanco();
		
		feriadoDomainObject = new Feriado();
		
		feriadoDomainObject.setDescricao("Feriado de carnaval");
		feriadoDomainObject.setData(LocalDate.of(2022, 2, 27));
		feriadoDomainObject.setPeriodo(periodoDomainObject);
		
		feriadoRepository.save(feriadoDomainObject);
	} 
	
	private void salvaPeriodoDomainObjectNoBanco() {
		periodoDomainObject = new Periodo();
		
		short anoPeriodoTeste = 2022;
		short valorPeriodoTeste = 1;
		
		periodoDomainObject.setNome("teste 01");
		periodoDomainObject.setDescricao("objeto de teste");
		periodoDomainObject.setDataInicio(LocalDate.of(2022, 2, 9));
		periodoDomainObject.setDataTermino(LocalDate.of(2022, 3, 22));
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setAno(anoPeriodoTeste);
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setNumero(valorPeriodoTeste);
		
		periodoRepository.save(periodoDomainObject);
		
		contadorDeFeriadosSalvos = (int) periodoRepository.count();
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	@Test
	private void deveAtribuirId_QuandoCadastrarFeriadoComDadosCorretos() {
		settaDadosCorretosEmFeriadoInput();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.body("id", notNullValue())
			.body("descricao", equalTo(feriadoInput.getDescricao()))
			.body("data", equalTo(feriadoInput.getData()))
			.body("periodo", notNull())
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	private void deveFalhar_QuandoCadastrarFeriadoComDataJaExistente() {
		settaPeriodoInputComDataJaExistente();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));

	}

	@Test
	private void deveFalhar_QuandoCadastrarFeriadoComCamposNulos() {
		feriadoInput = new FeriadoInput();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("descricao",
											   "data",
											   "periodo"));
	}
	
	@Test
	private void deveFalhar_QuandoCadastrarComCamposVazios() {
		settaPeriodoInputComCamposVazios();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("descricao"));
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO PUT ****/
	@Test
	private void deveAtribuirId_QuandoAtualizarFeriadoComDadosCorretos() {
		Feriado novoFeriadoDomainObject = criaNovoFeriadoObjectDomain();
		
		settaFeriadoInputComDadosAtualizadasCorretamente(novoFeriadoDomainObject);
		
		given()
			.pathParam("idFeriado", novoFeriadoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.put("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	private void deveRetornarErro_QuandoAtualizarFeriadoComDataExistente() {
		Feriado novoFeriadoDomainObject = criaNovoFeriadoObjectDomain();
		
		settaFeriadoInputAtualizadoComDataExistente(novoFeriadoDomainObject);
		
		given()
			.pathParam("idFeriado", novoFeriadoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.put("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	private void deveRetornarErro_QuandoAtualizarComCamposNulos() {
		feriadoInput = new FeriadoInput();
		
		given()
			.pathParam("idFeriado", feriadoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.put("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("descricao",
					  						   "data",
					   						   "periodo"));
	}
	
	@Test
	private void deveRetornarErro_QuandoAtualizarComCamposVazios() {
		Feriado novoFeriadoDomainObject = criaNovoFeriadoObjectDomain();
		
		settaFeriadoInputComDadosAtualizadosComCamposVazios(novoFeriadoDomainObject);
		
		given()
			.pathParam("idFeriado", feriadoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.put("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("descricao"));
	}

	/**** TESTES COM REQUISIÇÃ0 TIPO GET ****/
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriados() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("content", hasSize(contadorDeFeriadosSalvos));
	}
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriadoPorIdExistente() {
		given()
			.pathParam("idFeriado", feriadoDomainObject.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("descricao", equalTo(feriadoDomainObject.getDescricao()));
	}
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriadoPorPeriodoExistente() {
	given()
		.pathParam("periodoId", periodoDomainObject.getId())
		.accept(ContentType.JSON)
	.when()
		.get("/por-periodo/{periodoId}")
	.then()
		.body("content", hasSize(contadorDeFeriadosSalvos));
	}
	
	@Test
	private void deveRetornarErro_QuandoBuscarFeriadoIdInexistente() {
		given()
			.pathParam("idFeriado", FERIADO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriadoPorPeriodoInexistente() {
		given()
			.pathParam("periodoId", PERIODO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/por-periodo/{periodoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO DELETE ****/
	
	@Test
	private void deveRetornarSucesso_QuandoExcluirFeriadoComSucesso() {
		given()
			.pathParam("idFeriado", feriadoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	private void deveRetornarErro_QuandoDeletarFeriadoInexistente() {
		given()
			.pathParam("idFeriado", FERIADO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void settaDadosCorretosEmFeriadoInput() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado de Carnaval");
		feriadoInput.setData(LocalDate.of(2022, 2, 28));
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaPeriodoInputComDataJaExistente() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado de carnaval");
		feriadoInput.setData(feriadoDomainObject.getData());
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaPeriodoInputComCamposVazios() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("");
		feriadoInput.setData(LocalDate.of(2022, 2, 28));
		feriadoInput.setPeriodo(periodoIdInput);
		
	}
	
	private Feriado criaNovoFeriadoObjectDomain() {
		Feriado novoFeriadoDomain = new Feriado();
		
		novoFeriadoDomain.setDescricao("Feriado de carnaval");
		novoFeriadoDomain.setData(LocalDate.of(2022, 2, 27));
		novoFeriadoDomain.setPeriodo(periodoDomainObject);
		
		feriadoRepository.save(novoFeriadoDomain);
		
		return novoFeriadoDomain;
	}
	
	private void settaFeriadoInputComDadosAtualizadasCorretamente(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput.setDescricao(feriadoSalvo.getDescricao());
		feriadoInput.setData(LocalDate.of(2022,02, 29));
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaFeriadoInputAtualizadoComDataExistente(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput.setDescricao(feriadoSalvo.getDescricao());
		feriadoInput.setData(feriadoDomainObject.getData());
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaFeriadoInputComDadosAtualizadosComCamposVazios(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput.setDescricao("");
		feriadoInput.setData(feriadoDomainObject.getData());
		feriadoInput.setPeriodo(periodoIdInput);
		
	}
}
