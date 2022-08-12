package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

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
