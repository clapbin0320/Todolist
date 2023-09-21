package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.MemoRequest;
import web.todolist.dto.response.MemoResponse;
import web.todolist.service.MemoService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/memo")
@PreAuthorize("isAuthenticated()")
public class MemoController {

    private final MemoService memoService;

    /**
     * NAME : 메모 등록
     * DATE : 2023-09-21
     */
    @PostMapping("")
    public BaseResponse<MemoResponse.Register> registerMemo(@Valid @RequestBody MemoRequest.Register request) {
        return BaseResponse.success(BaseResponseStatus.CREATED, memoService.registerMemo(request));
    }

    /**
     * NAME : 메모 목록 조회
     * DATE : 2023-09-21
     */
    @GetMapping("")
    public BaseResponse<List<MemoResponse.Info>> getMemoList() {
        return BaseResponse.success(BaseResponseStatus.OK, memoService.getMemoList());
    }

    /**
     * NAME : 메모 상세 조회
     * DATE : 2023-09-21
     */
    @GetMapping("/{id}")
    public BaseResponse<MemoResponse.Detail> getMemoDetail(@PathVariable("id") Long id) {
        return BaseResponse.success(BaseResponseStatus.OK, memoService.getMemoDetail(id));
    }

    /**
     * NAME : 메모 수정
     * DATE : 2023-09-21
     */
    @PatchMapping("/{id}")
    public BaseResponse<?> updateMemo(@PathVariable("id") Long id,
                                      @Valid @RequestBody MemoRequest.Update request) {
        memoService.updateMemo(id, request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 메모 삭제
     * DATE : 2023-09-21
     */
    @DeleteMapping("/{id}")
    public BaseResponse<?> deleteMemo(@PathVariable("id") Long id) {
        memoService.deleteMemo(id);
        return BaseResponse.success(BaseResponseStatus.OK);
    }
}
