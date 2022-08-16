package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "sala")
public class Sala {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Long id;

    @Column(name = "nome_sala")
    private String nome;

    @Column(name = "descricao_sala")
    private String descricao;

    @Column(name = "capacidade_sala")
    private Integer capacidade;

    @Column(name = "tipo_quadro_sala")
    private String tipoQuadro;

    @Column(name = "utilizar_distribuicao")
    private Boolean utilizarNaDistribuicao;

    @Column(name = "utilizar_agendamento")
    private Boolean utilizarNoAgendamento;

    @Column(name = "distribuir")
    private Boolean distribuir;

    @ManyToOne
	@JoinColumn(name = "id_predio")
	private Predio predio;

    @ManyToOne
	@JoinColumn(name = "id_perfil")
	private PerfilSalaTurma perfilSalaTurma;
    
}
