package posweb.tarefas.tarefa;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    private final TarefaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse cadastrarTarefa(@RequestBody @Valid TarefaCadastroRequest request) {
        return service.cadastrarTarefa(request);
    }

    @GetMapping
    public List<TarefaResponse> listarTodos(
            @RequestParam(required = false) StatusTarefa status,
            @RequestParam(required = false) PrioridadeTarefa prioridade,
            @RequestParam(required = false) Long projetoId,
            @RequestParam(required = false) Long responsavelId
    ) {
        return service.listarTarefasCadastradas(status, prioridade, projetoId, responsavelId);
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
