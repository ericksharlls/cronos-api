package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
	@Column(name="nome_periodo")
	private String nome;
	
	@Column(name="descricao_periodo")
	private String descricao;
	
	@Column(name="data_inicio_periodo")
	private LocalDate dataInicio;
	
	@Column(name="data_termino_periodo")
	private LocalDate dataTermino;
	
	@Column(name="is_periodo_letivo")
	private Boolean isPeriodoLetivo;
	
	@Column(name="ano_periodo")
	private Short ano;

	@Column(name="numero_periodo")
	private Short numero;
	
}
