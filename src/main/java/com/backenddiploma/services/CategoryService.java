package com.backenddiploma.services;

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
import com.backenddiploma.services.integrations.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

//    public CategoryResponseDTO getCategoryByMcc(int mcc, Long userId) {
//        Category category = categoryRepository.findByMccRangeAndUserId(mcc, userId)
//                .orElseThrow(() -> new NotFoundException("Category not found for userId = " + userId + ", mcc = " + mcc));
//        return categoryMapper.toResponse(category);
//    }

    public Category getCategoryByMcc(int mcc, BudgetType budgetType, Long userId) {
        Optional<Category> categoryOpt = categoryRepository
                .findByMccRangeAndUserId(mcc, userId);

        Category category;
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
        } else {
            String defaultCategoryName = "Uncategorized";
            category =  categoryRepository.findByNameAndUserIdAndType(defaultCategoryName, userId, budgetType)
                    .orElseThrow(() -> new NotFoundException("Default category not found for userId = " + userId));
        }

        return category;
    }

    public Long getCategoryId(String name, Long userId, BudgetType budgetType) {
        if(categoryRepository.findByNameAndUserIdAndType(name, userId, budgetType).isEmpty()) {
            throw new NotFoundException("category not found");
        }
        return categoryRepository.findByNameAndUserIdAndType(name, userId, budgetType).get().getId();
    }

    @Transactional
    public CategoryResponseDTO create(CategoryCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        Category category = categoryMapper.toEntity(dto, user);
        category.setDefault(false);
        MultipartFile file = dto.getFile();
        if (file != null && !file.isEmpty()) {
            String publicId = "category_icons/" + user.getId() + "_" + UUID.randomUUID();
            String imageUrl = cloudinaryService.uploadFile(file, publicId);
            category.setIconUrl(imageUrl);
        } else if (dto.getIconUrl() != null) {
            category.setIconUrl(dto.getIconUrl());
        }

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
    public CategoryResponseDTO update(Long id, CategoryUpdateDTO form) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

        if (category.isDefault()) {
            if (form.getColor() != null) {
                category.setColor(form.getColor());
            }

            if (form.getName() != null || form.getType() != null || form.getIconUrl() != null || (form.getFile() != null && !form.getFile().isEmpty())) {
                throw new RuntimeException("Only color can be changed for default category");
            }

        } else {
            if (form.getName() != null) {
                category.setName(form.getName());
            }
            if (form.getColor() != null) {
                category.setColor(form.getColor());
            }
            if (form.getType() != null) {
                category.setType(form.getType());
            }

            MultipartFile file = form.getFile();
            if (file != null && !file.isEmpty()) {
                String publicId = "category_icons/" + category.getUser().getId() + "_" + UUID.randomUUID();
                String imageUrl = cloudinaryService.uploadFile(file, publicId);
                category.setIconUrl(imageUrl);
            } else if (form.getIconUrl() != null) {
                category.setIconUrl(form.getIconUrl());
            }
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }


    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        if (category.isDefault()) {
            throw new RuntimeException("Cannot delete default category");
        }
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllByUser(Long userId) {
        List<Category> categories = categoryRepository.findAllByUserId(userId);
        return categories.stream().map(categoryMapper::toResponse).collect(Collectors.toList());
    }
}
