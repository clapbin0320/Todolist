package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.DiaryRequest;
import web.todolist.dto.response.DiaryResponse;
import web.todolist.service.DiaryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diary")
@PreAuthorize("isAuthenticated()")
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * NAME : 일기 등록
     * DATE : 2023-09-21
     */
    @PostMapping("")
    public BaseResponse<DiaryResponse.Register> registerDiary(@Valid @RequestBody DiaryRequest.Register request) {
        return BaseResponse.success(BaseResponseStatus.CREATED, diaryService.registerDiary(request));
    }

    /**
     * NAME : 일기 조회
     * DATE : 2023-09-21
     */
    @GetMapping("")
    public BaseResponse<DiaryResponse.Info> getDiary(@Valid @RequestBody DiaryRequest.Info request) {
        return BaseResponse.success(BaseResponseStatus.OK, diaryService.getDiary(request));
    }

    /**
     * NAME : 일기 수정
     * DATE : 2023-09-21
     */
    @PatchMapping("/{id}")
    public BaseResponse<?> updateDiary(@PathVariable("id") Long id,
                                       @Valid @RequestBody DiaryRequest.Update request) {
        diaryService.updateDiary(id, request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 일기 삭제
     * DATE : 2023-09-21
     */
    @DeleteMapping("/{id}")
    public BaseResponse<?> deleteDiary(@PathVariable("id") Long id) {
        diaryService.deleteDiary(id);
        return BaseResponse.success(BaseResponseStatus.OK);
    }
}
