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