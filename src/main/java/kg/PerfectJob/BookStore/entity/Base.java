package kg.PerfectJob.BookStore.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class Base {
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @PrePersist
    public void onPrePersist() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }
}
