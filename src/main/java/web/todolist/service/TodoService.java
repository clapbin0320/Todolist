package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.Category;
import web.todolist.domain.Todo;
import web.todolist.domain.User;
import web.todolist.dto.request.TodoRequest;
import web.todolist.dto.response.TodoResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.CategoryRepository;
import web.todolist.repository.TodoRepository;
import web.todolist.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 할일 등록
     */
    @Transactional
    public TodoResponse.Register registerTodo(TodoRequest.Register request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        Category category = null;
        if (request.getCategory() != null) {
            category = categoryRepository.findByUserAndCategoryName(user, request.getCategory())
                    .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CATEGORY));
        }

        Todo todo = Todo.builder()
                .user(user)
                .category(category)
                .date(LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE))
                .todo(request.getTodo())
                .isDone(false)
                .build();

        todoRepository.save(todo);

        return TodoResponse.Register.builder().todoId(todo.getId()).build();
    }

    /**
     * 할일 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TodoResponse.Info> getTodolist(TodoRequest.Info request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);

        List<TodoResponse.Info> responseList = new ArrayList<>();
        List<Todo> todoList = todoRepository.getTodoList(user, date);
        for (Todo todo : todoList) {
            responseList.add(TodoResponse.Info.builder()
                    .todoId(todo.getId())
                    .categoryName(todo.getCategory() == null ? null : todo.getCategory().getCategoryName())
                    .todo(todo.getTodo())
                    .isDone(todo.getIsDone())
                    .build());
        }

        return responseList;
    }

    /**
     * 할일 수정
     */
    @Transactional
    public void updateTodo(Long id, TodoRequest.Update request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TODO));

        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!todo.getUser().equals(user)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository.findByUserAndCategoryName(user, request.getCategory())
                    .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CATEGORY));
            todo.changeCategory(category);
        }

        if (request.getDate() != null) {
            LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_DATE);
            todo.changeDate(date);
        }

        if (request.getTodo() != null) {
            todo.changeTodo(request.getTodo());
        }
    }

    /**
     * 할일 상태 변경 (완료/미완료)
     */
    @Transactional
    public void updateState(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TODO));

        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!todo.getUser().equals(user)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        if (todo.getIsDone()) todo.setIsDoneFalse();
        else todo.setIsDoneTrue();
    }

    /**
     * 할일 삭제
     */
    @Transactional
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TODO));

        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!todo.getUser().equals(user)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        todoRepository.delete(todo);
    }
}
