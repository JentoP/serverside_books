package be.thomasmore.bookserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieDetailedDTOBookItem {
    private int id;
    private String title;
    private Integer numberInSerie;
}
