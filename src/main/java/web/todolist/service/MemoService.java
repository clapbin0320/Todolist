package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.Memo;
import web.todolist.domain.User;
import web.todolist.dto.request.MemoRequest;
import web.todolist.dto.response.MemoResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.MemoRepository;
import web.todolist.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;

    /**
     * 메모 등록
     */
    @Transactional
    public MemoResponse.Register registerMemo(MemoRequest.Register request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Memo memo = Memo.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        memoRepository.save(memo);

        return MemoResponse.Register.builder().memoId(memo.getId()).build();
    }

    /**
     * 메모 목록 조회
     */
    @Transactional(readOnly = true)
    public List<MemoResponse.Info> getMemoList() {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        List<Memo> memoList = memoRepository.findByUser(user);
        List<MemoResponse.Info> memoInfoList = new ArrayList<>();
        for (Memo memo : memoList) {
            MemoResponse.Info memoInfo = MemoResponse.Info.builder()
                    .memoId(memo.getId())
                    .title(memo.getTitle() == null ? "제목없음" : memo.getTitle())
                    .updatedAt(memo.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
            memoInfoList.add(memoInfo);
        }

        return memoInfoList;
    }

    /**
     * 메모 상세 조회
     */
    @Transactional(readOnly = true)
    public MemoResponse.Detail getMemoDetail(Long id) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_MEMO));

        if (!user.equals(memo.getUser())) {
            throw new CustomException(Error.FORBIDDEN);
        }

        return MemoResponse.Detail.builder()
                .memoId(memo.getId())
                .title(memo.getTitle() == null ? "제목없음" : memo.getTitle())
                .content(memo.getContent())
                .updatedAt(memo.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

    /**
     * 메모 수정
     */
    @Transactional
    public void updateMemo(Long id, MemoRequest.Update request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_MEMO));

        if (!user.equals(memo.getUser())) {
            throw new CustomException(Error.FORBIDDEN);
        }

        if (request.getTitle() != null) {
            memo.changeTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            memo.changeContent(request.getContent());
        }

        memoRepository.save(memo);
    }

    /**
     * 메모 삭제
     */
    @Transactional
    public void deleteMemo(Long id) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_MEMO));

        if (!user.equals(memo.getUser())) {
            throw new CustomException(Error.FORBIDDEN);
        }

        memoRepository.delete(memo);
    }
}
