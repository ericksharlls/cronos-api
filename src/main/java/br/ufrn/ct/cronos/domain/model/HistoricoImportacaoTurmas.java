package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "historico_importacoes_turmas")
public class HistoricoImportacaoTurmas {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico_importacao_turmas")
    private Long id;

    @Column(name = "registrado_em")
    private LocalDateTime registradoEm;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_status")
	private StatusImportacaoTurmas status;

    @ManyToOne
	@JoinColumn(name = "id_importacao_turmas")
	private ImportacaoTurmas importacaoTurmas;

}
