
package br.ufrn.ct.cronos.cronos;

import org.flywaydb.core.Flyway;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.TestPropertySource;

import br.ufrn.ct.cronos.api.model.input.PredioInput;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//@SpringBootTest //fornece as funcionalidades do Spring Boot nos testes
@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroPredioIT {

	@Autowired
	private Flyway flyway;

	@LocalServerPort
	private int port;

	@Autowired
	private PredioRepository predioRepository;
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

	@Autowired
	protected ModelMapper modelMapper;
	
	@Autowired
	private SalaRepository salaRepository;

	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String ENTIDADE_EM_USO_PROBLEM_TYPE = "Entidade em uso";
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";

	private static final int PREDIO_ID_INEXISTENTE = 100;
	private Predio predioSetorAulasIV;
	private PerfilSalaTurma perfilSalaTurmaConvencional;
	private int quantidadePrediosCadastrados;

	@BeforeEach
	public void setUp() {
		// para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/predios";

		flyway.migrate();
		prepararDados();
	}
	
	/**
	 * Testes com o POST
	 */
	@Test
	public void deveAtribuirId_QuandoCadastrarPredioComDadosCorretos() {
		PredioInput predioInput = retornaPredioComDadosCorretos();
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(predioInput)
			.when()
				.post()
			.then()
				.body("id", Matchers.notNullValue())
				.body("nome", Matchers.equalTo(predioInput.getNome()))
				.body("descricao", Matchers.equalTo(predioInput.getDescricao()))
				.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveFalhar_QuandoCadastrarPredioComCamposVazios(){
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(retornaPredioComCamposVazios())
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", Matchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", Matchers.hasItems("nome", "descricao"));
	}

	@Test
	public void deveFalhar_QuandoCadastrarPredioComCamposNulos(){
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(retornaPredioComCamposNulos())
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", Matchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", Matchers.hasItems("nome", "descricao"));
				
	}

	@Test
	public void deveFalhar_QuandoCadastrarPredioComCamposDeTamanhosExcedidos(){
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(retornaPredioComCamposDeTamanhosExcedidos())
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", Matchers.equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", Matchers.hasItems("nome", "descricao"));
	}

	@Test
	public void deveRetornarStatus400_QuandoCadastrarPredioComNomeJaExistente() {
		RestAssured
		.given()
			.body(retornaPredioComNomeJaExistente())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", Matchers.equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	/**
	 * Testes com o PUT
	 */
	@Test
	public void deveRetornarCodigo200_QuandoAtualizarPredioComDadosCorretos(){
		PredioInput predioInput = new PredioInput();
		String novoNome = "Novo Nome";
		String novaDescricao = "Descrição Nova";
		predioInput.setNome(novoNome);
		predioInput.setDescricao(novaDescricao);

		RestAssured
			.given()
				.pathParam("predioId", predioSetorAulasIV.getId())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(predioInput)
			.when()
				.put("/{predioId}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("nome", Matchers.equalTo(novoNome))
				.body("descricao", Matchers.equalTo(novaDescricao));
	}

	/**
	 * Testes com o GET
	 */
	@Test
	public void deveRetornarQuantidadeCorretaDePredios_QuandoConsultarPredios() {
		RestAssured
		.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("content", Matchers.hasSize(quantidadePrediosCadastrados));
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarPredioExistente() {
		RestAssured
		.given()
			.pathParam("predioId", predioSetorAulasIV.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{predioId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", Matchers.equalTo(predioSetorAulasIV.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarPredioInexistente() {
		RestAssured
		.given()
			.pathParam("predioId", PREDIO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{predioId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	/**
	 * Testes com o DELETE
	 */
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoExcluirPredioSemUso(){
		RestAssured
			.given()
				.pathParam("predioId", predioSetorAulasIV.getId())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(predioSetorAulasIV)
			.when()
				.delete("/{predioId}")
			.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void deveFalhar_QuandoExcluirPredioEmUso(){
		salvarSalaNoPredioDoSetorAulasIV();
		RestAssured
			.given()
				.pathParam("predioId", predioSetorAulasIV.getId())
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(predioSetorAulasIV)
			.when()
				.delete("/{predioId}")
			.then()
				.statusCode(HttpStatus.CONFLICT.value())
				.body("title", Matchers.equalTo(ENTIDADE_EM_USO_PROBLEM_TYPE));
	}

	@Test
	public void deveFalhar_QuandoExcluirPredioInexistente(){
		RestAssured
			.given()
				.pathParam("predioId", PREDIO_ID_INEXISTENTE)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(predioSetorAulasIV)
			.when()
				.delete("/{predioId}")
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("title", Matchers.equalTo(RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE));
	}

	private void prepararDados() {
		Predio predio1 = new Predio();
		predio1.setNome("Prédio Administrativo do CT");
		predio1.setDescricao("Prédio Administrativo do Centro de Tecnologia");
		predioRepository.save(predio1);

		predioSetorAulasIV = new Predio();
		predioSetorAulasIV.setNome("Setor de Aulas IV");
		predioSetorAulasIV.setDescricao("Setor de Aulas IV");
		predioRepository.save(predioSetorAulasIV);

		Predio predio3 = new Predio();
		predio3.setNome("NTI");
		predio3.setDescricao("Núcleo de Tecnologia Industrial");
		predioRepository.save(predio3);

		quantidadePrediosCadastrados = (int) predioRepository.count();
	}

	private PredioInput retornaPredioComDadosCorretos(){
		PredioInput predioInput = new PredioInput();
		predioInput.setNome("Prédio para Testes");
		predioInput.setDescricao("Descrição do Prédio para Testes");
		
		return predioInput;
	}

	private PredioInput retornaPredioComCamposVazios(){
		PredioInput predioInput = new PredioInput();
		predioInput.setNome("");
		predioInput.setDescricao("");
		
		return predioInput;
	}

	private String retornaPredioComCamposNulos(){
		return "{\n" +
				" \n}";
	}

	private PredioInput retornaPredioComCamposDeTamanhosExcedidos(){
		PredioInput predioInput = new PredioInput();
		predioInput.setNome("aaabbbbcccdddeeeeffffgggg(Aqui estão 51 caracteres)");
		predioInput.setDescricao("aaaaabbbbbbcccccdddddeeeeeeffffffggggggghhhhhhiiiiijjjjjjjkkkkkklllllmmmmm(Aqui estão 101 caracteres)");
		
		return predioInput;
	}

	private PredioInput retornaPredioComNomeJaExistente(){
		PredioInput predioInput = new PredioInput();
		predioInput.setNome(predioSetorAulasIV.getNome());
		predioInput.setDescricao(predioSetorAulasIV.getDescricao());
		
		return predioInput;
	}

	private void salvarSalaNoPredioDoSetorAulasIV(){
		Sala sala = new Sala();

		sala.setNome("Sala D1");
		sala.setDescricao("Sala D1 do Setor de Aulas IV");
		sala.setCapacidade(50);
		sala.setDistribuir(true);
		sala.setPerfilSalaTurma(retornaPerfilSalaTurmaConvencional());
		sala.setPredio(predioSetorAulasIV);
		sala.setTipoQuadro("Branco");
		sala.setUtilizarNaDistribuicao(true);
		sala.setUtilizarNoAgendamento(false);
		
		salaRepository.save(sala);
	}

	private PerfilSalaTurma retornaPerfilSalaTurmaConvencional(){
		perfilSalaTurmaConvencional = new PerfilSalaTurma();
		perfilSalaTurmaConvencional.setNome("Convencional");
		perfilSalaTurmaConvencional.setDescricao("Perfil para aulas convencionais");
		perfilSalaTurmaRepository.save(perfilSalaTurmaConvencional);
		
		return perfilSalaTurmaConvencional;
	}
	
}
