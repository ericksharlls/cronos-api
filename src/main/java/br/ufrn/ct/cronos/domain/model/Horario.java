package br.ufrn.ct.cronos.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "horario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Horario implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @Column(name="horario")
    private Integer horario;

    @Column(name="inicio_horario")
    private LocalTime inicioHorario;

    @Column(name="termino_horario")
    private LocalTime terminoHorario;

    @Column(name="horario_intermediario")
    private LocalTime horarioIntermediario;

    @Column(name="turno")
    private String turno;

    @Column(name="inicio_horario_absoluto")
    private LocalTime inicioHorarioAbsoluto;

    @Column(name="termino_horario_absoluto")
    private LocalTime terminoHorarioAbsoluto;

}
