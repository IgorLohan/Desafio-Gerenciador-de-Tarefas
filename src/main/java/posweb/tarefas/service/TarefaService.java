package posweb.tarefas.service;

import posweb.tarefas.domain.PrioridadeTarefa;
import posweb.tarefas.domain.StatusTarefa;
import posweb.tarefas.dto.TarefaCadastroRequest;
import posweb.tarefas.dto.TarefaResponse;

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
