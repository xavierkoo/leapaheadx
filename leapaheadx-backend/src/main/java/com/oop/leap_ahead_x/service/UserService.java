package com.oop.leap_ahead_x.service;

import com.oop.leap_ahead_x.domain.User;
import com.oop.leap_ahead_x.dto.UserDTO;
import com.oop.leap_ahead_x.repos.UserRepository;
import com.oop.leap_ahead_x.exceptions.NotFoundException;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("uId"));
        return users.stream()
                .map((user) -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID uId) {
        return userRepository.findById(uId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {

        final User user = new User();
        String temp = userDTO.getPassword();
        userDTO.setPassword(this.passwordEncoder.encode(temp));
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUId();
    }

    public void update(final UUID uId, final UserDTO userDTO) {
        String temp = userDTO.getPassword();
        userDTO.setPassword(this.passwordEncoder.encode(temp));
        final User user = userRepository.findById(uId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID uId) {
        userRepository.deleteById(uId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUId(user.getUId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setName(user.getName());
        userDTO.setDisabled(user.isDisabled());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setName(userDTO.getName());
        user.setDisabled(userDTO.getDisabled());
        return user;
    }

    @Transactional
    public void unlock(UUID uId) {
        User user = userRepository.getReferenceById(uId);
        user.setDisabled(false);
    }

    @Transactional
    public void lock(UUID uId) {
        User user = userRepository.getReferenceById(uId);
        user.setDisabled(true);
    }

    @Transactional
    public String getRole(UUID uId) {
        User user = userRepository.getReferenceById(uId);
        return user.getRole();
    }
}
