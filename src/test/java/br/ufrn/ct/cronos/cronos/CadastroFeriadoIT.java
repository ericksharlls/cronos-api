package br.ufrn.ct.cronos.cronos;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;
import io.restassured.RestAssured;

//@SpringBootTest //fornece as funcionalidades do Spring Boot nos testes
@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroFeriadoIT {
	@Autowired
	private Flyway flyway;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	private Feriado testeFeriado;
	
	@BeforeEach
	public void setup () {
		// para fazer o log do q foi enviado na requisição e recebido na resposta quando o teste falha
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/feriados";

		flyway.migrate();
		
		init();
	}
	private void clear() {
		
	}

	private void init() {
	
		
	}
	
	/**** TESTES COM REQUISIÇÃ0 TIPO POST ****/
	@Test
	private void deveAtribuirId_QuandoCadastrarFeriadoComDadosCorretos() {
		
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
	
}
