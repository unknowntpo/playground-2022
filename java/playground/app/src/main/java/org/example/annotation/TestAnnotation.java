package org.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    // not null
    public String name() default "";
    public TestType type() default TestType.UT;
}
