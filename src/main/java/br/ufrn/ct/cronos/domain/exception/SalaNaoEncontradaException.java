package br.ufrn.ct.cronos.domain.exception;

public class SalaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public SalaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public SalaNaoEncontradaException(Long predioId) {
		this(String.format("Não existe um cadastro de Sala com id %d. Informe um id válido para a Sala.", predioId));
	}
	
}