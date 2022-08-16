package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

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
