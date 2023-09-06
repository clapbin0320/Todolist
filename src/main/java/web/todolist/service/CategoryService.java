package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.Category;
import web.todolist.domain.Todo;
import web.todolist.domain.User;
import web.todolist.dto.request.CategoryRequest;
import web.todolist.dto.response.CategoryResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.CategoryRepository;
import web.todolist.repository.TodoRepository;
import web.todolist.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    /**
     * 카테고리 등록
     */
    @Transactional
    public CategoryResponse.Register registerCategory(CategoryRequest.Register request) {
        // todo: 로그인 구현 후 User 로직 변경
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        // 카테고리명 중복 확인
        categoryRepository.findByCategoryName(request.getCategoryName()).ifPresent(category -> {
            throw new CustomException(Error.ALREADY_SAVED_CATEGORY);
        });

        Category category = Category.builder()
                .user(user)
                .categoryName(request.getCategoryName())
                .categoryColor(request.getCategoryColor())
                .build();

        categoryRepository.save(category);

        return CategoryResponse.Register.builder().categoryId(category.getId()).build();
    }

    /**
     * 카테고리 조회
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse.Info> getCategoryList(Long userId) {
        // todo: 로그인 구현 후 User 로직 변경
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        List<CategoryResponse.Info> responseList = new ArrayList<>();
        List<Category> categoryList = categoryRepository.findByUser(user);
        for (Category category : categoryList) {
            responseList.add(CategoryResponse.Info.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getCategoryName())
                    .categoryColor(category.getCategoryColor())
                    .size(category.getTodoList().size())
                    .build());
        }

        return responseList;
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void updateCategory(Long id, CategoryRequest.Update request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CATEGORY));

        // todo: 로그인 구현 후 User 로직 변경
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!category.getUser().equals(user)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        if (request.getCategoryName() != null) {
            category.changeCategoryName(request.getCategoryName());
        }

        if (request.getCategoryColor() != null) {
            category.changeCategoryColor(request.getCategoryColor());
        }
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_CATEGORY));

        // todo: 로그인 구현 후 User 로직 변경
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (!category.getUser().equals(user)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        // 삭제하는 카테고리에 있는 할일들의 카테고리 null 처리
        todoRepository.findByCategory(category).forEach(Todo::setCategoryNull);

        categoryRepository.delete(category);
    }
}
