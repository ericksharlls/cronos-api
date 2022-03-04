package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Feriado;

public interface FeriadoRepository extends CustomJpaRepository<Feriado, Long>, CustomizedFeriadoRepository {

}
