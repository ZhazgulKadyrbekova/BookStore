package kg.PerfectJob.BookStore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "books")
public class Book extends Base {
    
    @Column(name = "name")
    private String name;

    @Column(name = "average_rating")
    private float averageRating;

    @Column(name = "url")
    private String url;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany
    @JoinColumn(name = "comments")
    private List<BookComment> comments;

    @OneToOne
//    @Column(name = "image_id")
    private Image image;
}
