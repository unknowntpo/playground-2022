package org.example.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAnnotationTest {
    @TestAnnotation(name = "FakeUTTest", type = TestType.UT)
    class FakeUTTest {

    }

    @TestAnnotation(name = "FakeITTest", type = TestType.IT)
    class FakeITTest {

    }

    @Test
    void testGetInfoFromAnnotation() {
        {
            var fakeUTTest = new FakeUTTest();
            assertTrue(fakeUTTest.getClass().isAnnotationPresent(TestAnnotation.class));
            var annotation = fakeUTTest.getClass().getAnnotation(TestAnnotation.class);
            assertNotNull(annotation);
            assertEquals("FakeUTTest", annotation.name());
            assertEquals(TestType.UT, annotation.type());
        }
        {
            var fakeITTest = new FakeITTest();
            assertTrue(fakeITTest.getClass().isAnnotationPresent(TestAnnotation.class));
            var annotation = fakeITTest.getClass().getAnnotation(TestAnnotation.class);
            assertNotNull(annotation);
            assertEquals("FakeITTest", annotation.name());
            assertEquals(TestType.IT, annotation.type());
        }
    }

    private Object getClass(Object object) {
        return object.getClass();
    }
}