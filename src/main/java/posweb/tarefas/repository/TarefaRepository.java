package posweb.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import posweb.tarefas.domain.PrioridadeTarefa;
import posweb.tarefas.domain.StatusTarefa;
import posweb.tarefas.domain.Tarefa;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    boolean existsByProjetoId(Long projetoId);

    boolean existsByResponsavelId(Long responsavelId);

    List<Tarefa> findByStatus(StatusTarefa status);

    List<Tarefa> findByProjetoId(Long projetoId);

    @Query("""
            SELECT DISTINCT t FROM Tarefa t
            JOIN FETCH t.projeto
            LEFT JOIN FETCH t.responsavel
            WHERE (:status IS NULL OR t.status = :status)
              AND (:prioridade IS NULL OR t.prioridade = :prioridade)
              AND (:projetoId IS NULL OR t.projeto.id = :projetoId)
              AND (:responsavelId IS NULL OR t.responsavel.id = :responsavelId)
            """)
    List<Tarefa> buscarComFiltros(
            @Param("status") StatusTarefa status,
            @Param("prioridade") PrioridadeTarefa prioridade,
            @Param("projetoId") Long projetoId,
            @Param("responsavelId") Long responsavelId
    );

    @Query("""
            SELECT t FROM Tarefa t
            JOIN FETCH t.projeto
            LEFT JOIN FETCH t.responsavel
            WHERE t.id = :id
            """)
    Optional<Tarefa> findByIdComRelacionamentos(@Param("id") Long id);
}
