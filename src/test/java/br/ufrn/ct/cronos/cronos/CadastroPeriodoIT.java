package br.ufrn.ct.cronos.cronos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

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
	
	Short anoPeriodoTeste;  
	Short valorPeriodoTeste;
	
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
	}
	
	// Testes tipo POST
	@Test
	public void deveAtribuirIdAoCadastrarPeriodoComDadosCorretos() {
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
			.body("dataInicio", equalTo(periodoInput.getDataInicio()))
			.body("dataTermino", equalTo(periodoInput.getDataTermino()))
			.body("isPeriodoLetivo", equalTo(periodoInput.getIsPeriodoLetivo()))
			.body("ano", equalTo(periodoInput.getAno()))
			.statusCode(HttpStatus.CREATED.value());
		
	}
	
	public void deveRetornarErroParaPeriodoComNomeJaExistente () {
		retornaPeriodoComNomeJaExistente();
	}
	
	public void deveRetornarErroParaPeriodoEmIntervaloJaExistente() {
		
	}
	
	public void deveRetornarErroParaPeriodoSemNome() {
		
	}
	
	public void deveRetornarErroParaPeriodoSemDataInicio() {
		
	}
	
	public void deveRetornarErroParaPeriodoSemDataTermino() {
		
	}
	
	
	private PeriodoInput retornaPeriodoComDadosCorretos() {
		periodoInput.setNome("teste 03");
		periodoInput.setDescricao("objeto de teste");
		periodoInput.setDataInicio(LocalDate.of(2022, 7, 19));
		periodoInput.setDataTermino(LocalDate.of(2022, 9, 19));
		periodoInput.setIsPeriodoLetivo(true);
		periodoInput.setAno(anoPeriodoTeste);
		periodoInput.setIsPeriodoLetivo(true);
		periodoInput.setPeriodo(++valorPeriodoTeste);
		
		return periodoInput;
	}
	
	private PeriodoInput retornaPeriodoComNomeJaExistente() {
		retornaPeriodoComDadosCorretos();
		
		periodoInput.setNome("teste 02");
		
		return periodoInput;
	}
}
