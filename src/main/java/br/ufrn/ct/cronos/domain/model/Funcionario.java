package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
