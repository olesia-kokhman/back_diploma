package com.backenddiploma.services;

import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.CategoryResponseDTO;
import com.backenddiploma.dto.category.CategoryUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.CategoryMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponseDTO create(CategoryCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        Category category = categoryMapper.toEntity(dto, user);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryUpdateDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

        categoryMapper.updateCategoryFromDto(category, dto);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllByUser(Long userId) {
        List<Category> categories = categoryRepository.findAllByUserId(userId);
        return categories.stream().map(categoryMapper::toResponse).collect(Collectors.toList());
    }
}
