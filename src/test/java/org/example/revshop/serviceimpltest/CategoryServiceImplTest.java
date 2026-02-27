package org.example.revshop.serviceimpltest;


import org.example.revshop.model.Category;
import org.example.revshop.repos.CategoryRepository;
import org.example.revshop.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    // ✅ Test getCategories()
    @Test
    void testGetCategories() {

        Category c1 = new Category();
        c1.setCategoryId(1L);
        c1.setCategoryName("Electronics");

        Category c2 = new Category();
        c2.setCategoryId(2L);
        c2.setCategoryName("Clothing");

        when(categoryRepository.findAll())
                .thenReturn(List.of(c1, c2));

        List<Category> result = categoryService.getCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());

        verify(categoryRepository, times(1)).findAll();
    }

    // ✅ Test when no categories exist
    @Test
    void testGetCategories_Empty() {

        when(categoryRepository.findAll())
                .thenReturn(List.of());

        List<Category> result = categoryService.getCategories();

        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
    }
}