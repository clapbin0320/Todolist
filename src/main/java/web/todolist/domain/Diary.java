package web.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
    ZonedDateTime date;

    String title;

    String content;
}
