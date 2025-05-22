package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.mappers.UserMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.UserRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("User with email already exists: " + dto.getEmail());
        }

        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);

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
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
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


