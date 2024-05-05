package ua.zotin.java.testtask.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.zotin.java.testtask.dto.UserDto;
import ua.zotin.java.testtask.entities.User;
import ua.zotin.java.testtask.exception.exceptions.IdAreNotEqualsException;
import ua.zotin.java.testtask.exception.exceptions.JsonIsNullException;
import ua.zotin.java.testtask.exception.exceptions.UserAlreadyExistsException;
import ua.zotin.java.testtask.exception.exceptions.UserNotFoundException;
import ua.zotin.java.testtask.repositories.UserRepo;
import ua.zotin.java.testtask.services.UserService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = userRepo.findAll().stream().map(UserDto::mapToUserDto).collect(Collectors.toList());



        if (users.isEmpty()) {
            log.info("Users were not found");
            throw new UserNotFoundException("Users were not found");
        }

        return users;

    }

    @Override
    public User createUser(UserDto userDto) {

        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }


        User newUser = new User(userDto.getEmail(),userDto.getFirstName(),userDto.getLastName()
                ,userDto.getBirthDate(),userDto.getAddress(),userDto.getPhone());

        userRepo.save(newUser);



        return userRepo.save(newUser);
    }

    @Override
    public UserDto updateUserPatch(UserDto userDto, Long id) {

        if(userDto.getId()!=null){
            if(!userDto.getId().toString().equals(String.valueOf(id))){
                throw new IdAreNotEqualsException("If you input id, it must be same in body and in parameter");
            }
        }

        boolean result = hasNonNullField(userDto);
        if(!result){
            log.info("User DTO is null");
            throw new JsonIsNullException("Your request does not have json input");
        }

        Optional<User> existUser = userRepo.findById(id);

        log.info("User you try to update with Patch is " + existUser);
        if(existUser.isEmpty()){
            log.info("User you try to change is empty");
            throw new UserNotFoundException("User with id " + id + " was not found ");
        }

        User user = existUser.get();


        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            Optional<User> userWithSameEmail = userRepo.findByEmail(userDto.getEmail());
            if(userWithSameEmail.isPresent()){
                User userWithSameEmailGet = userWithSameEmail.get();
                if (!Objects.equals(userWithSameEmailGet.getId(), id)) {
                    throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
                }
            }
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.getBirthDate());
        }
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }

        userRepo.save(user);

        return UserDto.mapToUserDto(user);
    }


    @Override
    public UserDto updateUserPut(UserDto userDto, Long id) {

        String result = hasEmptyField(userDto);
        if(!result.isEmpty()){
            log.info("Missing one or more fields ");
            throw new JsonIsNullException("Body of your request does not have - " + result);
        }

        Optional<User> existUser = userRepo.findById(id);

        log.info("User you try to update with Patch is " + existUser);
        if(existUser.isEmpty()){
            log.info("User you try to change is empty");
            throw new UserNotFoundException("User with id " + id + " was not found ");
        }

        User user = existUser.get();

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            Optional<User> userWithSameEmail = userRepo.findByEmail(userDto.getEmail());
            if(userWithSameEmail.isPresent()){
                User userWithSameEmailGet = userWithSameEmail.get();
                if (!Objects.equals(userWithSameEmailGet.getId(), id)) {
                    throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
                }
            }
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.getBirthDate());
        }
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }



        userRepo.save(user);

        return UserDto.mapToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {

        Optional<User> userToDelete = userRepo.findById(id);
        if(userToDelete.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " was not found ");
        }

        userRepo.delete(userToDelete.get());
    }

    @Override
    public List<User> findByBirthDateBetween(LocalDate from, LocalDate to){

        List<User> usersBetweenDate = userRepo.findByBirthDateBetween(from,to);
        if(usersBetweenDate.isEmpty()){
            throw new UserNotFoundException("Users were not found");
        }
        return usersBetweenDate;
    }





    public static boolean hasNonNullField(Object obj) {
        if (obj == null) {
            return false;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                if (value != null) {
                    log.info("True");

                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static String hasEmptyField(Object obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder emptyFields = new StringBuilder();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if(field.getName().equals("id")){
                log.info("Current field is " + field.getName());
                continue;
            }
            try {
                Object value = field.get(obj);
                if (value == null) {
                    log.info("True");

                    emptyFields.append(field.getName()).append(", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return emptyFields.toString();
    }
}
