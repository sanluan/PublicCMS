/**
 * 
 */
package com.sanluan.common.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratorColumn {
    boolean condition() default false;

    boolean like() default false;
    
    boolean order() default false;
    
    boolean or() default false;
    
    String name() default "";
    
    String title();
}
