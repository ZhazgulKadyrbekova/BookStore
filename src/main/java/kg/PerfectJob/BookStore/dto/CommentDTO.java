package kg.PerfectJob.BookStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommentDTO {
    private double rating;
    private String description;
}
