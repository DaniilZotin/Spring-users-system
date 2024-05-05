package ua.zotin.java.testtask.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import ua.zotin.java.testtask.exception.exceptions.DataReadFromFilePropertiesException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Properties;

@Slf4j
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {


    private String pathForMinAgeData;
    private int minAge;


    @Override
    public void initialize(Adult constraintAnnotation) {
        this.pathForMinAgeData = constraintAnnotation.value();
    }



    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return false;
        }
        String propertiesFileName = "application.properties";
        log.info("Path for minAge is " + pathForMinAgeData);
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertiesFileName);
            this.minAge = Integer.parseInt(properties.getProperty(pathForMinAgeData));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            throw new DataReadFromFilePropertiesException("Error read data " + pathForMinAgeData + " from " + propertiesFileName);
        }
        log.info("Min age is " + minAge);
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("You must be at least " + minAge + " years old")
                .addConstraintViolation();

        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears() >= minAge;
    }
}