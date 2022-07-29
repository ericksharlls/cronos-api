package br.ufrn.ct.cronos.api.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.assembler.ImportacaoTurmasModelAssembler;
import br.ufrn.ct.cronos.api.assembler.ImportacaoTurmasResumoModelAssembler;
import br.ufrn.ct.cronos.api.model.ImportacaoTurmasModel;
import br.ufrn.ct.cronos.api.model.ImportacaoTurmasResumoModel;
import br.ufrn.ct.cronos.api.model.input.ImportacaoTurmasInput;
import br.ufrn.ct.cronos.domain.model.ImportacaoTurmas;
import br.ufrn.ct.cronos.domain.repository.ImportacaoTurmasRepository;
import br.ufrn.ct.cronos.domain.service.ImportarTurmasService;

@EnableAsync
@RestController
@RequestMapping(value = "/api-ufrn/turmas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImportacaoTurmasController {

    @Autowired
    private ImportarTurmasService importarTurmasService;

    @Autowired
    private ImportacaoTurmasRepository importacaoTurmasRepository;

    @Autowired
    private ImportacaoTurmasResumoModelAssembler importacaoTurmasResumoModelAssembler;

    @Autowired
    private ImportacaoTurmasModelAssembler importacaoTurmasModelAssembler;

    @PostMapping("/importacao")
    @ResponseStatus(HttpStatus.CREATED)
	public void importarTurmas(@RequestBody @Valid ImportacaoTurmasInput importacaoTurmasInput) {
        importarTurmasService.agendarImportacoes(importacaoTurmasInput.getSiglasNivelEnsino(), importacaoTurmasInput.getIdsUnidades(), importacaoTurmasInput.getIdPeriodo());
        importarTurmasService.executarAssincronamenteImportacoes();
        System.out.println("#### Fim da execução de ImportacaoTurmasController ####");
	}

    @GetMapping("/importacao")
	public Page<ImportacaoTurmasResumoModel> findAll(@PageableDefault(size = 5) Pageable pageable){
		Page<ImportacaoTurmas> importacoesPage = importacaoTurmasRepository.findAll(pageable);
	
		Page<ImportacaoTurmasResumoModel> importacoesModelPage = new PageImpl<>(
            importacaoTurmasResumoModelAssembler.toCollectionModel(importacoesPage.getContent()),
				pageable,
				importacoesPage.getTotalElements()
				);
		
		return importacoesModelPage;
	}

    @GetMapping("/importacao/{idImportacaoTurmas}")
    public ImportacaoTurmasModel buscar(@PathVariable Long idImportacaoTurmas) {
        ImportacaoTurmas importacao = importarTurmasService.buscar(idImportacaoTurmas);
        
        return importacaoTurmasModelAssembler.toModel(importacao);
    }

    @PutMapping("/importacao/{idImportacaoTurmas}")
    public ImportacaoTurmasModel executarImportacao(@PathVariable Long idImportacaoTurmas) {
        ImportacaoTurmas importacao = importarTurmasService.buscar(idImportacaoTurmas);
        
        return importacaoTurmasModelAssembler.toModel(importacao);
    }
    
}
