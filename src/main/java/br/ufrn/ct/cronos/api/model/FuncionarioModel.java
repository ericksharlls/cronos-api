package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioModel {
	
	private Long id;

	private String nome;
	
	private String matricula;
	
	private String cpf;
	
	private String email;
	
	private String telefone;
	
	private String ramal;
	
	private TipoFuncionarioModel tipoFuncionario;
}
