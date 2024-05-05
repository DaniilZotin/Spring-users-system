package ua.zotin.java.testtask.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.zotin.java.testtask.dto.UserDto;
import ua.zotin.java.testtask.entities.User;
import ua.zotin.java.testtask.services.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void getUsersTest() throws Exception {
        mockMvc.perform(get("/api/users/all")).andExpect(status().isOk());
    }

    @Test
    void createUserTest() throws Exception {
        UserDto newUser = new UserDto(1L, "daniil@gmail.com", "Daniil", "Zotin", LocalDate.of(2003, 2, 7), "Kyiv", "+380967330185");
        String userJson = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
        verify(userService,times(1)).createUser(newUser);
    }

    @Test
    void updateUserPatchTest() throws Exception {

        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "newemail@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "123456789");

        when(userService.updateUserPatch(userDto, userId)).thenReturn(userDto);


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/update/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(userDto.getBirthDate().toString()))
                .andExpect(jsonPath("$.address").value(userDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(userDto.getPhone()));


        verify(userService, times(1)).updateUserPatch(userDto, userId);
    }

//    @Test
//    void updateUserPutTest() throws Exception {
//
//        Long userId = 1L;
//        UserDto userDto = new UserDto(userId, "newemail@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "123456789");
//
//
//        when(userService.updateUserPut(userDto, userId)).thenReturn(userDto);
//
//
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId))
//                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
//                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
//                .andExpect(jsonPath("$.birthDate").value(userDto.getBirthDate().toString()))
//                .andExpect(jsonPath("$.address").value(userDto.getAddress()))
//                .andExpect(jsonPath("$.phone").value(userDto.getPhone()));
//
//
//        verify(userService, times(1)).updateUserPut(userDto, userId);
//    }

    @Test
    void searchUsersByBirthDateRangeTest() throws Exception {

        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(2005, 12, 31);


        List<User> users = new ArrayList<>();

        users.add(new User(1L,"danz@gmail.com","Mike", "Hidlton",
                LocalDate.of(2004, 5, 7),"Lviv","+380967330185"));
        users.add(new User(2L,"danz1@gmail.com","Mike", "Hidlton",
                LocalDate.of(1996, 5, 7),"Kyiv","+380967330185"));
        when(userService.findByBirthDateBetween(fromDate, toDate)).thenReturn(users);


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();


        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/searchByBirthDateRange")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$[1].firstName").value(users.get(1).getFirstName()));


        verify(userService, times(1)).findByBirthDateBetween(fromDate, toDate);
    }


    @Test
    void deleteUserTest() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/api/users/delete/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(userId);
    }




}
