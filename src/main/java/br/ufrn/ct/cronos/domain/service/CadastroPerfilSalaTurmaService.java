package br.ufrn.ct.cronos.domain.service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PerfilSalaTurmaNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

 

@Service
public class CadastroPerfilSalaTurmaService {
	
    private static final String MSG_PERFIL_SALA_TURMA_EM_USO 
        = "O perfil sala turma de id %d não pode ser removido, pois está em uso.";

	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
    @Transactional
	public PerfilSalaTurma salvar(PerfilSalaTurma perfilSalaTurma){
		perfilSalaTurmaRepository.detach(perfilSalaTurma);
    	
		Optional<PerfilSalaTurma> perfilExistente = perfilSalaTurmaRepository.findByNome(perfilSalaTurma.getNome());
		
		if(perfilExistente.isPresent() && !perfilExistente.get().equals(perfilSalaTurma)) {
			throw new NegocioException(String.format("Já existe um Perfil de Sala cadastrado com o nome \'%s\'",perfilSalaTurma.getNome()));
		}
		
		return perfilSalaTurmaRepository.save(perfilSalaTurma);
	}
	
    @Transactional
	public void excluir(Long perfilSalaTurmaId) {
        try {
        	perfilSalaTurmaRepository.deleteById(perfilSalaTurmaId);   
            perfilSalaTurmaRepository.flush(); 
        } catch (EmptyResultDataAccessException e){
            throw new PerfilSalaTurmaNaoEncontradoException(perfilSalaTurmaId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_PERFIL_SALA_TURMA_EM_USO, perfilSalaTurmaId)
            );
        }
    }

    public PerfilSalaTurma buscar(Long perfilSalaTurmaId) {
		return perfilSalaTurmaRepository.findById(perfilSalaTurmaId)
			.orElseThrow(() -> new PerfilSalaTurmaNaoEncontradoException(perfilSalaTurmaId));
	}

}
