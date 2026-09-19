package posweb.tarefas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import posweb.tarefas.domain.PrioridadeTarefa;
import posweb.tarefas.domain.StatusTarefa;

import java.time.LocalDate;

public record TarefaCadastroRequest(
        @NotBlank @Size(max = 120) String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDate prazo,
        @NotNull Long projetoId,
        Long responsavelId
) {
}
