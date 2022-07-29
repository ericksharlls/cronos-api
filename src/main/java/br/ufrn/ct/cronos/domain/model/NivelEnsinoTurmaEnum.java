package br.ufrn.ct.cronos.domain.model;

import java.util.HashMap;
import java.util.Map;

public enum NivelEnsinoTurmaEnum {

    INFANTIL("I", "INFANTIL"),
	MEDIO("M", "MÉDIO"),
	TÉCNICO("T", "TÉCNICO"),
	BÁSICO("B", "BÁSICO"),
	STRICTO_SENSU("S", "STRICTO SENSU"),
	DOUTORADO("D", "DOUTORADO"),
	MESTRADO("E", "MESTRADO"),
	RESIDENCIA("R", "RESIDÊNCIA"),
	FUNDAMENTAL("U", "FUNDAMENTAL"),
	TECNICO_INTEGRADO("N", "TÉCNICO INTEGRADO"),
	GRADUACAO("G", "GRADUAÇÃO"),
	LATO_SENSU("L", "LATO SENSU"),
	FORMACAO_COMPLEMENTAR("F", "FORMAÇÃO COMPLEMENTAR");

	private static final Map<String, NivelEnsinoTurmaEnum> listaNiveisEnsino = new HashMap<String, NivelEnsinoTurmaEnum>();
	
    static {
        for (NivelEnsinoTurmaEnum nivel : NivelEnsinoTurmaEnum.values()) {
			listaNiveisEnsino.put(nivel.getSigla(), nivel);
        }
    }
	
	private String sigla;
	private String descricao;
	
	private NivelEnsinoTurmaEnum(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return this.sigla;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

	public static NivelEnsinoTurmaEnum getBySigla(String siglaNivelEnsino) {
		NivelEnsinoTurmaEnum retorno = listaNiveisEnsino.get(siglaNivelEnsino);
		if(retorno == null) throw new IllegalArgumentException();
		return retorno;
	}

}
