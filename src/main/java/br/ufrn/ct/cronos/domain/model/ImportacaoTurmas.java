package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "importacoes_turmas")
public class ImportacaoTurmas {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "horario_ultima_operacao")
    private LocalDateTime horarioUltimaOperacao;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento")
	private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_status")
	private StatusImportacaoTurmas status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_predio_turma")
    private Predio predio;

    /*
     * A lista com todos os registros de histórico da Importação é preenchida apenas pelo método 'buscar' da classe ImportarTurmasService.
     */
    @Transient
    private List<HistoricoImportacaoTurmas> listaHistorico;

}
