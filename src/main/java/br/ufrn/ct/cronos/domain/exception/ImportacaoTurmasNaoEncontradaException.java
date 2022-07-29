package br.ufrn.ct.cronos.domain.exception;

public class ImportacaoTurmasNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

	public ImportacaoTurmasNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public ImportacaoTurmasNaoEncontradaException(Long idImportacaoTurmas) {
		this(String.format("Não existe um cadastro de Importação de Turmas com id %d. Informe um id válido para a Importação.", idImportacaoTurmas));
	}
    
}
