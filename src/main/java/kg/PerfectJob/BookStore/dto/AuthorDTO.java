package kg.PerfectJob.BookStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthorDTO {
    private String name;
    private String type;
    private LocalDate birthDate;
    private String biography;
}
