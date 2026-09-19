package posweb.tarefas.responsavel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    private final ResponsavelService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsavelResponse cadastrarResponsavel(@RequestBody @Valid ResponsavelCadastroRequest request) {
        return service.cadastrarResponsavel(request);
    }

    @GetMapping
    public List<ResponsavelResponse> listarTodos() {
        return service.listarResponsaveisCadastrados();
    }

    @GetMapping("/{id}")
    public ResponsavelResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponsavelResponse atualizarResponsavel(@PathVariable Long id,
                                                    @RequestBody @Valid ResponsavelCadastroRequest request) {
        return service.atualizarResponsavel(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirResponsavel(@PathVariable Long id) {
        service.excluirResponsavel(id);
    }
}
