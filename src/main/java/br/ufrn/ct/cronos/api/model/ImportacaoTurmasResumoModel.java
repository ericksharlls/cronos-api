package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ImportacaoTurmasResumoModel {

    private Long id;
    
    private LocalDateTime horarioUltimaOperacao;

	private DepartamentoModel departamento;

	private StatusImportacaoTurmasModel status;
    
}
