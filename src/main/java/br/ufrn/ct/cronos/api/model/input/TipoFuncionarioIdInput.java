package br.ufrn.ct.cronos.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TipoFuncionarioIdInput {
	
	@NotNull
	private Long id;
}
