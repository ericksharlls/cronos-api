package br.ufrn.ct.cronos.domain.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.FeriadoNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.model.Feriado;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.FeriadoRepository;


@Service 
public class CadastroFeriadoService {

	private static final String MSG_FERIADO_COM_DATA_EM_USO = "Já existe um feriado na data informada";
	private static final String MSG_FERIADO_FORA_DO_PERIODO_INFORMADO = "O feriado esta fora das datas do periodo informado";
	private static final String MSG_FERIADO_EM_USO = "O feriado de id %d não pode ser removido, pois esta em uso";
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private CadastroPeriodoService cadastroPeriodoService;
	
	@Transactional
	public Feriado buscar(Long idFeriado) {
		
		return feriadoRepository.findById(idFeriado).orElseThrow(() -> new FeriadoNaoEncontradoException(idFeriado));
	}
//	// tornar um só com salvar
//	@Transactional
//	public Feriado atualizar(Feriado feriadoAtual) {
//		
//		
//		Long periodoId = feriadoAtual.getPeriodo().getId();
//		
//		Periodo periodo = cadastroPeriodoService.buscar(periodoId);
//		
//		feriadoAtual.setPeriodo(periodo);
//		
//		vericarSeOFeriadoEstaNoPeriodoInformado(feriadoAtual, periodo);
//		verificarSeJaExisteUmFeriadoComMesmaData(feriadoAtual);
//		
//		return feriadoRepository.save(feriadoAtual);
//	}
	
	@Transactional
	public Feriado salvar(Feriado feriado) {
		feriadoRepository.detach(feriado);
		
		Long periodoId = feriado.getPeriodo().getId();
		
		Periodo periodo = cadastroPeriodoService.buscar(periodoId);
		
		feriado.setPeriodo(periodo);
		
		vericarSeOFeriadoEstaNoPeriodoInformado(feriado, periodo);
		verificarSeJaExisteUmFeriadoComMesmaData(feriado);
		
		return feriadoRepository.save(feriado);
	}
	
	@Transactional
	public void excluir(Long feriadoId) {
		try {
			feriadoRepository.deleteById(feriadoId);
			feriadoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FeriadoNaoEncontradoException(feriadoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_FERIADO_EM_USO,feriadoId));
		}
	}
	
	// refatorar metodo para diminuir um acesso ao banco
	private void vericarSeOFeriadoEstaNoPeriodoInformado(Feriado feriado, Periodo periodo) {
		Long periodoId = periodo.getId();
		
		Boolean existe = feriadoRepository.verificarSeADataDoFeriadoCorrespondeAoPeriodoInformado(periodoId, feriado.getData());
		
		if(!existe) {
			throw new NegocioException(MSG_FERIADO_FORA_DO_PERIODO_INFORMADO);
			
		}
	}
	
	private void verificarSeJaExisteUmFeriadoComMesmaData(Feriado feriado) {
		LocalDate data = feriado.getData();
		
		Optional<Feriado> feriadoExistente = feriadoRepository.findByData(data);
		
		if(feriadoExistente.isPresent() && !feriadoExistente.get().equals(feriado)) {
			throw new NegocioException(MSG_FERIADO_COM_DATA_EM_USO);
		}
	}

}
