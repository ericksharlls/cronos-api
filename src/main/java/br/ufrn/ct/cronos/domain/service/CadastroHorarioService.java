package br.ufrn.ct.cronos.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.model.Horario;
import br.ufrn.ct.cronos.domain.repository.HorarioRepository;

@Service
public class CadastroHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public Horario findByTurnoAndHorario(String turno, Integer horario){
        List<Horario> horarios = horarioRepository.findAll();
        horarios = horarios
                    .stream()
                        .filter(h -> h.getTurno().equals(turno) && h.getHorario().equals(horario))
                    .collect(Collectors.toList());
        Horario retorno = horarios.get(0);
        return retorno;
    }

    public List<Horario> listar(){
        return horarioRepository.findAll();
    }
    
}
