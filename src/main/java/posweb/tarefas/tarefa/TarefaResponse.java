package posweb.tarefas.tarefa;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDate prazo,
        LocalDateTime criadaEm,
        LocalDateTime concluidaEm,
        Long projetoId,
        String projetoNome,
        Long responsavelId,
        String responsavelNome
) {
}
