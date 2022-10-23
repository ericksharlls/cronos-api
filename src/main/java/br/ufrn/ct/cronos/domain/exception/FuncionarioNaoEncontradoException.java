package br.ufrn.ct.cronos.domain.exception;

public class FuncionarioNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	public FuncionarioNaoEncontradoException(String mensagem) {
		super(mensagem);
		
	}
	
	public FuncionarioNaoEncontradoException(Long funcionarioId) {
		this(String.format("Não existe um cadastro de Funcionário com o id %d", funcionarioId));
	}
}
