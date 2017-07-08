/**
 * 
 */
package com.publiccms.common.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * GeneratorColumn
 * 
 */
/**
 *
 * GeneratorColumn
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratorColumn {
    
    /**
     * @return
     */
    boolean condition() default false;

    /**
     * @return
     */
    boolean like() default false;
    
    /**
     * @return
     */
    boolean order() default false;
    
    /**
     * @return
     */
    boolean or() default false;
    
    /**
     * @return
     */
    String name() default "";
    
    /**
     * @return
     */
    String title();
}
