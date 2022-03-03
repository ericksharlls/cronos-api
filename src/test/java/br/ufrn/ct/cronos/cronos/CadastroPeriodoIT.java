package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;

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

import br.ufrn.ct.cronos.api.model.input.PeriodoInput;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//@SpringBootTest //fornece as funcionalidades do Spring Boot nos testes
@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroPeriodoIT {
	@Autowired
	private Flyway flyway;
	
	@LocalServerPort
	private int port;
	
	@Autowired 
	private PeriodoRepository periodoRepository;
	
	private Periodo testePeriodo1;
	private Periodo testePeriodo2;
	private PeriodoInput periodoInput;
	
	private Short anoPeriodoTeste;  
	private Short valorPeriodoTeste;
	private int contadorDePeriodosSalvos;
	
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final String ENTIDADE_EM_USO_PROBLEM_TYPE = "Entidade em uso";
	private static final int PERIODO_ID_INEXISTENTE = 81;
	
	@BeforeEach
	public void setup () {
		// para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/periodos";

		flyway.migrate();
		
		init();
	}
	// cria novas instancias dos objetos quando necessario
	private void clear () {
		testePeriodo1 = new Periodo();
		testePeriodo2 = new Periodo();
		periodoInput = new PeriodoInput(); 
	}
	// Popula os objetos e setta no banco de dados para realizar as demais operações
	private void init() {
		clear();
		
		anoPeriodoTeste = 2022;
		valorPeriodoTeste = 1;
		
		testePeriodo1.setNome("teste 01");
		testePeriodo1.setDescricao("objeto de teste");
		testePeriodo1.setDataInicio(LocalDate.now());
		testePeriodo1.setDataTermino(LocalDate.of(2022, 3, 22));
		testePeriodo1.setIsPeriodoLetivo(true);
		testePeriodo1.setAno(anoPeriodoTeste);
		testePeriodo1.setIsPeriodoLetivo(true);
		testePeriodo1.setPeriodo(valorPeriodoTeste);
		
		periodoRepository.save(testePeriodo1);
		
		testePeriodo2.setNome("teste 02");
		testePeriodo2.setDescricao("objeto de teste");
		testePeriodo2.setDataInicio(LocalDate.of(2022, 4, 19));
		testePeriodo2.setDataTermino(LocalDate.of(2022, 6, 19));
		testePeriodo2.setIsPeriodoLetivo(true);
		testePeriodo2.setAno(anoPeriodoTeste);
		testePeriodo2.setIsPeriodoLetivo(true);
		testePeriodo2.setPeriodo(++valorPeriodoTeste);
		
		periodoRepository.save(testePeriodo2);
		
		contadorDePeriodosSalvos = (int) periodoRepository.count();
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	
	@Test
	public void deveAtribuirId_QuandoCadastrarPeriodoComDadosCorretos() {
		retornaPeriodoComDadosCorretos();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.post()
		.then()
			.body("id", notNullValue())
			.body("nome", equalTo(periodoInput.getNome()))
			.body("descricao", equalTo(periodoInput.getDescricao()))
			.body("dataInicio", equalTo(periodoInput.getDataInicio().toString()))
			.body("dataTermino", equalTo(periodoInput.getDataTermino().toString()))
			.body("isPeriodoLetivo", equalTo(periodoInput.getIsPeriodoLetivo()))
			.body("ano", equalTo(2022))
			.body("periodo", equalTo(3))
			.statusCode(HttpStatus.CREATED.value());
		
	}
	
	@Test
	public void deveFalhar_QuandoHouverPeriodoComNomeJaExistente () {
		retornaPeriodoComNomeJaExistente(testePeriodo1.getNome());
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	@Test
	public void deveFalhar_QuandoHouverPeriodoEmIntervaloJaExistente() {
		retornaPeriodoComIntervaloExistente();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	@Test
	public void deveFalhar_QuandoHouverCamposVazios() {
		retornaPeriodoComAtributosVazios();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", "descricao"));
	}
	
	@Test
	public void deveFalhar_QuandoHouverCamposNulos() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", 
											   "descricao",
											   "dataInicio",
											   "dataTermino",
											   "isPeriodoLetivo",
											   "ano",
											   "periodo"));
	}
	
	/**** TESTE COM REQUISIÇÃO PUT ****/
	@Test
	public void deveRetornarSucesso_QuandoAtualizarPeriodoComDadosCorretos() {
		retornaPeriodoComNomeEDatasAtualizadas(testePeriodo2);
		
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.put("/{periodoId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveFalhar_QuandoAtualizarPeriodoParaNomeJaExistente() {
		retornaPeriodoAtualizadoComNomeExistente(testePeriodo2);
		
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.put("/{periodoId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	@Test
	public void deveFalhar_QuandoAtualizarPeriodoParaIntervaloJaExistente() {
		retornaPeriodoAtualizadoComDatasJaExistentes(testePeriodo2);
		
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.put("/{periodoId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	@Test
	public void deveFalhar_QuandoAtualizarComCamposVazios() {
		retornaPeriodoComAtualizarAtributosVazios(testePeriodo2);
		
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.put("/{periodoId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", "descricao"));
	}
	
	@Test
	public void deveFalhar_QuandoAtualizarComCamposNulos() {
		this.periodoInput = new PeriodoInput(); 
	
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(periodoInput)
		.when()
			.put("/{periodoId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", 
											   "descricao",
											   "dataInicio",
											   "dataTermino",
											   "isPeriodoLetivo",
											   "ano",
											   "periodo"));
	}
	
	/**** TESTES COM REQUISIÇÕES DELETE ****/
	
	@Test
	public void deveRetornarSucesso_QuandoExcluirPeriodoComSucesso(){
		
		given()
			.pathParam("periodoId", testePeriodo2.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{periodoId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	@Test
	public void deveFalhar_QuandoExcluirPeriodoInexistente(){
		given()
			.pathParam("periodoId", PERIODO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{periodoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	/*** TESTES COM RESQUISIÇÃO DO TIPO GET ****/
	@Test
	public void deveRetornarQuantidadeCorretaDePeriodos_QuandoBuscarPeriodos() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("content", hasSize(contadorDePeriodosSalvos));
	}
	
	@Test
	public void deveRetornarSucesso_QuandoConsultarPeriodoExistente() {
		given()
			.pathParam("periodoId", testePeriodo1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{periodoId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(testePeriodo1.getNome()));
	}
	@Test
	public void deveRetornarSucesso_QuandoConsultarPeriodoInexistente() {
		given()
			.pathParam("periodoId", PERIODO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{periodoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	private void retornaPeriodoComDadosCorretos() {
		periodoInput.setNome("teste 03");
		periodoInput.setDescricao("objeto de teste");
		periodoInput.setDataInicio(LocalDate.of(2022, 7, 19));
		periodoInput.setDataTermino(LocalDate.of(2022, 9, 19));
		periodoInput.setIsPeriodoLetivo(true);
		periodoInput.setAno(anoPeriodoTeste);
		periodoInput.setPeriodo(++valorPeriodoTeste);
		
	}
	
	private void retornaPeriodoComNomeJaExistente(String nomeJaExistente) {
		retornaPeriodoComDadosCorretos();
		
		periodoInput.setNome(nomeJaExistente);
	}
	
	private void retornaPeriodoComIntervaloExistente() {
		retornaPeriodoComDadosCorretos();
		
		periodoInput.setDataInicio(testePeriodo1.getDataInicio());
		periodoInput.setDataTermino(testePeriodo1.getDataTermino());
	}
	
	private void retornaPeriodoComAtributosVazios() {
		retornaPeriodoComDadosCorretos();
		
		periodoInput.setNome("");
		periodoInput.setDescricao("");
	}
	
	private void retornaPeriodoComNomeEDatasAtualizadas(Periodo periodoSalvo) {
		periodoInput.setNome("teste 03");
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(LocalDate.of(2022, 7, 19));
		periodoInput.setDataTermino(LocalDate.of(2022, 9, 19));
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setPeriodo(periodoSalvo.getPeriodo());
	}
	
	private void retornaPeriodoAtualizadoComNomeExistente(Periodo periodoSalvo) {
		periodoInput.setNome(testePeriodo1.getNome());
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(periodoSalvo.getDataInicio());
		periodoInput.setDataTermino(periodoSalvo.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setPeriodo(periodoSalvo.getPeriodo());
	}
	
	private void retornaPeriodoAtualizadoComDatasJaExistentes(Periodo periodoSalvo) {
		periodoInput.setNome(periodoSalvo.getNome());
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(testePeriodo1.getDataInicio());
		periodoInput.setDataTermino(testePeriodo1.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setPeriodo(periodoSalvo.getPeriodo());
	}
	
	private void retornaPeriodoComAtualizarAtributosVazios(Periodo periodoSalvo) {
		periodoInput.setNome("");
		periodoInput.setDescricao("");
		periodoInput.setDataInicio(periodoSalvo.getDataInicio());
		periodoInput.setDataTermino(periodoSalvo.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setPeriodo(periodoSalvo.getPeriodo());
		
	}
}
