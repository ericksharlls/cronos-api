package br.ufrn.ct.cronos.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.assembler.UnidadeAPIUFRNAssembler;
import br.ufrn.ct.cronos.api.model.UnidadeAPIUFRNModel;
import br.ufrn.ct.cronos.domain.filter.ConsultaUnidadeAPIUFRNFilter;
import br.ufrn.ct.cronos.domain.repository.TurmaApiUfrnRepository;

@RestController
@RequestMapping(value = "/api-ufrn/unidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class UnidadeAPIUFRNController {

    @Autowired
    private TurmaApiUfrnRepository turmaApiUfrnRepository;

    @Autowired
    private UnidadeAPIUFRNAssembler unidadeAPIUFRNAssembler;

    @GetMapping
	public List<UnidadeAPIUFRNModel> importarTurmas(@Valid ConsultaUnidadeAPIUFRNFilter consultaUnidadeAPIUFRNFilter) {
        return unidadeAPIUFRNAssembler
                .toCollectionModel(turmaApiUfrnRepository.retornaUnidadesPorNomeCentro(consultaUnidadeAPIUFRNFilter));
    }
    
}
