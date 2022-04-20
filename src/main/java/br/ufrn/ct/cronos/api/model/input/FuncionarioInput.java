package br.ufrn.ct.cronos.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.util.StringUtils;

import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioInput {
	
	@NotBlankAndSizeForString(max = 50)
	private String nome;
	
	private String matricula;
	
	/*
	 ** Aceita valor nulo.
	 ** Qualquer String informada será validada.
	*/
	@CPF
	private String cpf;
	
	/*
	 ** Aceita valor nulo.
	 ** Qualquer String informada será validada, com exceção de uma String vazia.
	*/ 
	@Email
	private String email;
	
	private String telefone;
	
	private String ramal;
	
	private Long idSigaa;
	
	@Valid
	@NotNull
	private TipoFuncionarioIdInput tipoFuncionario;

	/*
	 ** Método para validar se o CPF ou a Matrícula foram informados.
	 ** O prefixo 'is' foi adicionado, pois apenas a anotação @AssertTrue não foi suficiente para que o método fosse executado.
	 ** Para o método ser executado, ele precisa seguir a convenção de nomes getter (iniciando com get ou is).
	*/ 
	@AssertTrue
	private boolean isCpfOuMatricula() {
		if(!StringUtils.hasText(cpf) && !StringUtils.hasText(matricula)) {
			return false;
		}
		return true;
	}

}
