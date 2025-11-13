package be.thomasmore.bookserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookDetailedDTO {
    private int id;
    private String title;

    private String description;

    //more data will be inserted here later...

    private Collection<AuthorDTO> authors;

    // Serie info for detailed book view
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer serieId;     // null if book has no serie
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serieName;    // null if book has no serie
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberInSerie; // null if book has no serie
}

