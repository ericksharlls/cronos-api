package br.ufrn.ct.cronos.domain.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "periodo")
public class Periodo {
	@EqualsAndHashCode.Include // deixa explicito que o unico parametro a ser usado pelos metodos sera o id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_periodo;
	
	@Column(name="nome_periodo")
	private String nome;
	
	@Column(name="descricao_periodo")
	private String descricao;
	
	@Column(name="data_inicio_periodo")
	private Date dt_incio;
	
	@Column(name="data_termino_periodo")
	private Date dt_termino;
	
	@Column(name="is_periodo_letivo")
	private Short is_periodo;
	
	@Column(name="ano_periodo")
	private Short ano;
	
	@Column(name="numero_periodo")
	private Short periodo;
		
}
