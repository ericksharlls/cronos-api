package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDateTime;

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
