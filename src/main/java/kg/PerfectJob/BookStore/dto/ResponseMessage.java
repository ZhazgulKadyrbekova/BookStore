package kg.PerfectJob.BookStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResponseMessage {
    private String code;
    private ResponseEntity<?> body;
}
