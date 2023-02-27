package br.ufrn.ct.cronos.api.controller.openapi;

import br.ufrn.ct.cronos.api.model.ImportacaoTurmasModel;
import br.ufrn.ct.cronos.api.model.ImportacaoTurmasResumoModel;
import br.ufrn.ct.cronos.api.model.input.ImportacaoTurmasInput;
import br.ufrn.ct.cronos.api.model.input.ReexecucaoImportacaoTurmasInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Importação de Turmas", description = "Importa as Turmas da API dos Sistemas da UFRN")
public interface ImportacaoTurmasControllerOpenApi {

    @Operation(summary = "Cadastra Importações de Turmas", description = "Cadastra uma ou mais Importações de Turmas, sendo necessário informar um Prédio e um Período padrão para as turmas, bem como as Unidades Acadêmicas que devem ter suas Turmas importadas, de acordo com um ou mais níveis de ensino.")
    public void importarTurmas(ImportacaoTurmasInput importacaoTurmasInput);
    
    @Operation(summary = "Lista as Importações de Turmas", description = "Lista as importações com suas principais informações, como o status de sua execução, além do Prédio e Departamento aos quais está ligada.")
    public Page<ImportacaoTurmasResumoModel> findAll(Pageable pageable);
    
    @Operation(summary = "Exibe as informações de uma Importação", description = "Exibe todas as informações de uma importação, de acordo com o id parametrizado.")
    public ImportacaoTurmasModel buscar(Long idImportacaoTurmas);

    @Operation(summary = "Reexecuta uma Importação de Turmas", description = "Reexecuta uma Importação de Turmas de acordo com o id parametrizado da Importação específica.")
    public void reexecutarImportacao(Long idImportacaoTurmas, ReexecucaoImportacaoTurmasInput reexecucaoImportacaoTurmasInput);
}
