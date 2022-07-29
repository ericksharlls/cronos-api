package br.ufrn.ct.cronos.api.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoricoImportacaoTurmasModel {

    private Long id;

    private LocalDateTime registradoEm;
    
	private StatusImportacaoTurmasModel status;
    
}
