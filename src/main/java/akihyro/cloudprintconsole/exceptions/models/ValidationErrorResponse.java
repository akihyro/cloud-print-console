package akihyro.cloudprintconsole.exceptions.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Getter;
import lombok.val;

/**
 * バリデーションエラーレスポンス。
 */
@XmlRootElement(name = "validation-error-response")
@Data
public class ValidationErrorResponse {

    /** エラーリスト */
    @Getter(onMethod = @__({
        @XmlElementWrapper(name = "validation-errors"),
        @XmlElement(name = "validation-error"),
    }) )
    private List<ValidationError> errors;

    /**
     * BeanValidationの制約違反からバリデーションエラーレスポンスを取得する。
     *
     * @param violations BeanValidationの制約違反セット。
     * @return バリデーションエラーレスポンス。
     */
    public static ValidationErrorResponse valueOf(Set<ConstraintViolation<?>> violations) {

        // エラーリストを生成する
        val errors = new ArrayList<ValidationError>(violations.size());
        for (val violation : violations) {
            val error = ValidationError.valueOf(violation);
            errors.add(error);
        }

        // エラーレスポンスを生成する
        val errorResponse = new ValidationErrorResponse();
        errorResponse.setErrors(errors);
        return errorResponse;
    }

}
