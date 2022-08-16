package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

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

