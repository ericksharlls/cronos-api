package br.ufrn.ct.cronos.domain.exception;

public class PeriodoSemFeriadoExeption extends EntidadeNaoEncontradaException {

	public PeriodoSemFeriadoExeption(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}
	
	public PeriodoSemFeriadoExeption(Long periodoId) {
		this(String.format("Não existe um cadastro de Feriado no Periodo de id %d",periodoId));
	}
		
}
