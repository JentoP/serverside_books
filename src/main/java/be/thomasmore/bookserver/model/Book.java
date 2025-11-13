package be.thomasmore.bookserver.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"authors", "serie"})
@ToString(exclude = {"authors", "serie"})
@Entity
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @NotBlank(message = "Book Title should not be blank")
    @NotNull
    private String title;

    @Column(length=1024)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id")
    private Serie serie;

    @Column(name = "number_in_serie")
    private Integer numberInSerie;
}

