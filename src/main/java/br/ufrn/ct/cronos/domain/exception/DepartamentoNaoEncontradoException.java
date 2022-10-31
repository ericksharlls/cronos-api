package br.ufrn.ct.cronos.domain.exception;

public class DepartamentoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public DepartamentoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public DepartamentoNaoEncontradoException(Long departamentoId) {
		this(String.format("Não existe um cadastro de Departamento com id %d", departamentoId));
	}

}
