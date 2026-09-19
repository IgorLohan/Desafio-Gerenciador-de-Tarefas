package posweb.tarefas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjetoCadastroRequest(
        @NotBlank @Size(max = 80) String nome,
        @Size(max = 255) String descricao
) {
}
