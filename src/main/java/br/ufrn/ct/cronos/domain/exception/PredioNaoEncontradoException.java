package br.ufrn.ct.cronos.domain.exception;

public class PredioNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public PredioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PredioNaoEncontradoException(Long predioId) {
		this(String.format("Não existe um cadastro de Prédio com id %d. Informe um id válido para o Prédio.", predioId));
	}
	
}