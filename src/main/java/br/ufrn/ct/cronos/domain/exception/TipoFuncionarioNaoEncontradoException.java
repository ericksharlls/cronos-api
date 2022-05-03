package br.ufrn.ct.cronos.domain.exception;

public class TipoFuncionarioNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	public TipoFuncionarioNaoEncontradoException(String mensagem) {
		super(mensagem);
		
	}
	
	public TipoFuncionarioNaoEncontradoException(Long tipoFuncionarioId) {
		this(String.format("Não existe um cadastro de tipo funcionario com o id %d",tipoFuncionarioId));
	}
	
}
