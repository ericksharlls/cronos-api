package br.ufrn.ct.cronos.integrationtests;

import br.ufrn.ct.cronos.api.model.input.FuncionarioInput;
import br.ufrn.ct.cronos.api.model.input.TipoFuncionarioIdInput;
import br.ufrn.ct.cronos.domain.model.Funcionario;
import br.ufrn.ct.cronos.domain.model.TipoFuncionario;
import br.ufrn.ct.cronos.domain.repository.FuncionarioRepository;
import br.ufrn.ct.cronos.domain.repository.TipoFuncionarioRepository;
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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasItem;

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
	
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int FUNCIONARIO_ID_INEXISTENTE = 1281;
	private static final Long TIPO_FUNCIONARIO_ID_INEXISTENTE = 100L;
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";

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
		funcionarioDomainObject.setIdSigaaFuncionario(12556L);
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

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.post()
		.then()
			.body("id", notNullValue())
			.body("nome", equalTo(funcionarioInput.getNome()))
			.body("matricula", equalTo(funcionarioInput.getMatricula()))
			.body("cpf", equalTo(funcionarioInput.getCpf()))
			.body("email", equalTo(funcionarioInput.getEmail()))
			.body("telefone", equalTo(funcionarioInput.getTelefone()))
			.body("ramal", equalTo(funcionarioInput.getRamal()))
			.body("tipoFuncionario", notNullValue())
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioComTipoFuncionarioInexistente() {
		settaDadosEmFuncionarioInputTipoFuncionarioInexistente();

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioSemMatriculaECPF() {
		settaDadosEmFuncionarioInputSemMatriculaECPF();

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioComCamposNulos() {
		funcionarioInput = new FuncionarioInput();

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
			.body("validations.name", hasItems("nome", "tipoFuncionario"));
	}

	@Test
	public void deveFalhar_QuandoCadastrarFuncionarioComCamposVazios() {
		settaFuncionarioInputComCamposVazios();

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE))
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
	public void deveRetornarErro_QuandoAtualizarFuncionarioComTipoFuncionarioInexistente() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		settaDadosEmFuncionarioInputTipoFuncionarioInexistente();

		given()
			.pathParam("idFuncionario", novoFuncionarioDomainObject.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("/{idFuncionario}")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));    
	}
	
	@Test
	public void deveRetornarErro_QuandoAtualizarFuncionarioSemMatriculaECPF() {
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
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));    
	}
	
	@Test
	public void deveRetornarErro_QuandoAtualizarFuncionarioComCamposNulos() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		
		funcionarioInput = new FuncionarioInput();

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
			.body("validations.name", hasItems("nome", "tipoFuncionario"));    
	}
	
	@Test
	public void deveRetornarErro_QuandoAtualizarFuncionarioComNomeVazio() {
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
	
	@Test
	public void deveRetornarStatus404_QuandoAtualizarFuncionarioInexistente(){
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		settaFuncionarioInputComDadosAtualizados(novoFuncionarioDomainObject);

		given()
			.pathParam("idFuncionario", FUNCIONARIO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(funcionarioInput)
		.when()
			.put("{idFuncionario}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("title",equalTo(RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE));
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
	
	@Test
	public void deveRetornarSucesso_QuandoBuscarFuncionarioPorTipoENome() {
		Funcionario novoFuncionarioDomainObject = criaNovoFuncionarioObjectDomain();
		given()
			.param("tipoFuncionarioId", novoFuncionarioDomainObject.getTipoFuncionario().getId())
			.param("nome", novoFuncionarioDomainObject.getNome())
			.accept(ContentType.JSON)
		.when()
			.get("")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("content.nome", hasItem(equalTo( novoFuncionarioDomainObject.getNome())))
			.body("content.tipoFuncionario.id", hasItem(novoFuncionarioDomainObject.getTipoFuncionario().getId().intValue()));
	}
	
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
		funcionarioInput.setMatricula("28082021");
		funcionarioInput.setEmail("jorge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private void settaDadosEmFuncionarioInputTipoFuncionarioInexistente() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(TIPO_FUNCIONARIO_ID_INEXISTENTE);

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("Jorge da Silva de Medeiros");
		funcionarioInput.setCpf("80868469033");
		funcionarioInput.setMatricula("N8082021");
		funcionarioInput.setEmail("joprge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
	}

	private void settaDadosEmFuncionarioInputSemMatriculaECPF() {
		TipoFuncionarioIdInput tipoFuncionarioIdInput = new TipoFuncionarioIdInput();

		tipoFuncionarioIdInput.setId(tipoFuncionarioDomainObject.getId());

		funcionarioInput = new FuncionarioInput();

		funcionarioInput.setNome("Jorge da Silva de Medeiros");
		funcionarioInput.setEmail("joprge.silva@gmail.com");
		funcionarioInput.setTelefone("32136524");
		funcionarioInput.setRamal("2");
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
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private Funcionario criaNovoFuncionarioObjectDomain() {
		Funcionario novoFuncionarioDomainObject = new Funcionario();

		novoFuncionarioDomainObject.setNome("Daniel dos Santos Araujo");
		novoFuncionarioDomainObject.setCpf("77253830008");
		novoFuncionarioDomainObject.setMatricula("20218119");
		novoFuncionarioDomainObject.setEmail("daniel.santos@gmail.com");
		novoFuncionarioDomainObject.setTelefone("32136465");
		novoFuncionarioDomainObject.setRamal("1");
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
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
	}

	private void settaFuncionarioInputSemTipoFuncionario(Funcionario funcionarioSalvo){
		
		funcionarioInput = new FuncionarioInput();
		
		funcionarioInput.setNome(funcionarioSalvo.getNome());
		funcionarioInput.setCpf(funcionarioSalvo.getCpf());
		funcionarioInput.setMatricula(funcionarioSalvo.getMatricula());
		funcionarioInput.setEmail(funcionarioSalvo.getEmail());
		funcionarioInput.setTelefone(funcionarioSalvo.getTelefone());
		funcionarioInput.setRamal(funcionarioSalvo.getRamal());
	}
}
