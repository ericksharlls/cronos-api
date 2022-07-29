package br.ufrn.ct.cronos.api.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportacaoTurmasResumoModel {

    private Long id;
    
    private LocalDateTime horarioUltimaOperacao;

	private DepartamentoModel departamento;

	private StatusImportacaoTurmasModel status;
    
}
