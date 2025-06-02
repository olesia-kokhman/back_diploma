package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.dto.security.RegisterRequestDTO;
import com.backenddiploma.dto.security.LoginRequestDTO;
import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.mappers.UserMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.security.JwtCore;
import com.backenddiploma.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtCore jwtCore;
    private final DefaultCategoryLoader defaultCategoryLoader;
    private final CategoryRepository categoryRepository;

    @Transactional
    public String register(RegisterRequestDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Such email is already registered. Please use another one");
        }

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(request.getUsername());
        userCreateDTO.setRole(request.getRole());
        userCreateDTO.setEmail(request.getEmail());
        userCreateDTO.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User user = userMapper.toEntity(userCreateDTO);
        userRepository.save(user);
        List<Category> defaultCategories = defaultCategoryLoader.loadDefaultCategoriesForUser(user);
        categoryRepository.saveAll(defaultCategories);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return jwtCore.generateToken(userDetails);
    }

    public String authenticate(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwtCore.generateToken(userDetails);
    }
}
