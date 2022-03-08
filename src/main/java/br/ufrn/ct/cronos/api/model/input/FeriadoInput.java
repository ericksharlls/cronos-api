package br.ufrn.ct.cronos.api.model.input;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import br.ufrn.ct.cronos.domain.model.Periodo;

public class FeriadoInput {

	@NotNull
	private LocalDate data;
	
	@NotBlankAndSizeForString(max = 30)
	private String nome;
	
	private Periodo periodo; // é um PeriodoInput ou só Periodo
}
