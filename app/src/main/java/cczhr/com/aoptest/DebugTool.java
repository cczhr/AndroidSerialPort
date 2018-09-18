package cczhr.com.aoptest;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)//注解在class文件中可用，但会被VM丢弃。
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})//可以给构造方法进行注解  可以给方法进行注解
public @interface DebugTool {
}
