package posweb.tarefas.tarefa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import posweb.tarefas.comum.RecursoNaoEncontradoException;
import posweb.tarefas.projeto.Projeto;
import posweb.tarefas.projeto.ProjetoRepository;
import posweb.tarefas.responsavel.Responsavel;
import posweb.tarefas.responsavel.ResponsavelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository repository;
    private final ProjetoRepository projetoRepository;
    private final ResponsavelRepository responsavelRepository;

    @Override
    public TarefaResponse cadastrarTarefa(TarefaCadastroRequest request) {
        Tarefa paraSalvar = new Tarefa(
                request.titulo(),
                request.descricao(),
                request.status(),
                request.prioridade(),
                request.prazo(),
                buscarProjeto(request.projetoId()),
                buscarResponsavelOpcional(request.responsavelId())
        );
        return mapeamentoManualEntidadeParaResponse(repository.save(paraSalvar));
    }

    @Override
    public List<TarefaResponse> listarTarefasCadastradas(StatusTarefa status,
                                                        PrioridadeTarefa prioridade,
                                                        Long projetoId,
                                                        Long responsavelId) {
        return repository.buscarComFiltros(status, prioridade, projetoId, responsavelId).stream()
                .map(this::mapeamentoManualEntidadeParaResponse)
                .toList();
    }

    @Override
    public TarefaResponse buscarPorId(Long id) {
        return mapeamentoManualEntidadeParaResponse(buscarEntidade(id));
    }

    @Override
    public TarefaResponse atualizarTarefa(Long id, TarefaCadastroRequest request) {
        Tarefa tarefa = buscarEntidade(id);
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setStatus(request.status());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setPrazo(request.prazo());
        tarefa.setProjeto(buscarProjeto(request.projetoId()));
        tarefa.setResponsavel(buscarResponsavelOpcional(request.responsavelId()));
        return mapeamentoManualEntidadeParaResponse(repository.save(tarefa));
    }

    @Override
    public void excluirTarefa(Long id) {
        Tarefa tarefa = buscarEntidade(id);
        repository.delete(tarefa);
    }

    private Tarefa buscarEntidade(Long id) {
        return repository.findByIdComRelacionamentos(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
    }

    private Projeto buscarProjeto(Long projetoId) {
        return projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto não encontrado"));
    }

    private Responsavel buscarResponsavelOpcional(Long responsavelId) {
        if (responsavelId == null) {
            return null;
        }
        return responsavelRepository.findById(responsavelId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável não encontrado"));
    }

    private TarefaResponse mapeamentoManualEntidadeParaResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getPrazo(),
                tarefa.getCriadaEm(),
                tarefa.getConcluidaEm(),
                tarefa.getProjeto().getId(),
                tarefa.getProjeto().getNome(),
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getId() : null,
                tarefa.getResponsavel() != null ? tarefa.getResponsavel().getNome() : null
        );
    }
}
