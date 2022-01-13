package br.ufrn.ct.cronos.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerilSalaTurmaIdInput {

    @NotNull
	private Long id;
    
}
