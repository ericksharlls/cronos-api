package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "departamento")
public class Departamento {
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_departamento")
    private Long id;

    @Column(name="nome_departamento")
    private String nome;

    @Column(name="descricao_departamento")
    private String descricao;

    @Column(name="id_departamento_sigaa")
    private Long idSigaa;
    
}