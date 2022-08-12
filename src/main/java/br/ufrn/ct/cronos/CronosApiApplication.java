package br.ufrn.ct.cronos;

import br.ufrn.ct.cronos.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class CronosApiApplication {

	public static void main(String[] args) {
		// Para nao depender do TimeZone do SO, configuro ele na própria aplicação
		// Configuração do TimeZone na própria aplicação, para não depender do TimeZone do SO
		TimeZone.setDefault(TimeZone.getTimeZone("America/Fortaleza"));
		SpringApplication.run(CronosApiApplication.class, args);
	}

}
