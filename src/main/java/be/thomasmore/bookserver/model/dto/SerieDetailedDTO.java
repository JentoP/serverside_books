package be.thomasmore.bookserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class SerieDetailedDTO {
    private int id;
    private String name;
}
