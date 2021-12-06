package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Anotacoes @Data, @Getters e @Setters nao estao funcionando
@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "periodo")
public class Periodo {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPeriodo;
	
	@NotBlank
	@Size(max = 30)
	@Column(name="nome_periodo")
	private String nome;
	
	@NotBlank
	@NotNull
	@Size(max = 50)
	@Column(name="descricao_periodo")
	private String descricao;
	
	@NotNull
	@Column(name="data_inicio_periodo")
	private LocalDate dataInicio;
	
	@NotNull
	@Column(name="data_termino_periodo")
	private LocalDate dataTermino;
	
	@NotNull
	@Column(name="is_periodo_letivo")
	private Boolean isPeriodoLetivo;
	
	@NotNull
	@Column(name="ano_periodo")
	private Short ano;

	@NotNull
	@Column(name="numero_periodo")
	private Short periodo;


}
