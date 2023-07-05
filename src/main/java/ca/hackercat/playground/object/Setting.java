package ca.hackercat.playground.object;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {
    String properName() default "default_name";
}
