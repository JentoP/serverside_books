package be.thomasmore.bookserver.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthorDTO {
    private int id;
    @NotBlank(message = "Author name should not be blank")
    private String name;
}
