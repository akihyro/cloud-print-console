package akihyro.cloudprintconsole.exceptions.models;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.val;

/**
 * バリデーションエラー。
 */
@XmlRootElement(name = "validation-error")
@Data
public class ValidationError {

    /** メッセージ */
    @Getter(onMethod = @__({
        @XmlElement(name = "message")
    }) )
    private String message;

    /**
     * BeanValidationの制約違反からバリデーションエラーを取得する。
     *
     * @param violation BeanValidationの制約違反。
     * @return バリデーションエラー。
     */
    public static ValidationError valueOf(ConstraintViolation<?> violation) {
        val error = new ValidationError();
        error.setMessage(violation.getMessage());
        return error;
    }

}
