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
	
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String ENTIDADE_EM_USO_PROBLEM_TYPE = "Entidade em uso";
	private static final String RECURSO_NAO_ENCONTRADO_PROBLEM_TYPE = "Recurso não encontrado";
	
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
	
	/* Testes Metodo POST */
	@Test
	public void cadastroDeSalaTurmaComNomeVazio(){
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
	
	
	
	public void prepararDados(){
		PerfilSalaTurma perfil1 = new PerfilSalaTurma();
		perfil1.setNome("Sala 1");
		perfil1.setDescricao("Sala localizada na ala leste do Setor IV");
		perfilSalaTurmaRepository.save(perfil1);
		
		PerfilSalaTurma perfil2 = new PerfilSalaTurma();
		perfil2.setNome("Sala 2");
		perfil2.setDescricao("Sala localizada na ala oeste do Setor I");
		perfilSalaTurmaRepository.save(perfil2);
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
	
}
