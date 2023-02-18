package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.DepartamentoModelAssembler;
import br.ufrn.ct.cronos.api.model.DepartamentoModel;
import br.ufrn.ct.cronos.domain.model.Departamento;
import br.ufrn.ct.cronos.domain.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/departamentos")
public class DepartamentoController {
    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    DepartamentoModelAssembler departamentoModelAssembler;

    @GetMapping
    public Page<DepartamentoModel> buscar (@PageableDefault(size = 10) Pageable pageable) {
        Page<Departamento> departamentosPage = departamentoRepository.findAll(pageable);

        Page<DepartamentoModel> departamentoModelPage = new PageImpl<>(
                departamentoModelAssembler.toCollectionModel(departamentosPage.getContent()),
                                                                                    pageable,
                                                                                    departamentosPage.getTotalElements()
        );

        return departamentoModelPage;
    }
}
