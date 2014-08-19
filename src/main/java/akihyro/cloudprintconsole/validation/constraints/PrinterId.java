package akihyro.cloudprintconsole.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import akihyro.cloudprintconsole.validation.validators.PrinterIdValidator;

/**
 * バリデーション制約：プリンタID。
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PrinterIdValidator.class })
public @interface PrinterId {

    /** メッセージ */
    String message() default "{akihyro.cloudprintconsole.validation.constraints.PrinterId.message}";

    /** グループ */
    Class<?>[] groups() default { };

    /** ペイロード */
    Class<? extends Payload>[] payload() default { };

    /**
     * 制約リスト。
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        /** 制約リスト */
        PrinterId[] value();

    }

}
