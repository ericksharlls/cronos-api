package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FeriadoModel {
	private Long id;
	
	private String descricao;
	
	private LocalDate data;
	
	private PeriodoResumoModel periodo;
	
}
