package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
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
@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
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
	public void setup () {
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
		.body("idSigaa", equalTo(funcionarioInput.getIdSigaa()))
		.body("tipoFuncionario", notNullValue())
		.statusCode(HttpStatus.CREATED.value());
	}

	private void settaDadosCorretosEmFuncionarioInput() {
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
		funcionarioInput.setTipoFuncionario(tipoFuncionarioIdInput);
		
	}
}
