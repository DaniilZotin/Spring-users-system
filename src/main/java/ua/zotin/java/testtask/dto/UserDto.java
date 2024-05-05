package ua.zotin.java.testtask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ua.zotin.java.testtask.constants.ApplicationConstants;
import ua.zotin.java.testtask.validator.Adult;
import ua.zotin.java.testtask.entities.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @Size(min = ApplicationConstants.DataValidation.MIN_SIZE_OF_EMAIL,
            max = ApplicationConstants.DataValidation.MAX_SIZE_OF_EMAIL, message = "Size of email must be between 4 and 100 characters")
    @Column(name = "email", unique = true)
    @Email(message = "Invalid Email pattern")
    @NotBlank(message = "Email cannot be empty")
    private String email;


    @Size(min = ApplicationConstants.DataValidation.MIN_SIZE_FIRST_NAME,
            max = ApplicationConstants.DataValidation.MAX_SIZE_FIRST_NAME, message = "Size of first name must be between 1 and 50 characters")
    @Column(name = "first_name")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @Size(min = ApplicationConstants.DataValidation.MIN_SIZE_LAST_NAME,
            max = ApplicationConstants.DataValidation.MAX_SIZE_LAST_NAME, message = "Size of last name must be between 1 and 50 characters")
    @Column(name = "last_name")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;


    @Column(name = "birth_date")
    @NotNull(message = "Date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Adult(value="minimum.age")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;


    @Column(name = "address")
    @Size(min = ApplicationConstants.DataValidation.MIN_SIZE_ADDRESS,
            max = ApplicationConstants.DataValidation.MAX_SIZE_ADDRESS, message = "Size of address must be between 3 and 255 characters")
    private String address;

    @Size(min = ApplicationConstants.DataValidation.MIN_SIZE_OF_PHONE,
            max = ApplicationConstants.DataValidation.MAX_SIZE_OF_PHONE, message = "Size of number must be 12 characters")
    @Pattern(regexp = "^(\\+?38)?\\(?0(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\)?[0-9]{7}$", message = "Phone must start with '+38' and contain only 12 digits ")
    @Column(name = "phone_number")
    private String phone;

    public static UserDto mapToUserDto (User user){

        return new UserDto(user.getId(),user.getEmail(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getAddress(), user.getPhone());
    }



}
