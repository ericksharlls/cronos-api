package br.ufrn.ct.cronos.domain.repository;

import java.time.LocalDate;

public interface CustomizedPerfilSalaTurmaRepository {
	public boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataFim);
}
