package br.ufrn.ct.cronos.domain.model.dto;

import lombok.Data;

@Data
public class DepartamentoDTO {

    private Integer idUnidade, codigoUnidade;
	private String nomeUnidade, sigla, hierarquiaOrganizacional;
	private Integer idUnidadeGestora, idNivelOrganizacional, idClassificacaoUnidade;
	private Boolean unidadePatrimonial;
	private String email, telefones;
	private Integer idMunicipio, idAmbienteOrganizacional, idTipoUnidadeOrganizacional, idAreaAtuacaoUnidade;
    
}
