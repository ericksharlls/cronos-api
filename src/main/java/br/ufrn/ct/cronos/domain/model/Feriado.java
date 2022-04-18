package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "feriado")
public class Feriado {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_feriado")
	private Long id;
	
	@Column(name = "descricao_feriado")
	private String descricao;
	
	@Column(name = "data_feriado")
	private LocalDate data;
	
	@ManyToOne
	@JoinColumn(name =  "id_periodo")
	private Periodo periodo;
}

