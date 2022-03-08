package br.ufrn.ct.cronos.api.model;

import java.time.LocalDate;

import br.ufrn.ct.cronos.domain.model.Periodo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeriadoModel {
	private Long id;
	
	private LocalDate data;
	
	private String nome;
	
	private Periodo periodo;
}
