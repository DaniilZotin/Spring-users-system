package ua.zotin.java.testtask.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.zotin.java.testtask.dto.UserDto;
import ua.zotin.java.testtask.entities.User;
import ua.zotin.java.testtask.exception.exceptions.FromAndToException;
import ua.zotin.java.testtask.services.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping("all")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> users = userService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping("create")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Name user you try to create is " + userDto.getFirstName());
        User newUser = userService.createUser(userDto);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }


    @PatchMapping("update/{id}")
    public ResponseEntity<UserDto> updateUserPatch(@PathVariable("id") Long id,  @RequestBody UserDto userDto) {

        UserDto user = userService.updateUserPatch(userDto, id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserDto> updateUserPut(@PathVariable("id") Long id, @Valid  @RequestBody UserDto userDto) {

        UserDto user = userService.updateUserPut(userDto, id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

        userService.deleteUserById(id);

        return new ResponseEntity<>("You have deleted customer with id: " + id, HttpStatus.OK);
    }

    @GetMapping("/searchByBirthDateRange")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                                  @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        if (from.isAfter(to)) {
            throw new FromAndToException("'From' date must be before 'To' date");
        }

        List<User> users = userService.findByBirthDateBetween(from, to);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }




}
