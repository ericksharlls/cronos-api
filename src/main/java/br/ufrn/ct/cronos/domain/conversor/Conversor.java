package br.ufrn.ct.cronos.domain.conversor;

import org.json.JSONObject;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Turma;
import br.ufrn.ct.cronos.domain.model.dto.DocenteDTO;
import br.ufrn.ct.cronos.domain.model.dto.ServidorDTO;
import br.ufrn.ct.cronos.domain.model.dto.TurmaDTO;

public class Conversor {
    
    public static TurmaDTO convertJsonObjectToTurmaDTO(JSONObject jsonObjectTurma) {
        TurmaDTO turmaDTO = new TurmaDTO();
		turmaDTO.setAgrupadora(false);
		turmaDTO.setCapacidadeAluno((Integer) jsonObjectTurma.get("capacidade-aluno"));
		turmaDTO.setCargaHoraria(0);
		turmaDTO.setCodigo((String) jsonObjectTurma.get("codigo-turma"));
		turmaDTO.setCodigoComponente((String) jsonObjectTurma.get("codigo-componente"));
						
		try {
		    turmaDTO.setDescricaoHorario((String) jsonObjectTurma.get("descricao-horario"));
		} catch (Exception e) {
			System.out.println("### Turma com horário NULO. Valor 'INDEFINIDO' atribuído para o horário");
			turmaDTO.setDescricaoHorario("INDEFINIDO");
		} 
						
						/*
						 * ### Tipos de Participantes: 
						 * [{"id-tipo-participante":1,"descricao":"DOCENTE"},
						 * {"id-tipo-participante":2,"descricao":"DOCENTE_ASSISTIDO"},
						 * {"id-tipo-participante":3,"descricao":"MONITOR"},
						 * {"id-tipo-participante":4,"descricao":"DISCENTE"},
						 * {"id-tipo-participante":5,"descricao":"DESCONHECIDO"}]
						 * */	
		turmaDTO.setId((Integer) jsonObjectTurma.get("id-turma"));
		Integer idTurmaAgrupadora = (Integer) jsonObjectTurma.get("id-turma-agrupadora");
		turmaDTO.setIdTurmaAgrupadora(idTurmaAgrupadora);
		if (idTurmaAgrupadora == null) {
			turmaDTO.setAgrupadora(true);
		}
		//turmaDTO.setIdTurmaAgrupadora(idTurmaAgrupadora);
					
		//turmaDTO.setLocal(local);
		turmaDTO.setNivel((String) jsonObjectTurma.get("sigla-nivel"));
		turmaDTO.setNomeComponente((String) jsonObjectTurma.get("nome-componente"));
										
		turmaDTO.setSituacao("");
		turmaDTO.setTotalSolicitacoes(0);

        return turmaDTO;
    }

	public static DocenteDTO convertJsonObjectToDocenteDTO(JSONObject jsonObjectDocente) {
		DocenteDTO docenteDTO = new DocenteDTO();
		docenteDTO.setChDedicada(0L);
		Integer idDocenteInteiro = (Integer) jsonObjectDocente.get("id-participante");
		docenteDTO.setIdServidor(idDocenteInteiro.longValue());
		Integer idTurmaInteiro = (Integer) jsonObjectDocente.get("id-turma");
		docenteDTO.setIdTurma(idTurmaInteiro.longValue());
		docenteDTO.setNome((String) jsonObjectDocente.get("nome"));

		return docenteDTO;
	}

	public static ServidorDTO convertJsonObjectToServidorDTO(JSONObject jsonObjectServidor) {
		ServidorDTO servidorDTO = new ServidorDTO();
		Integer idDocenteInteiro = (Integer) jsonObjectServidor.get("id-servidor");
		servidorDTO.setIdServidor(idDocenteInteiro.longValue());
		servidorDTO.setNome((String) jsonObjectServidor.get("nome"));

		return servidorDTO;
	}

	public static Turma convertTurmaDtoToTurmaDomainObject(TurmaDTO turmaDTO) {
		Turma turmaParaCadastro = new Turma();
		turmaParaCadastro.setAlunosMatriculados(turmaDTO.getQtdMatriculados());
		turmaParaCadastro.setCapacidade(turmaDTO.getCapacidadeAluno());
		turmaParaCadastro.setCodigoDisciplina(turmaDTO.getCodigoComponente());
		turmaParaCadastro.setDistribuir(true);

		String horarioTemp = "";
		if (turmaDTO.getDescricaoHorario().contains("(")) {
			horarioTemp = turmaDTO.getDescricaoHorario().substring(0, turmaDTO.getDescricaoHorario().indexOf("("));
			horarioTemp = horarioTemp.trim();
		} else {
			horarioTemp = turmaDTO.getDescricaoHorario();
		}
		turmaParaCadastro.setHorario(horarioTemp);
		
		Predio predio = new Predio();
		predio.setId(Long.valueOf(1));
        turmaParaCadastro.setPredio(predio);

		PerfilSalaTurma perfilSalaTurma = new PerfilSalaTurma();
		perfilSalaTurma.setId(Long.valueOf(1));
        turmaParaCadastro.setPerfil(perfilSalaTurma);

        turmaParaCadastro.setNomeDisciplina(turmaDTO.getNomeComponente());
        turmaParaCadastro.setNumero(turmaDTO.getCodigo());
		turmaParaCadastro.setIdTurmaSIGAA(turmaDTO.getId().longValue());
        turmaParaCadastro.setLocal("INDEFINIDO");
		
		return turmaParaCadastro;
	}

	public static void transferTurmaDtoResumidoToTurmaDomainObject(TurmaDTO turmaDTO, Turma turmaParaAtualizacao) {
		turmaParaAtualizacao.setAlunosMatriculados(turmaDTO.getQtdMatriculados());
		turmaParaAtualizacao.setCapacidade(turmaDTO.getCapacidadeAluno());
		turmaParaAtualizacao.setCodigoDisciplina(turmaDTO.getCodigoComponente());
		String horarioTemp = "";
		if (turmaDTO.getDescricaoHorario().contains("(")) {
			horarioTemp = turmaDTO.getDescricaoHorario().substring(0, turmaDTO.getDescricaoHorario().indexOf("("));
			horarioTemp = horarioTemp.trim();
		} else {
			horarioTemp = turmaDTO.getDescricaoHorario();
		}
		turmaParaAtualizacao.setHorario(horarioTemp);
		turmaParaAtualizacao.setNomeDisciplina(turmaDTO.getNomeComponente());
		turmaParaAtualizacao.setNumero(turmaDTO.getCodigo());
	}

}
