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
public class CadastroPeriodoIT {
	@Autowired
	private Flyway flyway;
	
	@LocalServerPort
	private int port;
	
	@Autowired 
	private PeriodoRepository periodoRepository;
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	private Periodo periodoDomainObject;
	private PeriodoInput periodoInput;
	
	private Short anoPeriodoTeste;  
	private Short valorPeriodoTeste;
	private int contadorDePeriodosSalvos;
	
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final String ENTIDADE_EM_USO_PROBLEM_TYPE = "Entidade em uso";
	private static final int PERIODO_ID_INEXISTENTE = 81;
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";

	@BeforeEach
	public void setup () {
		// para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/periodos";

		flyway.migrate();
		
		init();
	}
	
	// Popula os objetos e setta no banco de dados para realizar as demais operações
	private void init() {
		periodoDomainObject = new Periodo();
		periodoInput = new PeriodoInput(); 
		
		anoPeriodoTeste = 2022;
		valorPeriodoTeste = 1;
		
		periodoDomainObject.setNome("teste 01");
		periodoDomainObject.setDescricao("objeto de teste");
		periodoDomainObject.setDataInicio(LocalDate.of(2022, 2, 9));
		periodoDomainObject.setDataTermino(LocalDate.of(2022, 3, 22));
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setAno(anoPeriodoTeste);
		periodoDomainObject.setIsPeriodoLetivo(true);
		periodoDomainObject.setNumero(valorPeriodoTeste);
		
		periodoRepository.save(periodoDomainObject);
				
		contadorDePeriodosSalvos = (int) periodoRepository.count();
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	
	@Test
	public void deveAtribuirId_QuandoCadastrarPeriodoComDadosCorretos() {
		settaPeriodoInputComDadosCorretos();
		
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
			.body("ano", equalTo(periodoInput.getAno().intValue())) // nao consigo passar o valor do objeto
			.body("numero", equalTo(periodoInput.getNumero().intValue())) // nao consigo passar o valor do objeto
			.statusCode(HttpStatus.CREATED.value());
		
	}
	
	@Test
	public void deveFalhar_QuandoCadastrarPeriodoComNomeJaExistente () {
		settaPeriodoInputComNomeJaExistente(periodoDomainObject.getNome());
		
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
	public void deveFalhar_QuandoCadastrarPeriodoEmIntervaloJaExistente() {
		settaPeriodoInputComIntervaloExistente();
		
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
	public void deveFalhar_QuandoCadastrarPeriodoComCamposVazios() {
		settaPeriodoInputComAtributosVazios();
		
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
	public void deveFalhar_QuandoCadastrarPeriodoComCamposNulos() {
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
											   "numero"));
	}
	
	/**** TESTE COM REQUISIÇÃO PUT ****/
	@Test
	public void deveRetornarSucesso_QuandoAtualizarPeriodoComDadosCorretos() {
		Periodo novoPeriodoDomainObject = criaNovoPeriodoDomainObject();
		
		settaPeriodoInputComNomeEDatasAtualizadas(novoPeriodoDomainObject);
		
		given()
			.pathParam("periodoId", novoPeriodoDomainObject.getId())
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
		Periodo novoPeriodoDomainObject = criaNovoPeriodoDomainObject();
		
		settaPeriodoInputAtualizadoComNomeExistente(novoPeriodoDomainObject);
		
		given()
			.pathParam("periodoId", novoPeriodoDomainObject.getId())
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
		Periodo novoPeriodoDomainObject = criaNovoPeriodoDomainObject();
		
		settaPeriodoInputAtualizadoComDatasJaExistentes(novoPeriodoDomainObject);
		
		given()
			.pathParam("periodoId", novoPeriodoDomainObject.getId())
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
		Periodo novoPeriodoDomainObject = criaNovoPeriodoDomainObject();
		
		settaPeriodoInputParaAtualizarAtributosVazios(novoPeriodoDomainObject);
		
		given()
			.pathParam("periodoId", novoPeriodoDomainObject.getId())
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
			.pathParam("periodoId", periodoDomainObject.getId())
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
											   "numero"));
	}
	@Test
	public void deveRetornarStatus404_QuandoAtualizarPeriodoInexistente(){
			settaPeriodoInputComDadosCorretos();

			given()
				.pathParam("periodoId", PERIODO_ID_INEXISTENTE)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(periodoInput)
			.when()
				.put("/{periodoId}")
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("title",equalTo(RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE));
	}
	
	/**** TESTES COM REQUISIÇÕES DELETE ****/
	
	@Test
	public void deveRetornarSucesso_QuandoExcluirPeriodoComSucesso(){
		
		given()
			.pathParam("periodoId", periodoDomainObject.getId())
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
	
	@Test
	public void deveFalhar_QuandoExcluirPeriodoEmUso() {
		criaFeriadoEmPeriodoDomainObject();
		
		given()
			.pathParam("periodoId", periodoDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{periodoId}")
		.then()
			.statusCode(HttpStatus.CONFLICT.value())
			.body("title",equalTo(ENTIDADE_EM_USO_PROBLEM_TYPE));
	}
	
	private void criaFeriadoEmPeriodoDomainObject() {
		Feriado feriado = new Feriado();
		
		feriado.setDescricao("Carnaval");
		feriado.setData(LocalDate.of(2022, 02, 25));
		feriado.setPeriodo(periodoDomainObject);
		
		feriadoRepository.save(feriado);
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
	public void deveRetornarSucesso_QuandoConsultarPeriodoPorIdExistente() {
		given()
			.pathParam("periodoId", periodoDomainObject.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{periodoId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(periodoDomainObject.getNome()));
	}
	@Test
	public void deveRetornarSucesso_QuandoConsultarPeriodoPorIdInexistente() {
		given()
			.pathParam("periodoId", PERIODO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{periodoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	private void settaPeriodoInputComDadosCorretos() {
		periodoInput.setNome("teste 03");
		periodoInput.setDescricao("objeto de teste");
		periodoInput.setDataInicio(LocalDate.of(2022, 7, 19));
		periodoInput.setDataTermino(LocalDate.of(2022, 9, 19));
		periodoInput.setIsPeriodoLetivo(true);
		periodoInput.setAno(anoPeriodoTeste);
		periodoInput.setNumero(++valorPeriodoTeste);
		
	}
	
	private void settaPeriodoInputComNomeJaExistente(String nomeJaExistente) {
		settaPeriodoInputComDadosCorretos();
		
		periodoInput.setNome(nomeJaExistente);
	}
	
	private void settaPeriodoInputComIntervaloExistente() {
		settaPeriodoInputComDadosCorretos();
		
		periodoInput.setDataInicio(periodoDomainObject.getDataInicio());
		periodoInput.setDataTermino(periodoDomainObject.getDataTermino());
	}
	
	private void settaPeriodoInputComAtributosVazios() {
		settaPeriodoInputComDadosCorretos();
		
		periodoInput.setNome("");
		periodoInput.setDescricao("");
	}
	
	private void settaPeriodoInputComNomeEDatasAtualizadas(Periodo periodoSalvo) {
		periodoInput.setNome(periodoSalvo.getDescricao());
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(LocalDate.of(2022, 7, 19));
		periodoInput.setDataTermino(LocalDate.of(2022, 9, 19));
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setNumero(periodoSalvo.getNumero());
	}
	
	private void settaPeriodoInputAtualizadoComNomeExistente(Periodo periodoSalvo) {
		periodoInput.setNome(periodoDomainObject.getNome());
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(periodoSalvo.getDataInicio());
		periodoInput.setDataTermino(periodoSalvo.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setNumero(periodoSalvo.getNumero());
	}
	
	private void settaPeriodoInputAtualizadoComDatasJaExistentes(Periodo periodoSalvo) {
		periodoInput.setNome(periodoSalvo.getNome());
		periodoInput.setDescricao(periodoSalvo.getDescricao());
		periodoInput.setDataInicio(periodoDomainObject.getDataInicio());
		periodoInput.setDataTermino(periodoDomainObject.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setNumero(periodoSalvo.getNumero());
	}
	
	private void settaPeriodoInputParaAtualizarAtributosVazios(Periodo periodoSalvo) {
		periodoInput.setNome("");
		periodoInput.setDescricao("");
		periodoInput.setDataInicio(periodoSalvo.getDataInicio());
		periodoInput.setDataTermino(periodoSalvo.getDataTermino());
		periodoInput.setIsPeriodoLetivo(periodoSalvo.getIsPeriodoLetivo());
		periodoInput.setAno(periodoSalvo.getAno());
		periodoInput.setNumero(periodoSalvo.getNumero());
		
	}
	
	private Periodo criaNovoPeriodoDomainObject() {
		 Periodo novoPeriodoDomain = new Periodo();
		 
		novoPeriodoDomain.setNome("teste 02");
		novoPeriodoDomain.setDescricao("novo objeto de teste");
		novoPeriodoDomain.setDataInicio(LocalDate.of(2022, 4, 9));
		novoPeriodoDomain.setDataTermino(LocalDate.of(2022, 5, 22));
		novoPeriodoDomain.setIsPeriodoLetivo(true);
		novoPeriodoDomain.setAno(anoPeriodoTeste);
		novoPeriodoDomain.setIsPeriodoLetivo(true);
		novoPeriodoDomain.setNumero(valorPeriodoTeste);
			
		periodoRepository.save(novoPeriodoDomain);
		
		return novoPeriodoDomain;
	}

}
