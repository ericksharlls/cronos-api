package br.ufrn.ct.cronos.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "turma")
public class Turma {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma")
    private Long id;

    @Column(name = "horario_turma")
    private String horario;

    @Column(name = "nome_docente_turma")
    private String docente;

    @Column(name = "nome_componente_turma")
    private String nomeDisciplina;

    @Column(name = "codigo_componente_turma")
    private String codigoDisciplina;

    @Column(name = "local")
    private String local;

    @Transient
    private String sala;

    @Column(name = "capacidade_turma")
    private Integer capacidade;

    @Column(name = "numero_turma")
    private String numero;

    @Column(name = "alunos_matriculados_turma")
    private Integer alunosMatriculados;

    @Column(name = "distribuir")
    private Boolean distribuir;

    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private PerfilSalaTurma perfil;

    @ManyToOne
    @JoinColumn(name="id_predio")
    private Predio predio;

    @ManyToOne
    @JoinColumn(name="id_periodo")
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name="id_departamento")
    private Departamento departamento;

    @Column(name = "id_sala_temp")
    private Long idSalaTemp;

    @Column(name = "id_turma_sigaa")
    private Long idTurmaSIGAA;
   
    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "turma_docente",
        joinColumns = @JoinColumn(name = "id_turma"),
        inverseJoinColumns = @JoinColumn(name = "id_docente")
    )
    private Set<Funcionario> docentes = new HashSet<>();

    public void addDocente(Funcionario docente) {
        docentes.add(docente);
        docente.getTurmas().add(this);
    }
    
    public void removeDocente(Funcionario docente) {
        docentes.remove(docente);
        docente.getTurmas().remove(this);
    }
    
}
