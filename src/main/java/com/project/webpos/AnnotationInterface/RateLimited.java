package com.project.webpos.AnnotationInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
  int capacity() default 5; // Total allowed request (burst capacity)
  int refillTokens() default 5; // Number of request refilled per period
  int periodInSeconds() default 120; // Time frame for the refill window
}
