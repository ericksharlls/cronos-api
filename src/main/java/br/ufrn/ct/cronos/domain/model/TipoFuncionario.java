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
@Table(name = "tipo_funcionario")
public class TipoFuncionario {
    
    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_funcionario")
    private Long idTipo;

    @Column(name = "nome_tipo_funcionario")
    private  String nomeTipo;

    @Column(name = "descrição_tipo_funcionario")
    private String descricaoTipo;

    


}
