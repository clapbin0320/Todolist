package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.Diary;
import web.todolist.domain.User;
import web.todolist.dto.request.DiaryRequest;
import web.todolist.dto.response.DiaryResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.DiaryRepository;
import web.todolist.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    /**
     * 일기 등록
     */
    @Transactional
    public DiaryResponse.Register registerDiary(DiaryRequest.Register request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
        diaryRepository.findByUserAndDate(user, date).ifPresent(diary -> {
            throw new CustomException(Error.ALREADY_SAVED_DIARY);
        });

        Diary diary = Diary.builder()
                .user(user)
                .date(date)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        diaryRepository.save(diary);

        return DiaryResponse.Register.builder().diaryId(diary.getId()).build();
    }

    /**
     * 일기 조회
     */
    @Transactional(readOnly = true)
    public DiaryResponse.Info getDiary(DiaryRequest.Info request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);

        Diary diary = diaryRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_DIARY));

        return DiaryResponse.Info.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .build();
    }

    /**
     * 일기 수정
     */
    @Transactional
    public void updateDiary(Long id, DiaryRequest.Update request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_DIARY));

        if (!user.equals(diary.getUser())) {
            throw new CustomException(Error.FORBIDDEN);
        }

        if (request.getDate() != null) {
            diary.changeDate(LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE));
        }

        if (request.getTitle() != null) {
            diary.changeTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            diary.changeContent(request.getContent());
        }

        diaryRepository.save(diary);
    }

    /**
     * 일기 삭제
     */
    @Transactional
    public void deleteDiary(Long id) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_DIARY));

        if (!user.equals(diary.getUser())) {
            throw new CustomException(Error.FORBIDDEN);
        }

        diaryRepository.delete(diary);
    }
}
