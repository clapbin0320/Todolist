package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.CategoryRequest;
import web.todolist.dto.response.CategoryResponse;
import web.todolist.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@PreAuthorize("isAuthenticated()")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * NAME : 카테고리 등록
     * DATE : 2023-09-06
     */
    @PostMapping("")
    public BaseResponse<CategoryResponse.Register> registerCategory(@Valid @RequestBody CategoryRequest.Register request) {
        return BaseResponse.success(BaseResponseStatus.CREATED, categoryService.registerCategory(request));
    }

    /**
     * NAME : 카테고리 조회
     * DATE : 2023-09-06
     */
    @GetMapping("")
    public BaseResponse<List<CategoryResponse.Info>> getCategoryList() {
        return BaseResponse.success(BaseResponseStatus.OK, categoryService.getCategoryList());
    }

    /**
     * NAME : 카테고리 수정
     * DATE : 2023-09-06
     */
    @PatchMapping("/{id}")
    public BaseResponse<?> updateCategory(@PathVariable("id") Long id,
                                          @Valid @RequestBody CategoryRequest.Update request) {
        categoryService.updateCategory(id, request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 카테고리 삭제
     * DATE : 2023-09-06
     */
    @DeleteMapping("/{id}")
    public BaseResponse<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return BaseResponse.success(BaseResponseStatus.OK);
    }
}
