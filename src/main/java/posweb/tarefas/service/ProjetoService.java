package posweb.tarefas.service;

import posweb.tarefas.dto.ProjetoCadastroRequest;
import posweb.tarefas.dto.ProjetoResponse;

import java.util.List;

public interface ProjetoService {

    ProjetoResponse cadastrarProjeto(ProjetoCadastroRequest request);

    List<ProjetoResponse> listarProjetosCadastrados();

    ProjetoResponse buscarPorId(Long id);

    ProjetoResponse atualizarProjeto(Long id, ProjetoCadastroRequest request);

    void excluirProjeto(Long id);
}
