package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.SalaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.TurmaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.TurmaModelAssembler;
import br.ufrn.ct.cronos.api.model.TurmaModel;
import br.ufrn.ct.cronos.api.model.input.TurmaInput;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.model.Turma;
import br.ufrn.ct.cronos.domain.repository.TurmaRepository;
import br.ufrn.ct.cronos.domain.service.CadastroTurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CadastroTurmaService cadastroTurma;

    @Autowired
    private TurmaInputDisassembler turmaInputDisassembler;

    @Autowired
    private TurmaModelAssembler turmaModelAssembler;

    // TODO Erick Shalls
    @GetMapping
    public Page<TurmaModel> listar () {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurmaModel cadastrar(@RequestBody @Valid TurmaInput turmaInput) {
            Turma turma = turmaInputDisassembler.toDomainObject(turmaInput);

            turma = cadastroTurma.salvar(turma);

            return turmaModelAssembler.toModel(turma);
    }

    @PutMapping("/{turmaId}")
    public TurmaModel atualizar (@PathVariable Long turmaId, @RequestBody @Valid TurmaInput turmaInput) {
        Turma turmaAtual = cadastroTurma.buscarOuFalhar(turmaId);

        turmaInputDisassembler.copyToDomainObject(turmaInput, turmaAtual);

        turmaAtual = cadastroTurma.atualizar(turmaAtual);

        return turmaModelAssembler.toModel(turmaAtual);
    }

    @DeleteMapping("/{turmaid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover (@PathVariable Long turmaid) {
        cadastroTurma.excluir(turmaid);
    }
}
