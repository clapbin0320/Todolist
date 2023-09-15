package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.TodoRequest;
import web.todolist.dto.response.TodoResponse;
import web.todolist.service.TodoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
@PreAuthorize("isAuthenticated()")
public class TodoController {

    private final TodoService todoService;

    /**
     * NAME : 할일 등록
     * DATE : 2023-09-06
     */
    @PostMapping("")
    public BaseResponse<TodoResponse.Register> registerTodo(@RequestBody TodoRequest.Register request) {
        return BaseResponse.success(BaseResponseStatus.CREATED, todoService.registerTodo(request));
    }

    /**
     * NAME : 할일 목록 조회
     * DATE : 2023-09-06
     */
    @GetMapping("")
    public BaseResponse<List<TodoResponse.Info>> getTodoList(@RequestBody TodoRequest.Info request) {
        return BaseResponse.success(BaseResponseStatus.OK, todoService.getTodolist(request));
    }

    /**
     * NAME : 할일 수정
     * DATE : 2023-09-06
     */
    @PatchMapping("/{id}")
    public BaseResponse<?> updateTodo(@PathVariable("id") Long id,
                                      @RequestBody TodoRequest.Update request) {
        todoService.updateTodo(id, request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 할일 상태 변경 (완료/미완료)
     * DATE : 2023-09-06
     */
    @PatchMapping("/state/{id}")
    public BaseResponse<?> updateTodo(@PathVariable("id") Long id) {
        todoService.updateState(id);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 할일 삭제
     * DATE : 2023-09-06
     */
    @DeleteMapping("/{id}")
    public BaseResponse<?> deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return BaseResponse.success(BaseResponseStatus.OK);
    }
}
