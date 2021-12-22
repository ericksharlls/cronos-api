package br.ufrn.ct.cronos.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.CustomizedPerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;

@Repository
public class PerfilSalaTurmaRepositoryImpl implements CustomizedPerfilSalaTurmaRepository{

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	@Override
	public boolean verificarIntervaloDatasJaExiste(LocalDate dataInicio, LocalDate dataFim){
		String consulta = "SELECT pst FROM PerfilSalaTurma pst WHERE :dataInicio BETWEEN pst.dataInicio AND pst.dataFim"
				+ "AND :dataFim BETWEEN pst.dataIncio AND pst.datafim";
		
		TypedQuery<PerfilSalaTurma> query  = manager.createQuery(consulta,PerfilSalaTurma.class );
		
		query.setParameter("dataInicio",dataInicio);
		query.setParameter("dataFim",dataFim);
		
		List<PerfilSalaTurma> PerfisJaExistentes = (List<PerfilSalaTurma>) query.getResultList();
		
		if(PerfisJaExistentes.isEmpty()) {
			return true;
		}
		return false;
	}
	

	
}
