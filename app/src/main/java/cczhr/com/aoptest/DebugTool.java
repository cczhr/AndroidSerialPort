package cczhr.com.aoptest;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})//可以给构造方法进行注解  可以给方法进行注解
public @interface DebugTool {
    public String resourceId() default "哈哈";
}
