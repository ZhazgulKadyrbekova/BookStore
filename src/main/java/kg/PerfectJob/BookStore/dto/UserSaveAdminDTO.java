package kg.PerfectJob.BookStore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserSaveAdminDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
}
