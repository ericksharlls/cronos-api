package br.ufrn.ct.cronos.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/*Em um feriado a informação mais relevante de um periodo, que deve ser recebida do usuário é o id. Essa classe é usada 
 * para receber o id de um determinado periodo de um feriado qualquer.*/

@Setter
@Getter
public class PeriodoIdInput {
	
	@NotNull
	private Long id;
}
