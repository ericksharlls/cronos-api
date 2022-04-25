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
	 ** Para tratar uma String vazia, foi criado o método 'isCampoEmailVazio'.
	*/ 
	@Email
	private String email;
	
	private String telefone;
	
	private String ramal;
	
	@Valid
	@NotNull
	private TipoFuncionarioIdInput tipoFuncionario;

	/*
	 ** O prefixo 'is' foi adicionado nos métodos de validação a seguir, pois apenas a anotação @AssertTrue não 
	 ** é suficiente para que os métodos sejam executados.
	 ** Para o método ser executado, ele precisa seguir a convenção de nomes getter (iniciando com get ou is).
	*/

	/*
	 ** Método para validar se o CPF ou a Matrícula foram informados.
	*/ 
	@AssertTrue
	private boolean isCpfOuMatricula() {
		if(!StringUtils.hasText(cpf) && !StringUtils.hasText(matricula)) {
			return false;
		}
		return true;
	}

	/*
	 ** A constraint @Email aceita uma String vazia.
	 ** O método abaixo foi criado para contornar isso e retornar mensagem de erro se a String do e-mail vier vazia.
	 ** Já se uma String não-vazia for informada, será tratada normalmente pela constraint @Email.
	*/
	@AssertTrue
	private boolean isCampoEmailVazio() {
		if(email != null && email.isEmpty()) {
			return false;
		}
		return true;
	}

	/*
	 ** Esse método valida se a matrícula é uma sequência numérica composta de 7 a 11 dígitos.
	*/
	@AssertTrue
	private boolean isMatriculaInvalida() {
		if(matricula != null && (email.isEmpty() || !matricula.matches("\\d{7,11}"))) {
			return false;
		}
		return true;
	}

}
