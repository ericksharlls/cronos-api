package br.ufrn.ct.cronos.domain.filter;

import javax.validation.constraints.AssertTrue;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaUnidadeAPIUFRNFilter {
    
    private String nome;
    private Integer idUnidadeResponsavel;
    private Boolean apenasUnidadesAcademicas = Boolean.FALSE;
    /*
	 ** O prefixo 'is' foi adicionado nos métodos de validação a seguir, pois apenas a anotação @AssertTrue não 
	 ** é suficiente para que os métodos sejam executados.
	 ** Para o método ser executado, ele precisa seguir a convenção de nomes getter (iniciando com get ou is).
	*/

	/*
	 ** Método para validar se o Nome ou a IdUnidadeResponsavel foram informados.
	*/ 
	@AssertTrue
	private boolean isNomeOuIdUnidadeResponsavel() {
		if(!StringUtils.hasText(nome) && idUnidadeResponsavel == null) {
			return false;
		}
		return true;
	}

}
