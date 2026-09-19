package posweb.tarefas.projeto;

import java.util.List;

public interface ProjetoService {

    ProjetoResponse cadastrarProjeto(ProjetoCadastroRequest request);

    List<ProjetoResponse> listarProjetosCadastrados();

    ProjetoResponse buscarPorId(Long id);

    ProjetoResponse atualizarProjeto(Long id, ProjetoCadastroRequest request);

    void excluirProjeto(Long id);
}
