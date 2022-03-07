package br.ufrn.ct.cronos.domain.repository;


import java.time.LocalDate;





public interface CustomizedFeriadoRepository {

	Boolean verificarSeADataDoFeriadoCorrespondeAoPeriodoInformado(Long periodoId,LocalDate data);
	
	
}
