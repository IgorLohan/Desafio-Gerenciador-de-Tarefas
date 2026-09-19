package posweb.tarefas.responsavel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResponsavelCadastroRequest(
        @NotBlank @Size(max = 80) String nome,
        @NotBlank @Email @Size(max = 120) String email
) {
}
