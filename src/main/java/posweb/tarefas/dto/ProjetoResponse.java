package posweb.tarefas.dto;

import java.time.LocalDateTime;

public record ProjetoResponse(Long id, String nome, String descricao, LocalDateTime criadoEm) {
}
