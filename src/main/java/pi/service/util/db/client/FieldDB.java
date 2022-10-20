/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Target(value = ElementType.FIELD)
public @interface FieldDB {
    String value();
}
