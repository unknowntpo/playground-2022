package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appCanBeInstantiated() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest, "app should be instantiated");
    }
}
