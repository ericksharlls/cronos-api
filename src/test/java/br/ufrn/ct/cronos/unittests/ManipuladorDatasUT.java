package br.ufrn.ct.cronos.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.ufrn.ct.cronos.core.utils.ManipuladorDatas;

public class ManipuladorDatasUT {

    @Test
    public void testRetornaDatasLetivasPorDias() {
        List<LocalDate> intervaloDatas = new ArrayList<LocalDate>();
        List<LocalDate> feriados = new ArrayList<LocalDate>();
        String horarioTurma = "";
        assertEquals(ManipuladorDatas.retornaDatasLetivasPorDias(intervaloDatas, feriados, horarioTurma), null);
    } 

}
