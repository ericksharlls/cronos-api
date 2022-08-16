package br.ufrn.ct.cronos.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class PerilSalaTurmaIdInput {

    @NotNull
	private Long id;
    
}
