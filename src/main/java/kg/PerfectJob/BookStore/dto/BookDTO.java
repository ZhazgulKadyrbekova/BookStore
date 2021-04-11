package kg.PerfectJob.BookStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookDTO {
    private String name;
    private long authorID;
    private long categoryID;
}
