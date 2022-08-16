package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "status_importacao_turmas")
public class StatusImportacaoTurmas implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "descricao")
    private String descricao;
    
}
