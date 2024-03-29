package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.ImportacaoTurmasModelAssembler;
import br.ufrn.ct.cronos.api.assembler.ImportacaoTurmasResumoModelAssembler;
import br.ufrn.ct.cronos.api.controller.openapi.ImportacaoTurmasControllerOpenApi;
import br.ufrn.ct.cronos.api.model.ImportacaoTurmasModel;
import br.ufrn.ct.cronos.api.model.ImportacaoTurmasResumoModel;
import br.ufrn.ct.cronos.api.model.input.ImportacaoTurmasInput;
import br.ufrn.ct.cronos.api.model.input.ReexecucaoImportacaoTurmasInput;
import br.ufrn.ct.cronos.domain.model.ImportacaoTurmas;
import br.ufrn.ct.cronos.domain.repository.ImportacaoTurmasRepository;
import br.ufrn.ct.cronos.domain.service.ImportarTurmasService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@EnableAsync
@RestController
@RequestMapping(value = "/api-ufrn/turmas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImportacaoTurmasController implements ImportacaoTurmasControllerOpenApi {

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
        importarTurmasService
                    .agendarImportacoes(importacaoTurmasInput.getSiglasNivelEnsino(), importacaoTurmasInput.getIdsUnidades(), 
                        importacaoTurmasInput.getIdPeriodo(),importacaoTurmasInput.getIdPredioPadrao());
        importarTurmasService.executarAssincronamenteImportacoes();
        log.info("#### Fim da execução do Controller. Agora a Camada de Serviço atuará de forma assíncrona na importação.");
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
    public void reexecutarImportacao(@PathVariable Long idImportacaoTurmas, 
                        @RequestBody @Valid ReexecucaoImportacaoTurmasInput reexecucaoImportacaoTurmasInput) {
        importarTurmasService
                    .agendarImportacaoPorId(idImportacaoTurmas, reexecucaoImportacaoTurmasInput.getSiglasNivelEnsino(), 
                                    reexecucaoImportacaoTurmasInput.getIdPeriodo());
        importarTurmasService.reexecutarAssincronamenteImportacao(idImportacaoTurmas);
    }
    
}
