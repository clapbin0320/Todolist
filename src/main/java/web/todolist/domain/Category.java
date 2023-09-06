package web.todolist.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String categoryName;

    String categoryColor;

    @OneToMany(mappedBy = "category")
    List<Todo> todoList = new ArrayList<>();

    public void changeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void changeCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}
