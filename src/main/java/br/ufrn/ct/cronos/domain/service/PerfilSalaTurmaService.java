package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.EntidadeNaoEncontradaException;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;

@Service
public class PerfilSalaTurmaService {
	
	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	public PerfilSalaTurma salvar(PerfilSalaTurma perfilSalaTurma) {
		return perfilSalaTurmaRepository.save(perfilSalaTurma);
	}
	
	public void excluir(Long perfilSalaTurmaId) {
        try {
        	perfilSalaTurmaRepository.deleteById(perfilSalaTurmaId);   
        } catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                String.format("Não existe um cadastro de um perfil sala turma com id %d", perfilSalaTurmaId)
            );
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format("O perfil sala turma de id %d não pode ser removido, pois está em uso", perfilSalaTurmaId)
            );
        }
    }

}
