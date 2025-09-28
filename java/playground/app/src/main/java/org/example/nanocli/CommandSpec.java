package org.example.nanocli;

public @interface CommandSpec {
    String name() default "";
    Class<? extends Command>[] subCommands() default {};
}
