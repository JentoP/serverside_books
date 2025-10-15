package be.thomasmore.bookserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a Serie entity in the database.
 * This class is mapped to a database table using JPA annotations.
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "SERIES")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Serie name should not be blank")
    @Column(unique = true)
    private String name;

    public Serie(String name) {
        this.name = name;
    }
}
