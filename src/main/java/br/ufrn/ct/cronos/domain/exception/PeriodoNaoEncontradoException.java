package br.ufrn.ct.cronos.domain.exception;

public class PeriodoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public PeriodoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PeriodoNaoEncontradoException(Long predioId) {
		this(String.format("Não existe um cadastro de Período com id %d", predioId));
	}
	
}