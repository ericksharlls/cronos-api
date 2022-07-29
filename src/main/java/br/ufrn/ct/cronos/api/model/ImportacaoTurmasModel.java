package br.ufrn.ct.cronos.api.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportacaoTurmasModel {
    
    private Long id;
    
    private LocalDateTime horarioUltimaOperacao;

	private DepartamentoModel departamento;

	private StatusImportacaoTurmasModel status;

    private List<HistoricoImportacaoTurmasModel> listaHistorico;

}
