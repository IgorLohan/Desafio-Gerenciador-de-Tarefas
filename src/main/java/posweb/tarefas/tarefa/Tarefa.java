package posweb.tarefas.tarefa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import posweb.tarefas.projeto.Projeto;
import posweb.tarefas.responsavel.Responsavel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private PrioridadeTarefa prioridade;

    private LocalDate prazo;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @Column(name = "concluida_em")
    private LocalDateTime concluidaEm;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Responsavel responsavel;

    Tarefa(String titulo, String descricao, StatusTarefa status, PrioridadeTarefa prioridade,
           LocalDate prazo, Projeto projeto, Responsavel responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.prazo = prazo;
        this.projeto = projeto;
        this.responsavel = responsavel;
    }

    @PrePersist
    void prePersist() {
        if (criadaEm == null) {
            criadaEm = LocalDateTime.now();
        }
        if (status == null) {
            status = StatusTarefa.PENDENTE;
        }
        if (prioridade == null) {
            prioridade = PrioridadeTarefa.MEDIA;
        }
        atualizarConclusao();
    }

    @PreUpdate
    void preUpdate() {
        atualizarConclusao();
    }

    void atualizarConclusao() {
        if (status == StatusTarefa.CONCLUIDA) {
            if (concluidaEm == null) {
                concluidaEm = LocalDateTime.now();
            }
        } else {
            concluidaEm = null;
        }
    }
}
