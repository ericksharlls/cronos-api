package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "agendamento")
public class Agendamento {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;

    @Column(name="motivo")
    private String motivo;

    @Column(name="hora_realizacao_agendamento")
    private LocalDateTime horaRealizacaoAgendamento;

    @ManyToOne
    @JoinColumn(name="id_funcionario")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name="id_periodo")
    private Periodo periodo;
    
    @ManyToOne
    @JoinColumn(name="id_usuario_sistema")
    private Usuario usuarioSistema;

}
