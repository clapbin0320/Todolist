package web.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Todo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate date;

    String todo;

    Boolean isDone; // 완료 여부

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeDate(LocalDate date) {
        this.date = date;
    }

    public void changeTodo(String todo) {
        this.todo = todo;
    }

    public void setIsDoneTrue() {
        this.isDone = true;
    }

    public void setIsDoneFalse() {
        this.isDone = false;
    }

    public void setCategoryNull() {
        this.category = null;
    }
}
