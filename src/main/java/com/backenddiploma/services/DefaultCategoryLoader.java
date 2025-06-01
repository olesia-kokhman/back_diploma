package com.backenddiploma.services;

import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.DefaultCategoryCreateDTO;
import com.backenddiploma.mappers.CategoryMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.BudgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DefaultCategoryLoader {

    private final CategoryMapper categoryMapper;

    public List<Category> loadDefaultCategoriesForUser(User user) {
        List<Category> categories = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("db/seeds/default_categories_seed.csv"))))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] tokens = line.split(",");
                String name = tokens[0].trim();
                String nameUK = tokens[1].trim();
                BudgetType type = BudgetType.valueOf(tokens[2].trim());
                Integer startMcc = tokens[3].isBlank() ? null : Integer.parseInt(tokens[3].trim());
                Integer endMcc = tokens[4].isBlank() ? null : Integer.parseInt(tokens[4].trim());
                String color = tokens[5].trim();
                String iconUrl = tokens.length > 6 ? tokens[6].trim() : null;

                DefaultCategoryCreateDTO categoryCreateDTO = new DefaultCategoryCreateDTO();
                categoryCreateDTO.setUserId(user.getId());
                categoryCreateDTO.setName(name);
                categoryCreateDTO.setNameUK(nameUK);
                categoryCreateDTO.setType(type);
                categoryCreateDTO.setStartMcc(startMcc);
                categoryCreateDTO.setEndMcc(endMcc);
                categoryCreateDTO.setColor(color);
                categoryCreateDTO.setIconUrl(iconUrl);
                categoryCreateDTO.setDefault(true);
                categories.add(categoryMapper.defaultToEntity(categoryCreateDTO, user));
            }

        } catch (Exception e) {
            System.out.println("Error reading default_categories.csv: " + e.getMessage());
            throw new RuntimeException("Failed to load default categories", e);
        }
        return categories;
    }
}
