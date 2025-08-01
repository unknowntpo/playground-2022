package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonBuilderTest {

    @Test
    public void testPersonBuilder() {
        Person person = new PersonBuilder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .email("john.doe@example.com")
                .build();

        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals(30, person.getAge());
        assertEquals("john.doe@example.com", person.getEmail());
    }

    @Test
    public void testBuilderFluentInterface() {
        Person person = new PersonBuilder()
                .firstName("Jane")
                .lastName("Smith")
                .age(25)
                .email("jane.smith@example.com")
                .build();

        assertNotNull(person);
        assertEquals("Jane Smith", person.getFirstName() + " " + person.getLastName());
    }
}