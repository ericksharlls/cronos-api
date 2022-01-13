package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

import br.ufrn.ct.cronos.core.validations.IntervaloEntreDatas;
import br.ufrn.ct.cronos.core.validations.NotBlankAndSizeForString;
import br.ufrn.ct.cronos.core.validations.NotNullAndRangeForNumber;
import br.ufrn.ct.cronos.core.validations.RangeForYear;
import lombok.Data;
import lombok.EqualsAndHashCode;

@IntervaloEntreDatas(dataInicialField = "dataInicio", dataFinalField = "dataTermino")
@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "periodo")
public class Periodo {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_periodo")
	private Long id;
	
	@NotBlankAndSizeForString(max = 30)
	@Column(name="nome_periodo")
	private String nome;
	
	@NotBlankAndSizeForString(max = 50)
	@Column(name="descricao_periodo")
	private String descricao;
	
	@NotNull
	@Column(name="data_inicio_periodo")
	private LocalDate dataInicio;
	
	@NotNull
	@Column(name="data_termino_periodo")
	private LocalDate dataTermino;
	
	@Column(name="is_periodo_letivo")
	private Boolean isPeriodoLetivo;
	
	@RangeForYear
	@Column(name="ano_periodo")
	private Short ano;

	@NotNullAndRangeForNumber(min = 1, max = 10)
	@Column(name="numero_periodo")
	private Short periodo;
	
}
