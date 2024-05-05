package ua.zotin.java.testtask.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
@Documented


public @interface Adult {

    String value() ;
    String message() default "You must be at least ... years old" ;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
