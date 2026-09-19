package posweb.tarefas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_responsavel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String nome;

    @Column(length = 120, nullable = false)
    private String email;

    @OneToMany(mappedBy = "responsavel")
    private List<Tarefa> tarefas = new ArrayList<>();

    public Responsavel(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}
