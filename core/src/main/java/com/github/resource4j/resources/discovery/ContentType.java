package com.github.resource4j.resources.discovery;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ContentType {

    String extension() default ".";

    String mimeType() default "application/octet-stream";

}
