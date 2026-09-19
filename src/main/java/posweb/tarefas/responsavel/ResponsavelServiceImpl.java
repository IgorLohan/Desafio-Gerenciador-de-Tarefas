package posweb.tarefas.responsavel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import posweb.tarefas.comum.RecursoNaoEncontradoException;
import posweb.tarefas.tarefa.TarefaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsavelServiceImpl implements ResponsavelService {

    private final ResponsavelRepository repository;
    private final TarefaRepository tarefaRepository;

    @Override
    public ResponsavelResponse cadastrarResponsavel(ResponsavelCadastroRequest request) {
        Responsavel paraSalvar = new Responsavel(request.nome(), request.email());
        return mapeamentoManualEntidadeParaResponse(repository.save(paraSalvar));
    }

    @Override
    public List<ResponsavelResponse> listarResponsaveisCadastrados() {
        return repository.findAll().stream()
                .map(this::mapeamentoManualEntidadeParaResponse)
                .toList();
    }

    @Override
    public ResponsavelResponse buscarPorId(Long id) {
        return mapeamentoManualEntidadeParaResponse(buscarEntidade(id));
    }

    @Override
    public ResponsavelResponse atualizarResponsavel(Long id, ResponsavelCadastroRequest request) {
        Responsavel responsavel = buscarEntidade(id);
        responsavel.setNome(request.nome());
        responsavel.setEmail(request.email());
        return mapeamentoManualEntidadeParaResponse(repository.save(responsavel));
    }

    @Override
    public void excluirResponsavel(Long id) {
        Responsavel responsavel = buscarEntidade(id);
        if (tarefaRepository.existsByResponsavelId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir um responsável que ainda possui tarefas"
            );
        }
        repository.delete(responsavel);
    }

    private Responsavel buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável não encontrado"));
    }

    private ResponsavelResponse mapeamentoManualEntidadeParaResponse(Responsavel responsavel) {
        return new ResponsavelResponse(responsavel.getId(), responsavel.getNome(), responsavel.getEmail());
    }
}
