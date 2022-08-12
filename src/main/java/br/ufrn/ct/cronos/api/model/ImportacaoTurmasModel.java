package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ImportacaoTurmasModel {
    
    private Long id;
    
    private LocalDateTime horarioUltimaOperacao;

	private DepartamentoModel departamento;

	private StatusImportacaoTurmasModel status;

    private List<HistoricoImportacaoTurmasModel> listaHistorico;

}
