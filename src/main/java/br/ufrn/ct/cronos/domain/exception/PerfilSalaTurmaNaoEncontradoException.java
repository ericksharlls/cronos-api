package br.ufrn.ct.cronos.domain.exception;

public class PerfilSalaTurmaNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public PerfilSalaTurmaNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PerfilSalaTurmaNaoEncontradoException(Long perfilSalTurmaId) {
		this(String.format("Não existe um cadastro de Perfil de Sala/Turma com id %d", perfilSalTurmaId));
	}
	
}