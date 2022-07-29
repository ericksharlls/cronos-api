package br.ufrn.ct.cronos.domain.model;

import java.time.LocalDate;

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
@Table(name = "disponibilidade_sala")
public class DisponibilidadeSala {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidade_sala")
    private Long id;

	@Column(name="data_reserva")
    private LocalDate dataReserva;

    @ManyToOne
    @JoinColumn(name="id_periodo")
    private Periodo periodo;
    
    @ManyToOne
    @JoinColumn(name="id_sala")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name="id_horario_sala")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name="id_turma")
    private Turma turma;
    
    @ManyToOne
    @JoinColumn(name="id_agendamento")
    private Agendamento agendamento;
    
}
