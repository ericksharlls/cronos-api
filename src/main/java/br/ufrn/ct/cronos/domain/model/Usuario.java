package br.ufrn.ct.cronos.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "login_usuario")
    private String login;

    @Column(name = "senha_usuario")
    private String senha;

    @Column(name = "ativo_usuario")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name="id_funcionario")
    private Funcionario funcionario;
    
}
