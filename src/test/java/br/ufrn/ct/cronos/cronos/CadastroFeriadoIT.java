package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
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
		
		feriadoDomainObject.setDescricao("Carnaval");
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
		periodoDomainObject.setPeriodo(valorPeriodoTeste);
		
		periodoRepository.save(periodoDomainObject);
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
	private void deveRetornarErro_QuandoJaHouverFeriadoComADataInformada() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoHouverCamposNulos() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoHouverCamposVazios() {
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO PUT ****/
	@Test
	private void deveAtribuirId_QuandoAtualizarFeriadoComDadosCorretos() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoAtualizarFeriadoComDataExistente() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoAtualizarComCamposNulos() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoAtualizarComCamposVazios() {
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO GET ****/
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriados() {
		
	}
	
	@Test
	private void deveRetornarSucesso_QuandoBuscarFeriadoPorId() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoBuscarFeriadoInexistente() {
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO DELETE ****/
	
	@Test
	private void deveRetornarSucesso_QuandoExcluirFeriadoComDadosCorretos() {
		
	}
	
	@Test
	private void deveRetornarErro_QuandoDeletarFeriadoInexistente() {
		
	}
	
	private void settaDadosCorretosEmFeriadoInput() {
		PeriodoIdInput periodoIdInput = new PeriodoIdInput();
		
		periodoIdInput.setId(periodoDomainObject.getId());
		
		feriadoInput = new FeriadoInput();
		
		feriadoInput.setDescricao("Feriado de Carnaval");
		feriadoInput.setData(LocalDate.of(2022, 2, 28));
		feriadoInput.setPeriodo(periodoIdInput);
	}
}
