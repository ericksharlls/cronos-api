package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "predio")
public class Predio {
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_predio")
    private Long id;

    @Column(name = "nome_predio")
    private String nome;
    
    @Column(name = "descricao_predio")
    private String descricao;

}
