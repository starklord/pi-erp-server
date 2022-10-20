package pi.service.util.db.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*
* @author etorres
*/
@Retention(RetentionPolicy.RUNTIME )
@Target(value = ElementType.TYPE)
public @interface TableDB {
    public String name();
    public String sequence() default "";
}