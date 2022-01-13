package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalaModel {

    private Long id;
    private String nome;
    private String descricao;
    private Integer capacidade;
    private String tipoQuadro;
    private Boolean utilizarNaDistribuicao;
    private Boolean utilizarNoAgendamento;
    private Boolean distribuir;
	private PredioModel predio;
	private PerfilSalaTurmaModel perfilSalaTurma;
    
}
