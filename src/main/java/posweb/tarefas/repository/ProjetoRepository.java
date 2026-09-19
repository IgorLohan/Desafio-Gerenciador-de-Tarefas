package posweb.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posweb.tarefas.domain.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
