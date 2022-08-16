package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
	private Short numero;
    
}
