package br.ufrn.ct.cronos.domain.repository;

import br.ufrn.ct.cronos.domain.filter.ConsultaUnidadeAPIUFRNFilter;
import br.ufrn.ct.cronos.domain.model.dto.DepartamentoDTO;
import br.ufrn.ct.cronos.domain.model.dto.TurmaDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TurmaApiUfrnRepository {

    public List<DepartamentoDTO> retornaUnidadesAcademicasPorIdCentro(Integer idCentro);

    public List<DepartamentoDTO> retornaUnidadesPorNomeCentro(ConsultaUnidadeAPIUFRNFilter filtro);

    public List<TurmaDTO> retornaTurmasAbertasPorSiglaNivelEnsinoIdDepartamentoAnoPeriodo(Set<String> siglanivelEnsino, Integer idDepartamento, Integer ano, Integer periodo);
    
    public DepartamentoDTO retornaUnidadePorId(Integer idUnidade);

}
