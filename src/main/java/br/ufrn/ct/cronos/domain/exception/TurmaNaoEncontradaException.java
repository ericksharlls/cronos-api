package br.ufrn.ct.cronos.domain.exception;

public class TurmaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public TurmaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public TurmaNaoEncontradaException(Long turmaId) {
        this(String.format("Não existe um cadastro de turma com o id %d",turmaId));
    }
}
