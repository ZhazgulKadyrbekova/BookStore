package kg.PerfectJob.BookStore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "images")
public class Media extends Base {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "format", nullable = false)
    private String format;
    @Column(name = "url", nullable = false)
    private String url;
}