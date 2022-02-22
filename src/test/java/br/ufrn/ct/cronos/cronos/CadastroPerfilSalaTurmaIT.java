package br.ufrn.ct.cronos.cronos;

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

import br.ufrn.ct.cronos.api.model.input.PerfilSalaTurmaInput;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import org.flywaydb.core.Flyway;

@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroPerfilSalaTurmaIT {
	@Autowired
	private Flyway flyway;

	@LocalServerPort
	private int port;
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

	@Autowired
	protected ModelMapper modelMapper;
	
	private PerfilSalaTurma perfilTurma;
	private int quantidadeDePerfisNoBanco;
	
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String ENTIDADE_EM_USO_PROBLEM_TYPE = "Entidade em uso";
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";
	
	@BeforeEach
	public void setUp() {
		// para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/perfilSalaTurma";

		flyway.migrate();
		prepararDados();
	}
	
	/* Testes Metodo POST */
	@Test
	public void cadastroDeSalaTurmaCorreto(){
		PerfilSalaTurmaInput perfil = retornaPerfilSalaTurmaComOsDadosCorretos();
			given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(perfil)
			.when()
				.post()
			.then()
				.body("id", notNullValue())
				.body("nome", equalTo(perfil.getNome()))
				.body("descricao",equalTo(perfil.getDescricao()))
				.statusCode(HttpStatus.CREATED.value());
	}
	
	
	@Test
	public void Deve_Falhar_QuandoCadastrarPerfilDeSalaTurmaComNomeVazio(){
		PerfilSalaTurmaInput perfil = retornaPerfilSalaTurmaComONomeVazio();
			
			given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(perfil)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", hasItems("nome"));
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTurmaComDescricaoVazio() {
		PerfilSalaTurmaInput perfil = retornaPerfilSalaTurmaComADescricaoVazio();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(perfil)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name",hasItems("descricao"));
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTrumaComCamposVazios() {
		PerfilSalaTurmaInput perfil = retornaPerfilSalaTurmaComCamposVazios();
		
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(perfil)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name",hasItems("nome","descricao"));
		
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTurmaComCamposNulos() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComCamposNulos())
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome","descricao"));
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTurmaComCampoNomeDeTamanhoExedido() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComNomeDeTamanhoExedido())
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome"));
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTurmaComCampoDescicaoDeTamanhoExedido() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComDescricaoDeTamanhoExedido())
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("descricao"));
	}
	
	@Test
	public void deve_Falhar_QuandoCadastrarPerfilSalaTurmaComCamposDeTamanhoExedido() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComCamposDeTamanhoExedido())
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome","descricao"));
	}	
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarPerfilSalaTurma_ComNomeJaExistente() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComNomeJaExistente())
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title",equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	/* Testes com o PUT*/
	
	@Test
	public void deveRetornarCodigo200_quandoAtualizar_PerfilSalaTurma_ComOsDadosCorretos() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("Nome Atualizado");
		perfil.setDescricao("Descricao Atualizada");
		
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(perfil)
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome",equalTo("Nome Atualizado"))
			.body("descricao", equalTo("Descricao Atualizada"));
	}
	
	//@Test
	public void deveFalhar_QuandoAtualizarPerfilSalaTurma_ComNomeJaExistente_EmOutroPerfilSalaTurma() {
		PerfilSalaTurma perfilBanco = retornaUmPerfilSalvoNoBanco();
		PerfilSalaTurmaInput perfilInput = new PerfilSalaTurmaInput();
		perfilInput.setNome(perfilBanco.getNome());
		perfilInput.setDescricao("descricao");
		
		given()
			.pathParam("perfilSalaTurmaId",perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(perfilInput)
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title",equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	@Test
	public void deveRetornarCodigo200_QuandoAtualizarDescricaoDe_PerfilSalaTuma_ComNomeJaExistente() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome(perfilTurma.getNome());
		perfil.setDescricao("Descricao teste");
		
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(perfil)
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome",equalTo(perfilTurma.getNome()))
			.body("descricao",equalTo(perfil.getDescricao()));
	}
	
	@Test
	public void deveFalhar_QuandoAtualizar_PerfilSalaTurma_ComCamposVazios() {
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComCamposVazios())
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name",hasItems("nome","descricao"));
		
	}
	
	@Test
	public void deveFalharQuandoAtualizar_PerfilSalaTurma_ComCamposNulos(){
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComCamposNulos())
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name",hasItems("nome","descricao"));
	}

	@Test
	public void deveFalhar_QuandoAtualizarPerfilSalaTurmaComTamanhosExcedidos() {
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(retornaPerfilSalaTurmaComCamposDeTamanhoExedido())
		.when()
			.put("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name",hasItems("nome","descricao"));
	}
	
	/* Testes Com o Get */
	
	@Test
	public void deveRetornarQuantidadeCorretaDe_PerfisSalaTurma_Cadastrados() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("content",hasSize(quantidadeDePerfisNoBanco));
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretosQuandoConsutar_PerfilSalaTurmaExistente() {
		given()
			.pathParam("perfilSalaTurmaId",perfilTurma.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome",equalTo(perfilTurma.getNome()));
	}
	
	@Test
	public void deveRetornar404_QuandoConsutar_PerfilSalaTurmaInexistente() {
		int id = 20;
		given()
			.pathParam("perfilSalaTurmaId", id)
			.accept(ContentType.JSON)
		.when()
			.get("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	/* Testes com o DELETE */
	
	@Test
	public void deveRetornar204_QuandoExcluirPerfilSalaTurma_ComSucesso() {
		given()
			.pathParam("perfilSalaTurmaId", perfilTurma.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	public void deveFalhar_quandoExcluirPerfilInexstente() {
		int id = 20;
		given()
			.pathParam("perfilSalaTurmaId",id)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("title", equalTo(RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE));
	}
	
	public void prepararDados(){
		PerfilSalaTurma perfil1 = new PerfilSalaTurma();
		perfil1.setNome("Sala 1");
		perfil1.setDescricao("Sala localizada na ala leste do Setor IV");
		perfilSalaTurmaRepository.save(perfil1);
		
		PerfilSalaTurma perfil2 = new PerfilSalaTurma();
		perfil2.setNome("Sala 2");
		perfil2.setDescricao("Sala localizada na ala oeste do Setor I");
		perfilSalaTurmaRepository.save(perfil2);
		
		perfilTurma = new PerfilSalaTurma();
		perfilTurma.setNome("Sala 50");
		perfilTurma.setDescricao("Descricao teste ");
		perfilSalaTurmaRepository.save(perfilTurma);
		
		quantidadeDePerfisNoBanco = (int) perfilSalaTurmaRepository.count();
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComOsDadosCorretos(){
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("Sala Teste");
		perfil.setDescricao("Descricao teste");
		
		return perfil;
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComONomeVazio() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("");
		perfil.setDescricao("Descricao teste");
		
		return perfil;
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComADescricaoVazio() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("Sala teste");
		perfil.setDescricao("");
		
		return perfil;
	}
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComCamposVazios() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("");
		perfil.setDescricao("");
		
		return perfil;
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComNomeDeTamanhoExedido() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa");
		perfil.setDescricao("Teste Descricao");
		
		return perfil;
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComDescricaoDeTamanhoExedido() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("Teste Nome");
		perfil.setDescricao("aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa ");
		
		return perfil;
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComCamposDeTamanhoExedido() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa");
		perfil.setDescricao("aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaaaaaa aaaaaa aaaaaaa aaaaaaaa aaaaaa aaaa");
		
		return perfil;
	}
	
	
	private String retornaPerfilSalaTurmaComCamposNulos() {
		return "{\n" + "\n}";
	}
	
	private PerfilSalaTurmaInput retornaPerfilSalaTurmaComNomeJaExistente() {
		PerfilSalaTurmaInput perfil = new PerfilSalaTurmaInput();
		perfil.setNome("Sala 1");
		perfil.setDescricao("descricao test");
		
		return perfil;
	}
	
	private PerfilSalaTurma retornaUmPerfilSalvoNoBanco() {
		PerfilSalaTurma perfil = new PerfilSalaTurma();
		perfil.setNome("Sala 100");
		perfil.setDescricao("Sala de aula");
		
		perfilSalaTurmaRepository.save(perfil);
		
		return perfil;
	}
	
}
