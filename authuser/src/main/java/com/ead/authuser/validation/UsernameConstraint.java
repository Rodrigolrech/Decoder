package com.ead.authuser.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class) // Classe para implementar essa interface e nela criar a validação em si;
@Target({ElementType.METHOD, ElementType.FIELD}) // Aonde podemos utilizar essa implementação;
@Retention(RetentionPolicy.RUNTIME) //Quando a validação irá ocorrer;
public @interface UsernameConstraint {

    String message() default "Invalid username";
    Class<?>[] groups() default {}; // Grupo de validação
    Class<? extends Payload> [] payload() default {}; // Nível que irá ocorrer tal erro.
}
