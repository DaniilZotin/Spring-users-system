package ua.zotin.java.testtask.services;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.zotin.java.testtask.dto.UserDto;
import ua.zotin.java.testtask.entities.User;
import ua.zotin.java.testtask.exception.exceptions.UserAlreadyExistsException;
import ua.zotin.java.testtask.exception.exceptions.UserNotFoundException;
import ua.zotin.java.testtask.repositories.UserRepo;
import ua.zotin.java.testtask.services.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;


    @Mock
    private UserRepo userRepo;
    @Test
    void getEmptyUserListFromDbThrowsException(){

        List<User> resultListForTest = new ArrayList<>();
        when(userRepo.findAll()).thenReturn(resultListForTest);
        assertThrows(UserNotFoundException.class, ()-> userService.getUsers());
    }

    @Test
    public void createUserSavesNewUserIfNotExists() {

        UserDto newUserDto = new UserDto(1L,"new@example.com", "Alice", "Smith",
                LocalDate.of(1995, 5, 5), "Address", "987654321");
        when(userRepo.existsByEmail(newUserDto.getEmail())).thenReturn(false);
        User newUser = new User(newUserDto.getEmail(), newUserDto.getFirstName(), newUserDto.getLastName(),
                newUserDto.getBirthDate(), newUserDto.getAddress(), newUserDto.getPhone());
        when(userRepo.save(newUser)).thenReturn(newUser);


        User savedUser = userService.createUser(newUserDto);


        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(newUserDto.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(newUserDto.getFirstName(), savedUser.getFirstName());
        Assertions.assertEquals(newUserDto.getLastName(), savedUser.getLastName());
        Assertions.assertEquals(newUserDto.getBirthDate(), savedUser.getBirthDate());
        Assertions.assertEquals(newUserDto.getAddress(), savedUser.getAddress());
        Assertions.assertEquals(newUserDto.getPhone(), savedUser.getPhone());
    }
    @Test
    public void createUserThrowsExceptionIfUserAlreadyExists() {

        UserDto existingUserDto = new UserDto(1L,"existing@example.com", "John", "Doe",
                LocalDate.of(1990, 1, 1), "Address", "123456789");
        when(userRepo.existsByEmail(existingUserDto.getEmail())).thenReturn(true);


        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(existingUserDto));
    }


    @Test
    public void updateUserWhenJsonRequestWasEmpty() {

        UserDto userDto = new UserDto();

        Assertions.assertFalse(UserServiceImpl.hasNonNullField(userDto));
    }

    @Test
    public void deleteUserByIdThrowsExceptionIfUserNotFound() {

        Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        Assertions.assertEquals("User with id " + userId + " was not found", exception.getMessage().trim());

    }

    @Test
    public void deleteUserByIdDeletesUserIfFound() {
        Long userId = 1L;
        User userToDelete = new User(userId, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "123456789");
        when(userRepo.findById(userId)).thenReturn(Optional.of(userToDelete));


        userService.deleteUserById(userId);

        verify(userRepo, times(1)).delete(userToDelete);
    }



}
