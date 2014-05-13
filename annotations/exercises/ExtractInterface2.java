//: annotations/ExtractInterface.java
// APT-based annotation processing.
package annotations.exercises;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ExtractInterface2 {
  public String value();
} ///:~
