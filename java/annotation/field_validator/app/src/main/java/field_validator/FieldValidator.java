package field_validator;

import java.lang.reflect.Field;

public class FieldValidator {
    public static boolean validate(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(NotNullOrEmpty.class)) {
                field.setAccessible(true); // Access private fields
                if (field.getType().equals(String.class)) {
                    String value = (String) field.get(object);
                    if (value == null || value.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
