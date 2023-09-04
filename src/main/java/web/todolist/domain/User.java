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
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    String nickname;

    @OneToMany(mappedBy = "user")
    List<Todolist> todolist = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Category> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Diary> diaryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Memo> memoList = new ArrayList<>();
}
