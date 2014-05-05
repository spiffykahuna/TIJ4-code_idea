package annotations.exercises;


import annotations.database.Constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLNumber {
    //    int value() default 5;
    int scale() default 5;
    int precision() default 0;
    double defaultValue() default 0;

    String name() default "";
    Constraints constraints() default @Constraints;
}
