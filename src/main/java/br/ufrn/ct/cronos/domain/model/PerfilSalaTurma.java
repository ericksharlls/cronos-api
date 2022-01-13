package br.ufrn.ct.cronos.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "perfil_sala_turma")
public class PerfilSalaTurma {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil_sala_turma")
	private Long id;
	
	@Column(name = "nome_perfil_sala_turma")
	private String nome;
	
	@Column(name = "descricao_perfil_sala_turma")
	private String descricao;
	

}
