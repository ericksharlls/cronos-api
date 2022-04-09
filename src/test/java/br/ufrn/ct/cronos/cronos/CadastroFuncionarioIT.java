package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;

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

import br.ufrn.ct.cronos.api.model.input.FuncionarioInput;
import br.ufrn.ct.cronos.api.model.input.TipoFuncionarioIdInput;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;
import br.ufrn.ct.cronos.domain.repository.TipoFuncionarioRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//@SpringBootTest //fornece as funcionalidades do Spring Boot nos testes
@ExtendWith(SpringExtension.class) // faz com q o contexto do Spring seja levantado no momento da execução dos
									// testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroFuncionarioIT {

	@Autowired
	private Flyway flyway;

	@LocalServerPort
	private int port;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private TipoFuncionarioRepository tipoFuncionarioRepository;

	private Funcionario funcionarioDomainObject;
	private FuncionarioInput funcionarioInput;
	private TipoFuncionario tipoFuncionarioDomainObject;
	private int contadorDeFuncionariosSalvos;

	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int FUNCIONARIO_ID_INEXISTENTE = 81;
	private static final int TIPO_FUNCIONARIO_ID_INEXISTENTE = 100;

	@BeforeEach
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/funcionarios";

		flyway.migrate();

		init();
	}

	private void init() {
		criaTipoFuncionarioNoBanco();

		funcionarioDomainObject = new Funcionario();

		funcionarioDomainObject.setNome("Joaquim dos Santos Araujo");
		funcionarioDomainObject.setCpf("30461317044");
		funcionarioDomainObject.setMatricula("N3042022");
		funcionarioDomainObject.setEmail("joaquim.santos@gmail.com");
		funcionarioDomainObject.setTelefone("32132465");
		funcionarioDomainObject.setRamal("1");
		funcionarioDomainObject.setIdSigaa(12556L);
		funcionarioDomainObject.setTipoFuncionario(tipoFuncionarioDomainObject);

		funcionarioRepository.save(funcionarioDomainObject);
	}

	private void criaTipoFuncionarioNoBanco() {
		tipoFuncionarioDomainObject = new TipoFuncionario();

		tipoFuncionarioDomainObject.setDescricao("Presta suporte de TI");
		tipoFuncionarioDomainObject.setNome("Suporte");

		tipoFuncionarioRepository.save(tipoFuncionarioDomainObject);
	}

	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	@Test
	public void deveAtribuirId_QuandoCadastrarFuncionarioComDadosCorretos() {
		settaDadosCorretosEmFuncionarioInput();

		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(funcionarioInput).when().post().then()
				.body("id", notNullValue()).body("nome", equalTo(funcionarioInput.getNome()))
				.body("matricula", equalTo(funcionarioInput.getMatricula()))
				.body("cpf", equalTo(funcionarioInput.getCpf())).body("email", equalTo(funcionarioInput.getEmail()))
				.body("telefone", equalTo(funcionarioInput.getTelefone()))
				.body("ramal", equalTo(funcionarioInput.getRamal()))
				.body("idSigaa", equalTo(funcionarioInput.getIdSigaa())).body("tipoFuncionario", notNullValue())
				.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioSemTipoFuncionario() {
		settaDadosEmFuncionarioInputSemTipoFuncionario();

		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(funcionarioInput).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioSemMatriculaECPF() {
		settaDadosEmFuncionarioInputSemMatriculaECPF();

		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(funcionarioInput).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioComCamposNulos() {
		funcionarioInput = new FuncionarioInput();

		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(funcionarioInput).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", hasItems("nome", "idSigaa", "tipoFuncionario"));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioComCamposVazios() {
		settaFuncionarioInputComCamposVazios();

		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(funcionarioInput).when().post().then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
				.body("validations.name", hasItems("nome"));
	}

	/**** TESTES COM REQUISIÇÃ0 TIPO PUT ****/
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFuncionarioComDadosCorretos() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		novoFuncionarioDomainObject.setNome("Daniel dos Santos Silva");
		
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);
		
		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.OK.value());       
	}
	
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFuncionarioSemTipoFuncionario() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		novoFuncionarioDomainObject.setTipoFuncionario(null);
		
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);
		
		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));    
	}
	
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFuncionarioSemMatriculaECPF() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		novoFuncionarioDomainObject.setCpf(null);
		novoFuncionarioDomainObject.setMatricula(null);
		
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);
		
		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));    
	}
	
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFuncionarioComCamposNulos() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		novoFuncionarioDomainObject.setNome(null);
		novoFuncionarioDomainObject.setIdSigaa(null);
		novoFuncionarioDomainObject.setTipoFuncionario(null);
		
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);
		
		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", "idSigaa", "tipoFuncionario"));    
	}
	
	@Test
	public void deveRetornarSucesso_QuandoAtualizarFuncionarioComNomeVazio() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		novoFuncionarioDomainObject.setNome("");
		
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);
		
		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome"));    
	}
	/**** TESTES COM REQUISIÇÃ0 TIPO GET ****/
	@Test
	public void deveRetornarSucesso_QuandoBuscarFuncionarioPorIdExistente() {
		given()
			.pathParam("idFuncionario", funcionarioDomainObject.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(funcionarioDomainObject.getNome()));
	}
	
	@Test
	public void deveRetornarSucesso_QuandoBuscarFuncionarioPorIdInexistente() {
		given()
			.pathParam("idFuncionario", FUNCIONARIO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
//	@Test
//	public void deveRetornarSucesso_QuandoBuscarFuncionarioPorTipoENome() {
//		given()
//			.accept(ContentType.JSON)
//		.when()
//			.get("/funcionarios")
//		.then()
//			.statusCode(HttpStatus.OK.value())
//			.body("nome", equalTo(funcionarioDomainObject.getNome()));
//	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO DELETE ****/
	@Test
	public void deveRetornarSucesso_QuandoExcluirFuncionarioComSucesso() {
		given()
			.pathParam("idFuncionario", funcionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	public void deveRetornarErro_QuandoDeletarFuncionarioInexistente() {
		given()
			.pathParam("idFuncionario", FUNCIONARIO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.delete("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	
	private void settaDadosCorretosEmFuncionarioInput() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(tipoFuncionarioDomainObject.getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("Jorge da Silva de Medeiros");
		funcionarioInput.setCpf("80868469033");
		funcionarioInput.setMatricula("N8082021");
		funcionarioInput.setEmail("jorge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
		funcionarioInput.setIdSigaa(15256L);
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private void settaDadosEmFuncionarioInputSemTipoFuncionario() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(tipoFuncionarioDomainObject.getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("Jorge da Silva de Medeiros");
		funcionarioInput.setCpf("80868469033");
		funcionarioInput.setMatricula("N8082021");
		funcionarioInput.setEmail("joprge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
		funcionarioInput.setIdSigaa(15256L);
	}

	private void settaDadosEmFuncionarioInputSemMatriculaECPF() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(tipoFuncionarioDomainObject.getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("Jorge da Silva de Medeiros");
		funcionarioInput.setEmail("joprge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
		funcionarioInput.setIdSigaa(15256L);
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private void settaFuncionarioInputComCamposVazios() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(tipoFuncionarioDomainObject.getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("");
		funcionarioInput.setCpf("");
		funcionarioInput.setMatricula("");
		funcionarioInput.setEmail("");
		funcionarioInput.setTelefone("");
		funcionarioInput.setRamal("");
		funcionarioInput.setIdSigaa(15256L);
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private Funcionario criaNovoFuncionarioObjectDomain() {
		Funcionario novoFuncionarioDomainObject = new Funcionario();

		novoFuncionarioDomainObject.setNome("Daniel dos Santos Araujo");
		novoFuncionarioDomainObject.setCpf("49508493011");
		novoFuncionarioDomainObject.setMatricula("N4952022");
		novoFuncionarioDomainObject.setEmail("daniel.santos@gmail.com");
		novoFuncionarioDomainObject.setTelefone("32136465");
		novoFuncionarioDomainObject.setRamal("1");
		novoFuncionarioDomainObject.setIdSigaa(12558L);
		novoFuncionarioDomainObject.setTipoFuncionario(tipoFuncionarioDomainObject);

		funcionarioRepository.save(novoFuncionarioDomainObject);

		return novoFuncionarioDomainObject;
	}
	private void settaFuncionarioInputComDadosAtualizados(Funcionario funcionarioSalvo) {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(funcionarioSalvo.getTipoFuncionario().getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome(funcionarioSalvo.getNome());
		funcionarioInput.setCpf(funcionarioSalvo.getCpf());
		funcionarioInput.setMatricula(funcionarioSalvo.getMatricula());
		funcionarioInput.setEmail(funcionarioSalvo.getEmail());
		funcionarioInput.setTelefone(funcionarioSalvo.getTelefone());
		funcionarioInput.setRamal(funcionarioSalvo.getRamal());
		funcionarioInput.setIdSigaa(funcionarioSalvo.getIdSigaa());
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
		
	}
}
