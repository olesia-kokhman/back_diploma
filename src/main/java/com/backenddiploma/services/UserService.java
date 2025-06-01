package com.backenddiploma.services;

import com.backenddiploma.config.CloudinaryConfig;
import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.mappers.UserMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;

import com.backenddiploma.services.integrations.CloudinaryService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//import com.backenddiploma.security.UserDetailsImpl;
//import org.springframework.core.userdetails.UserDetails;
//import org.springframework.core.userdetails.UserDetailsService;
//import org.springframework.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                String.format("User '%s' not found", username)));
//
//        return UserDetailsImpl.build(user);
//    }

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DefaultCategoryLoader defaultCategoryLoader;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("User with email already exists: " + dto.getEmail());
        }

        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);

        List<Category> defaultCategories = defaultCategoryLoader.loadDefaultCategoriesForUser(savedUser);
        categoryRepository.saveAll(defaultCategories);
        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new AlreadyExistsException("User with email already exists: " + dto.getEmail());
            }
        }

        userMapper.updateUserFromDto(user, dto);
        MultipartFile file = dto.getFile();
        if (file != null && !file.isEmpty()) {

            if (user.getProfilePicturePublicId() != null) {
                cloudinaryService.deleteFile(user.getProfilePicturePublicId());
            }

            String publicId = "user_avatars/" + id + "_" + UUID.randomUUID();
            String imageUrl = cloudinaryService.uploadFile(file, publicId);
            user.setProfilePictureUrl(imageUrl);
            user.setProfilePicturePublicId(publicId);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (user.getProfilePicturePublicId() != null) {
            cloudinaryService.deleteFile(user.getProfilePicturePublicId());
            user.setProfilePicturePublicId(null);
            user.setProfilePictureUrl(null);
            userRepository.save(user);
        }
    }


    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

}


