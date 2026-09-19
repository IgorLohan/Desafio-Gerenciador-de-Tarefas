package posweb.tarefas.tarefa;

import java.util.List;

public interface TarefaService {

    TarefaResponse cadastrarTarefa(TarefaCadastroRequest request);

    List<TarefaResponse> listarTarefasCadastradas(StatusTarefa status,
                                                 PrioridadeTarefa prioridade,
                                                 Long projetoId,
                                                 Long responsavelId);

    TarefaResponse buscarPorId(Long id);

    TarefaResponse atualizarTarefa(Long id, TarefaCadastroRequest request);

    void excluirTarefa(Long id);
}
