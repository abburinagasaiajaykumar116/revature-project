package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.model.Category;
import org.example.revshop.repos.CategoryRepository;
import org.example.revshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
