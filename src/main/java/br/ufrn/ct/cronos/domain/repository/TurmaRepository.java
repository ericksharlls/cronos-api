package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.model.Turma;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends CustomJpaRepository<Turma, Long>, CustomizedTurmaRepository, JpaSpecificationExecutor<Turma> {

    @Query("from Turma t WHERE t.departamento.id = :departamentoId AND t.periodo.id = :periodoId")
    List<Turma> findByDepartamentoAndPeriodo(@Param("departamentoId") Long departamentoId, @Param("periodoId") Long periodoId);

    Turma findByIdTurmaSIGAA(Long idTurmaSIGAA);

    @Query("from Turma t WHERE  t.codigoDisciplina = :codigo AND t.horario = :horario AND t.numero = :numero AND t.periodo.id = :idPeriodo")
    Optional<Turma> buscarTurmaComMesmoParametro(@Param("codigo") String codigo, @Param("horario") String horario, @Param("numero") String numero, @Param("idPeriodo") Long idPeriodo);

    @Query("from Turma t WHERE  t.codigoDisciplina = :codigo AND t.horario = :horario AND t.numero = :numero AND t.periodo.id = :idPeriodo AND t.id <> :idTurma")
    Optional<Turma> buscarTurmaComMesmoParametro(@Param("codigo") String codigo, @Param("horario") String horario, @Param("numero") String numero, @Param("idPeriodo") Long idPeriodo, @Param("idTurma") Long idTurma);

}