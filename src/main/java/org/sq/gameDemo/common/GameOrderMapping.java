package org.sq.gameDemo.common;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameOrderMapping {
    String value() default "";
}