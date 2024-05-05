package ua.zotin.java.testtask.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    @UtilityClass
    public class DataValidation{
        public static final int MIN_SIZE_OF_EMAIL = 4;
        public static final int MAX_SIZE_OF_EMAIL = 100;
        public static final int MIN_SIZE_OF_PHONE = 13;
        public static final int MAX_SIZE_OF_PHONE = 13;

        public static final int MIN_SIZE_ADDRESS = 3;
        public static final int MAX_SIZE_ADDRESS = 255;

        public static final int MAX_SIZE_FIRST_NAME = 50;
        public static final int MIN_SIZE_FIRST_NAME = 2;
        public static final int MAX_SIZE_LAST_NAME = 50;
        public static final int MIN_SIZE_LAST_NAME = 2;


    }
}
