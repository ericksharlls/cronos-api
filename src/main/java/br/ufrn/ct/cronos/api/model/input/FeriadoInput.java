package br.ufrn.ct.cronos.api.model.input;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeriadoInput {

	@NotBlankAndSizeForString(max = 50)
	private String descricao;
	
	@NotNull
	private LocalDate data;
	
	@Valid
	@NotNull
	private PeriodoIdInput periodo; 
}
