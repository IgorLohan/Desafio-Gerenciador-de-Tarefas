package posweb.tarefas.projeto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    private final ProjetoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetoResponse cadastrarProjeto(@RequestBody @Valid ProjetoCadastroRequest request) {
        return service.cadastrarProjeto(request);
    }

    @GetMapping
    public List<ProjetoResponse> listarTodos() {
        return service.listarProjetosCadastrados();
    }

    @GetMapping("/{id}")
    public ProjetoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProjetoResponse atualizarProjeto(@PathVariable Long id,
                                            @RequestBody @Valid ProjetoCadastroRequest request) {
        return service.atualizarProjeto(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProjeto(@PathVariable Long id) {
        service.excluirProjeto(id);
    }
}
