package posweb.tarefas.responsavel;

import java.util.List;

public interface ResponsavelService {

    ResponsavelResponse cadastrarResponsavel(ResponsavelCadastroRequest request);

    List<ResponsavelResponse> listarResponsaveisCadastrados();

    ResponsavelResponse buscarPorId(Long id);

    ResponsavelResponse atualizarResponsavel(Long id, ResponsavelCadastroRequest request);

    void excluirResponsavel(Long id);
}
