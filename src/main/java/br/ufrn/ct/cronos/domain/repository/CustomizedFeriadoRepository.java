package br.ufrn.ct.cronos.domain.repository;

import java.util.Date;
import java.util.List;

public interface CustomizedFeriadoRepository {

    public List<Date> getDatasFeriadosByPeriodo(Long idPeriodo);
    
}
