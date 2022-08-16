package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class HistoricoImportacaoTurmasModel {

    private Long id;

    private LocalDateTime registradoEm;
    
	private StatusImportacaoTurmasModel status;
    
}
