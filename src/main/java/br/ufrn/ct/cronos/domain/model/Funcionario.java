package br.ufrn.ct.cronos.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Long id;

    @Column(name="nome_funcionario")
    private String nome;

    @Column(name="matricula_funcionario")
    private String matricula;

    @Column(name="cpf_funcionario")
    private String cpf;

    @Column(name="email_funcionario")
    private String email;

    @Column(name="telefone_funcionario")
    private String telefone;

    @Column(name="ramal_funcionario")
    private String ramal;

    @Column(name="id_sigaa_funcionario")
    private Long idSigaaFuncionario;

    @ManyToOne
    @JoinColumn(name="id_tipo_funcionario")
    private TipoFuncionario tipoFuncionario;

    @ManyToMany(mappedBy = "docentes")
    private Set<Turma> turmas = new HashSet<>();
    
}
