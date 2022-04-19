package br.ufrn.ct.cronos.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioInput {
	
	@NotBlankAndSizeForString(max = 50)
	private String nome;
	
	private String matricula;
	
	private String cpf;
	
	private String email;
	
	private String telefone;
	
	private String ramal;
	
	private Long idSigaa;
	
	@Valid
	@NotNull
	private TipoFuncionarioIdInput tipoFuncionario;
}
