package ua.zotin.java.testtask.services;


import ua.zotin.java.testtask.dto.UserDto;
import ua.zotin.java.testtask.entities.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    User createUser(UserDto userDto);

    UserDto updateUserPatch(UserDto userDto, Long id);

    UserDto updateUserPut(UserDto userDto, Long id);
    void deleteUserById(Long id);

    List<User> findByBirthDateBetween(LocalDate from, LocalDate to);
}
