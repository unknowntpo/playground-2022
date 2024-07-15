package org.example;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GetEnvTest {
    @Disabled("TODO: how to mock System.getenv()?")
    @Test
    void shouldGetEnv() {
        assertEquals(2, 1+1);
    }
}
