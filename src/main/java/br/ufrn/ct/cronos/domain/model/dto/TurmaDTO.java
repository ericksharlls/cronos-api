package br.ufrn.ct.cronos.domain.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TurmaDTO {

    @EqualsAndHashCode.Include
    private Integer id;
    private Integer idTurmaAgrupadora, idUnidade, cargaHoraria, capacidadeAluno, totalSolicitacoes, qtdMatriculados;
	private String unidade, codigoComponente, nomeComponente, codigo, descricaoHorario, local, situacao, nivel;
	private Boolean agrupadora;
	private List<DocenteDTO> docentesList = new ArrayList<>();
    
}
