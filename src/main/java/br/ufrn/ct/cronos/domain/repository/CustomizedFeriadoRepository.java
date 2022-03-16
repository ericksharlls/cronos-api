package br.ufrn.ct.cronos.domain.repository;


import java.time.LocalDate;

// não vai precisar mais
public interface CustomizedFeriadoRepository {

	Boolean verificarSeADataDoFeriadoCorrespondeAoPeriodoInformado(Long periodoId,LocalDate data);
	
	
}
