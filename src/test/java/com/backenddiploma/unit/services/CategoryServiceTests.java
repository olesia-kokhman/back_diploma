package com.backenddiploma.unit.services;
import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.CategoryResponseDTO;
import com.backenddiploma.dto.category.CategoryUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.CategoryMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.CategoryService;
import com.backenddiploma.services.integrations.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    // === getCategoryByMcc ===

    @Test
    void whenGetCategoryByMcc_thenReturnCategory() {
        Category category = new Category();

        when(categoryRepository.findByMccRangeAndUserId(1234, 1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryByMcc(1234, BudgetType.EXPENSE, 1L);

        assertNotNull(result);
    }

    @Test
    void whenGetCategoryByMcc_fallbackToDefault_thenReturnDefault() {
        Category defaultCategory = new Category();

        when(categoryRepository.findByMccRangeAndUserId(1234, 1L)).thenReturn(Optional.empty());
        when(categoryRepository.findByNameAndUserIdAndType("Uncategorized", 1L, BudgetType.EXPENSE))
                .thenReturn(Optional.of(defaultCategory));

        Category result = categoryService.getCategoryByMcc(1234, BudgetType.EXPENSE, 1L);

        assertNotNull(result);
    }

    @Test
    void whenGetCategoryByMcc_defaultNotFound_thenThrow() {
        when(categoryRepository.findByMccRangeAndUserId(1234, 1L)).thenReturn(Optional.empty());
        when(categoryRepository.findByNameAndUserIdAndType("Uncategorized", 1L, BudgetType.EXPENSE))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryByMcc(1234, BudgetType.EXPENSE, 1L));
    }

    // === getCategoryId ===

    @Test
    void whenGetCategoryId_thenReturnId() {
        Category category = new Category();
        category.setId(10L);

        when(categoryRepository.findByNameAndUserIdAndType("Test", 1L, BudgetType.EXPENSE))
                .thenReturn(Optional.of(category));

        Long result = categoryService.getCategoryId("Test", 1L, BudgetType.EXPENSE);

        assertEquals(10L, result);
    }

    @Test
    void whenGetCategoryId_notFound_thenThrow() {
        when(categoryRepository.findByNameAndUserIdAndType("Test", 1L, BudgetType.EXPENSE))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryId("Test", 1L, BudgetType.EXPENSE));
    }

    // === create ===

    @Test
    void whenCreateCategory_withFile_thenSaveAndReturn() {
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        dto.setFile(file);

        Category category = new Category();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryMapper.toEntity(dto, user)).thenReturn(category);
        when(cloudinaryService.uploadFile(file, anyString())).thenReturn("imageUrl");
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO result = categoryService.create(dto);

        assertNotNull(result);
        verify(cloudinaryService).uploadFile(file, anyString());
        verify(categoryRepository).save(category);
    }

    @Test
    void whenCreateCategory_withIconUrl_thenSaveAndReturn() {
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setUserId(1L);
        dto.setIconUrl("iconUrl");

        User user = new User();
        user.setId(1L);

        Category category = new Category();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryMapper.toEntity(dto, user)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO result = categoryService.create(dto);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void whenCreateCategory_userNotFound_thenThrow() {
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.create(dto));
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnCategory() {
        Category category = new Category();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO result = categoryService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getById(1L));
    }

    // === update ===

    @Test
    void whenUpdateCategory_thenSaveAndReturn() {
        Category category = new Category();
        category.setDefault(false);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO result = categoryService.update(1L, dto);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void whenUpdateDefaultCategory_onlyColorAllowed_thenSaveAndReturn() {
        Category category = new Category();
        category.setDefault(true);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setColor("blue");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO result = categoryService.update(1L, dto);

        assertNotNull(result);
    }

    @Test
    void whenUpdateDefaultCategory_invalidField_thenThrow() {
        Category category = new Category();
        category.setDefault(true);

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setName("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> categoryService.update(1L, dto));
    }

    @Test
    void whenUpdateCategory_notFound_thenThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.update(1L, new CategoryUpdateDTO()));
    }

    // === delete ===

    @Test
    void whenDeleteCategory_thenDelete() {
        Category category = new Category();
        category.setDefault(false);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void whenDeleteDefaultCategory_thenThrow() {
        Category category = new Category();
        category.setDefault(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> categoryService.delete(1L));
    }

    @Test
    void whenDeleteCategory_notFound_thenThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.delete(1L));
    }

    // === getAllByUser ===

    @Test
    void whenGetAllByUser_thenReturnList() {
        Category cat1 = new Category();
        Category cat2 = new Category();

        when(categoryRepository.findAllByUserId(1L)).thenReturn(List.of(cat1, cat2));
        when(categoryMapper.toResponse(cat1)).thenReturn(new CategoryResponseDTO());
        when(categoryMapper.toResponse(cat2)).thenReturn(new CategoryResponseDTO());

        List<CategoryResponseDTO> result = categoryService.getAllByUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
