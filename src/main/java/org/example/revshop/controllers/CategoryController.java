package org.example.revshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.revshop.model.Category;
import org.example.revshop.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }
}
