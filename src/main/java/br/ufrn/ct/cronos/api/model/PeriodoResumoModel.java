package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PeriodoResumoModel {

    private Long id;

	private String nome;
	
	private Boolean isPeriodoLetivo;
    
}
