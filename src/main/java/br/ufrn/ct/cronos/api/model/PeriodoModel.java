package br.ufrn.ct.cronos.api.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PeriodoModel {

    private Long id;

	private String nome;
	private String descricao;
	
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	
	private Boolean isPeriodoLetivo;
	
	private Short ano;
	private Short periodo;
    
}
