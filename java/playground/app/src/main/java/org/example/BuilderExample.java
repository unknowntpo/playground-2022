package org.example;

public class BuilderExample {
    public static void main(String[] args) {
        // Create a Person using the generated PersonBuilder
        Person person = new PersonBuilder()
                .firstName("Alice")
                .lastName("Johnson")
                .age(28)
                .email("alice.johnson@example.com")
                .build();
        
        System.out.println("Created person: " + person);
        
        // Create another person with different approach
        Person employee = new PersonBuilder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@company.com")
                .age(35)
                .build();
        
        System.out.println("Created employee: " + employee);
    }
}