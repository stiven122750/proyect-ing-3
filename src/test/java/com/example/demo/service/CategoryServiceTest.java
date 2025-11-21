package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setup() {
        category = new Category("Electronics");
    }

    @Test
    void shouldReturnAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldReturnCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        Category updated = new Category("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = categoryService.update(1L, updated);

        assertEquals("New Name", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> categoryService.update(1L, new Category()));
    }

    @Test
    void shouldDeleteSuccessfully() {
        categoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
