package br.ufrn.ct.cronos.api.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeriadoModel {
	private Long id;
	
	private String descricao;
	
	private LocalDate data;
	
	private PeriodoResumoModel periodo;
	
}
