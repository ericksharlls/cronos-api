package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.assembler.TurmaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.TurmaModelAssembler;
import br.ufrn.ct.cronos.api.model.TurmaModel;
import br.ufrn.ct.cronos.api.model.input.TurmaInput;
import br.ufrn.ct.cronos.core.data.PageableTranslator;
import br.ufrn.ct.cronos.domain.model.Turma;

import br.ufrn.ct.cronos.domain.service.CadastroTurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import br.ufrn.ct.cronos.domain.filter.TurmaFilter;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/turmas")
public class TurmaController {

    @Autowired
    private CadastroTurmaService cadastroTurma;

    @Autowired
    private TurmaInputDisassembler turmaInputDisassembler;

    @Autowired
    private TurmaModelAssembler turmaModelAssembler;

    @GetMapping
	public Page<TurmaModel> pesquisar(TurmaFilter filtro, @PageableDefault(size = 10) Pageable pageable) {
		pageable = traduzirPageable(pageable);
		Page<Turma> turmasPage = cadastroTurma.pesquisar(filtro, pageable);
		
		Page<TurmaModel> turmasModelPage = new PageImpl<>(
			//1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
			turmaModelAssembler.toCollectionModel(turmasPage.getContent()),
			//prediosPage.getContent(),
			//2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
			pageable,
			//3 Parâmetro: total de elementos da lista
			turmasPage.getTotalElements()
		);

		return turmasModelPage;
	}

	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = Map.of(
				"codigoDisciplina", "codigoDisciplina",
				"nomeDisciplina", "nomeDisciplina"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
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
