package br.ufrn.ct.cronos.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),;
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://cronos.ct.ufrn.br" + path;
		this.title = title;
	}
	
}