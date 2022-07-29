package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UnidadeAPIUFRNModel {

    private Integer idUnidade, codigoUnidade;
	private String nomeUnidade, sigla;
	private Integer idUnidadeGestora;
    
}
