package org.example.nanocli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandSpec {
    String name();
    Class<? extends Command>[] subCommands() default {};
    String description();
}
