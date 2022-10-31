package br.ufrn.ct.cronos.api.model;

import br.ufrn.ct.cronos.domain.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TurmaModel {

    private Long id;

    private String horario;

    private String nomeDisciplina;

    private String codigoDisciplina;

    private Integer capacidade;

    private String numero;

    private Integer alunosMatriculados;

    private Boolean distribuir;

    private PerfilSalaTurma perfil;

    private Predio predio;

    private Periodo periodo;

    private Departamento departamento;

    private Set<Funcionario> docente;
}
