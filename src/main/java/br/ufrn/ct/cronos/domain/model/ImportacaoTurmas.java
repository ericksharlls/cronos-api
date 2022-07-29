package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    /*
     * A lista com todos os registros de histórico da Importação é preenchida apenas pelo método 'buscar' da classe ImportarTurmasService.
     */
    @Transient
    private List<HistoricoImportacaoTurmas> listaHistorico;

}
