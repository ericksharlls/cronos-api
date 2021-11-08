package br.ufrn.ct.cronos.domain.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

//Anotacoes @Date, @Getters e @Setters nao estao funcionando 
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "periodo")
public class Periodo {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_periodo;
	
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
	private Date dt_incio;
	
	@NotNull
	@Column(name="data_termino_periodo")
	private Date dt_termino;
	
	@NotNull
	@Max(1)
	@Min(0)
	@Column(name="is_periodo_letivo")
	private Short is_periodo;
	
	@NotNull
	@Column(name="ano_periodo")
	private Short ano;

	@NotNull
	@Column(name="numero_periodo")
	private Short periodo;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDt_incio() {
		return dt_incio;
	}

	public void setDt_incio(Date dt_incio) {
		this.dt_incio = dt_incio;
	}

	public Date getDt_termino() {
		return dt_termino;
	}

	public void setDt_termino(Date dt_termino) {
		this.dt_termino = dt_termino;
	}

	public Short getIs_periodo() {
		return is_periodo;
	}

	public void setIs_periodo(Short is_periodo) {
		this.is_periodo = is_periodo;
	}

	public Short getAno() {
		return ano;
	}

	public void setAno(Short ano) {
		this.ano = ano;
	}

	public Short getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Short periodo) {
		this.periodo = periodo;
	}

	public long getId_periodo() {
		return id_periodo;
	}

	public void setId_periodo(long id_periodo) {
		this.id_periodo = id_periodo;
	}

}
