package br.ufrn.ct.cronos.integrationtests;

import br.ufrn.ct.cronos.api.model.input.FeriadoInput;
import br.ufrn.ct.cronos.api.model.input.PeriodoIdInput;
import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

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
	private static final int PERIODO_ID_INEXISTENTE = 100;
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";

	@BeforeEach
	public void setup () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/feriados";

		flyway.migrate();
		
		init();
	}

	private void init() {
		criaPeriodoDomainObjectNoBanco();
		
		feriadoDomainObject = new Feriado();
		
		feriadoDomainObject.setDescricao("Feriado de carnaval");
		feriadoDomainObject.setData(LocalDate.of(2023, 02, 28));
		feriadoDomainObject.setPeriodo(periodoDomainObject);
		
		feriadoRepository.save(feriadoDomainObject);
	} 
	
	private void criaPeriodoDomainObjectNoBanco() {
		periodoDomainObject = new Periodo();
		
		short anoPeriodoTeste = 2023;
		short valorPeriodoTeste = 1;
		
		periodoDomainObject.setNome("teste 01");
		periodoDomainObject.setDescricao("objeto de teste");
		periodoDomainObject.setDataInicio(LocalDate.of(2023, 02, 9));
		periodoDomainObject.setDataTermino(LocalDate.of(2023, 03, 22));
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setAno(anoPeriodoTeste);
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setNumero(valorPeriodoTeste);
		
		periodoRepository.save(periodoDomainObject);
		
		contadorDeFeriadosSalvos = (int) periodoRepository.count();
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	@Test
	public void deveAtribuirId_QuandoCadastrarFeriadoComDadosCorretos() {
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
			.body("data", equalTo(feriadoInput.getData().toString()))
			.body("periodo", notNullValue())
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveFalhar_QuandoCadastrarFeriadoComDataJaExistente() {
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
	public void deveFalhar_QuandoCadastrarFeriadoComCamposNulos() {
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
	public void deveFalhar_QuandoCadastrarComCamposVazios() {
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

	@Test
	public void deveRetornarSucesso_QuandoCadastrarFeriadoComDataInicialDoPeriodo() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado Teste");
		feriadoInput.setData(periodoDomainObject.getDataInicio());
		feriadoInput.setPeriodo(periodoIdInput);

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.body("id", notNullValue())
			.body("descricao", equalTo(feriadoInput.getDescricao()))
			.body("data", equalTo(feriadoInput.getData().toString()))
			.body("periodo", notNullValue())
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveRetornarSucesso_QuandoCadastrarFeriadoComDataFinalDoPeriodo() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado Teste");
		feriadoInput.setData(periodoDomainObject.getDataTermino());
		feriadoInput.setPeriodo(periodoIdInput);

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.post()
		.then()
			.body("id", notNullValue())
			.body("descricao", equalTo(feriadoInput.getDescricao()))
			.body("data", equalTo(feriadoInput.getData().toString()))
			.body("periodo", notNullValue())
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveFalhar_QuandoCadastrarFeriadoComDataAnteriorADataInicialDoPeriodo() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado Teste");
		feriadoInput.setData(periodoDomainObject.getDataInicio().minusDays(1));
		feriadoInput.setPeriodo(periodoIdInput);

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
	public void deveFalhar_QuandoCadastrarFeriadoComDataPosteriorADataFinalDoPeriodo() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado Teste");
		feriadoInput.setData(periodoDomainObject.getDataTermino().plusDays(1));
		feriadoInput.setPeriodo(periodoIdInput);

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
	
	/**** TESTES COM REQUISIÇÃ0 TIPO PUT ****/
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFeriadoComDadosCorretos() {
		Feriado novoFeriadoDomainObject = criaNovoFeriadoObjectDomain();
		
		novoFeriadoDomainObject.setDescricao("teste");
		novoFeriadoDomainObject.setData(LocalDate.of(2023,02,27));
		
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
	public void deveRetornarErro_QuandoAtualizarFeriadoComDataExistente() {
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
	public void deveRetornarErro_QuandoAtualizarComCamposNulos() {
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
	public void deveRetornarErro_QuandoAtualizarComCamposVazios() {
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

	@Test
	public void deveRetornarStatus404_QuandoAtualizarFeriadoInexistente(){
		settaDadosCorretosEmFeriadoInput();

		given()
			.pathParam("idFeriado", FERIADO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(feriadoInput)
		.when()
			.put("{idFeriado}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("title",equalTo(RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE));
	}

	/**** TESTES COM REQUISIÇÃ0 TIPO GET ****/

	@Test
	public void deveRetornarSucesso_QuandoBuscarFeriadoPorIdExistente() {
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
	public void deveRetornarSucesso_QuandoBuscarFeriadoPorPeriodoExistente() {
	given()
		.param("periodoId",periodoDomainObject.getId())
		.accept(ContentType.JSON)
	.when()
		.get("")
	.then()
		.body("content", hasSize(contadorDeFeriadosSalvos));
	}
	
	@Test
	public void deveRetornarErro_QuandoBuscarFeriadoIdInexistente() {
		given()
			.pathParam("idFeriado", FERIADO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{idFeriado}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarok_QuandoBuscarFeriadoPorPeriodoInexistente() {
		given()
			.param("periodoId", PERIODO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("")
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	
	/**** TESTES COM REQUISIÇÃ0 TIPO DELETE ****/
	
	@Test
	public void deveRetornarSucesso_QuandoExcluirFeriadoComSucesso() {
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
	public void deveRetornarErro_QuandoDeletarFeriadoInexistente() {
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
		feriadoInput.setData(LocalDate.of(2023, 02, 27));
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
		feriadoInput.setData(LocalDate.of(2023, 03, 1));
		feriadoInput.setPeriodo(periodoIdInput);
		
	}
	
		Feriado novoFeriadoDomain = new Feriado();
		private Feriado criaNovoFeriadoObjectDomain() {
		
		novoFeriadoDomain.setDescricao("Feriado de carnaval");
		novoFeriadoDomain.setData(LocalDate.of(2023, 03, 1));
		novoFeriadoDomain.setPeriodo(periodoDomainObject);
		
		feriadoRepository.save(novoFeriadoDomain);
		
		return novoFeriadoDomain;
	}
	
	private void settaFeriadoInputComDadosAtualizadasCorretamente(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao(feriadoSalvo.getDescricao());
		feriadoInput.setData(LocalDate.of(2023,02,27));
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaFeriadoInputAtualizadoComDataExistente(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao(feriadoSalvo.getDescricao());
		feriadoInput.setData(feriadoDomainObject.getData());
		feriadoInput.setPeriodo(periodoIdInput);
	}
	
	private void settaFeriadoInputComDadosAtualizadosComCamposVazios(Feriado feriadoSalvo) {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(feriadoSalvo.getPeriodo().getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("");
		feriadoInput.setData(feriadoDomainObject.getData());
		feriadoInput.setPeriodo(periodoIdInput);
		
	}
}
