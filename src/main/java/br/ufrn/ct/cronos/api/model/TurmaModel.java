package br.ufrn.ct.cronos.api.model;

import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.model.Predio;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurmaModel {

    private Long id;

    private String horario;

    private String docente;

    private String nomeDisciplina;

    private String codigoDisciplina;

    private String local;

    private Integer capacidade;

    private String numero;

    private Integer alunosMatriculados;

    private Boolean distribuir;

    private PerfilSalaTurma perfil;

    private Predio predio;

    private Periodo periodo;

    private Departamento departamento;
}
