package posweb.tarefas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import posweb.tarefas.domain.PrioridadeTarefa;
import posweb.tarefas.domain.StatusTarefa;
import posweb.tarefas.dto.TarefaCadastroRequest;
import posweb.tarefas.dto.TarefaResponse;
import posweb.tarefas.service.TarefaService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping({"/tarefas", "/tarefa"})
public class TarefaController {

    private final TarefaService service;

    @PostMapping
    public ResponseEntity<TarefaResponse> cadastrarTarefa(@RequestBody @Valid TarefaCadastroRequest request) {
        TarefaResponse criada = service.cadastrarTarefa(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public List<TarefaResponse> listarTodos(
            @RequestParam(required = false) StatusTarefa status,
            @RequestParam(required = false) PrioridadeTarefa prioridade,
            @RequestParam(required = false) Long projetoId,
            @RequestParam(name = "projeto", required = false) Long projeto,
            @RequestParam(required = false) Long responsavelId
    ) {
        Long idDoProjeto = projetoId != null ? projetoId : projeto;
        return service.listarTarefasCadastradas(status, prioridade, idDoProjeto, responsavelId);
    }

    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizarTarefa(@PathVariable Long id,
                                          @RequestBody @Valid TarefaCadastroRequest request) {
        return service.atualizarTarefa(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirTarefa(@PathVariable Long id) {
        service.excluirTarefa(id);
    }
}
