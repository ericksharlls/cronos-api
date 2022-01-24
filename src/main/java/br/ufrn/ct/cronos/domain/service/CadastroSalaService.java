package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.SalaNaoEncontradaException;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;

@Service
public class CadastroSalaService {

    private static final String MSG_SALA_EM_USO 
            = "Sala de id %d não pode ser removida, pois está em uso";

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
	private CadastroPredioService cadastroPredio;

    @Autowired
	private CadastroPerfilSalaTurmaService cadastroPerfilSalaTurma;

    @Transactional
    public Sala salvar(Sala sala) {
        Long predioId = sala.getPredio().getId();
		Long perfilSalaTurmaId = sala.getPerfilSalaTurma().getId();

        Predio predio = cadastroPredio.buscar(predioId);
        PerfilSalaTurma perfilSalaTurma = cadastroPerfilSalaTurma.buscar(perfilSalaTurmaId);
        
		sala.setPredio(predio);
		sala.setPerfilSalaTurma(perfilSalaTurma);

		return salaRepository.save(sala);
    }

    @Transactional
    public void excluir(Long salaId) {
        try {
            salaRepository.deleteById(salaId);
        } catch (EmptyResultDataAccessException e){
            throw new SalaNaoEncontradaException(salaId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
				String.format(MSG_SALA_EM_USO, salaId));
        }
    }

    public Sala buscarOuFalhar(Long salaId) {
		return salaRepository.findById(salaId)
			.orElseThrow(() -> new SalaNaoEncontradaException(salaId));
	}
    
}
