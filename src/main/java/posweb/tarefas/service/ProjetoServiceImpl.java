package posweb.tarefas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import posweb.tarefas.domain.Projeto;
import posweb.tarefas.domain.RecursoNaoEncontradoException;
import posweb.tarefas.dto.ProjetoCadastroRequest;
import posweb.tarefas.dto.ProjetoResponse;
import posweb.tarefas.repository.ProjetoRepository;
import posweb.tarefas.repository.TarefaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository repository;
    private final TarefaRepository tarefaRepository;

    @Override
    public ProjetoResponse cadastrarProjeto(ProjetoCadastroRequest request) {
        Projeto paraSalvar = new Projeto(request.nome(), request.descricao());
        return mapeamentoManualEntidadeParaResponse(repository.save(paraSalvar));
    }

    @Override
    public List<ProjetoResponse> listarProjetosCadastrados() {
        return repository.findAll().stream()
                .map(this::mapeamentoManualEntidadeParaResponse)
                .toList();
    }

    @Override
    public ProjetoResponse buscarPorId(Long id) {
        return mapeamentoManualEntidadeParaResponse(buscarEntidade(id));
    }

    @Override
    public ProjetoResponse atualizarProjeto(Long id, ProjetoCadastroRequest request) {
        Projeto projeto = buscarEntidade(id);
        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        return mapeamentoManualEntidadeParaResponse(repository.save(projeto));
    }

    @Override
    public void excluirProjeto(Long id) {
        Projeto projeto = buscarEntidade(id);
        if (tarefaRepository.existsByProjetoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir um projeto que ainda possui tarefas"
            );
        }
        repository.delete(projeto);
    }

    private Projeto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado"));
    }

    private ProjetoResponse mapeamentoManualEntidadeParaResponse(Projeto projeto) {
        return new ProjetoResponse(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getCriadoEm()
        );
    }
}
