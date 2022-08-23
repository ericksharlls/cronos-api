package br.ufrn.ct.cronos.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.model.input.distribuicaoturmas.DistribuicaoTurmasInput;
import br.ufrn.ct.cronos.domain.service.distribuicaoturmas.DistribuicaoTurmasService;

@EnableAsync
@RestController
@RequestMapping(value = "/distribuicao/turmas", produces = MediaType.APPLICATION_JSON_VALUE)
public class DistribuicaoTurmasController {

    private DistribuicaoTurmasService distribuicaoTurmasService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public void importarTurmas(@RequestBody @Valid DistribuicaoTurmasInput distribuicaoTurmasInput) {
        distribuicaoTurmasService.executarDistribuicaoAssincronamente(distribuicaoTurmasInput.getIdPeriodo(), distribuicaoTurmasInput.getIdPredio());
        System.out.println("#### Fim da execução de DistribuicaoTurmasController ####");
    }
    
}
