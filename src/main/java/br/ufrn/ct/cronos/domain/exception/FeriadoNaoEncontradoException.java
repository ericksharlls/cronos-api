package br.ufrn.ct.cronos.domain.exception;

public class FeriadoNaoEncontradoException extends EntidadeNaoEncontradaException {

	public FeriadoNaoEncontradoException(String mensagem) {
		super(mensagem);
		
	}
	
	public FeriadoNaoEncontradoException(Long feriadoId) {
		this(String.format("Não existe um cadastro de Feriado com o id %d",feriadoId));
	}

}
