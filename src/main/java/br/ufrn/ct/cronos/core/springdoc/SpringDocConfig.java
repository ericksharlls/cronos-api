package br.ufrn.ct.cronos.core.springdoc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cronos API")
                        .version("v1")
                        .description("API REST do sistema Cronos: um sistema para gerenciamento de alocações de turmas e agendamentos diversos nos espaços físicos (salas de aula, laboratórios, dentre outros) do Setor de Aulas IV da UFRN.")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                ).externalDocs(new ExternalDocumentation()
                        .description("Cronos")
                        .url("https://cronos.ct.ufrn.br")
                );
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(operation -> {
                        ApiResponses responses = operation.getResponses();

                        ApiResponse apiResponseNaoEncontrado = new ApiResponse().description("Recurso não encontrado");
                        ApiResponse apiResponseErroInterno = new ApiResponse().description("Erro interno no servidor");
                        ApiResponse apiResponseSemRepresentacao = new ApiResponse()
                                .description("Recurso não possui uma representação que poderia ser aceita pelo consumidor");

                        responses.addApiResponse("404", apiResponseNaoEncontrado);
                        responses.addApiResponse("406", apiResponseSemRepresentacao);
                        responses.addApiResponse("500", apiResponseErroInterno);
                    });
        };
    }

}
