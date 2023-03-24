package pg.reflectionutils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Optimal method. If it exists it can be check only for name (checkSignature==false) 
 * or for all signature (checkSignture==true)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {
	boolean checkSignature();
}
