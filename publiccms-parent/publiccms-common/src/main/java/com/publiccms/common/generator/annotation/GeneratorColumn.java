/**
 * 
 */
package com.publiccms.common.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段元数据注解
 * 
 * GeneratorColumn
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratorColumn {
    
    /**
     * @return 是否作为查询条件
     */
    boolean condition() default false;

    /**
     * @return 是否作为like查询
     */
    boolean like() default false;
    
    /**
     * @return 是否作为排序条件
     */
    boolean order() default false;
    
    /**
     * @return 是否作为or查询
     */
    boolean or() default false;
    
    /**
     * @return or查询名称
     */
    String name() default "";
    
    /**
     * @return 文字描述
     */
    String title();
}
