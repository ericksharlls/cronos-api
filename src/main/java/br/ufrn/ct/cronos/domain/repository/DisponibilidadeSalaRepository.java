package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.DisponibilidadeSala;

public interface DisponibilidadeSalaRepository extends CustomJpaRepository<DisponibilidadeSala, Long>, CustomizedDisponibilidadeSalaRepository {
    
}
