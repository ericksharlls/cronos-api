package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tipo_funcionario")
public class TipoFuncionario {
    
    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_funcionario")
    private Long id;

    @Column(name = "nome_tipo_funcionario")
    private  String nome;

    @Column(name = "descricao_tipo_funcionario")
    private String descricao;

    


}
