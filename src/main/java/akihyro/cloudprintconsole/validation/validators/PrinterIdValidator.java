package akihyro.cloudprintconsole.validation.validators;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.models.CloudPrintApiSearchRequest;
import akihyro.cloudprintconsole.models.UserInfo;
import akihyro.cloudprintconsole.validation.constraints.PrinterId;

/**
 * プリンタIDバリデータ。
 */
@Slf4j
public class PrinterIdValidator implements ConstraintValidator<PrinterId, String> {

    /** API */
    @Inject
    private CloudPrintApi api;

    /** ユーザ情報 */
    @Inject
    private UserInfo userInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("プリンタIDバリデータを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("プリンタIDバリデータを終了します。 => {}", this);
    }

    /**
     * 制約を元に初期化する。
     *
     * @param constraint 制約。
     */
    @Override
    public void initialize(PrinterId constraint) {
        log.info("プリンタIDバリデータを制約を元に初期化します。 => バリデータ: {}, 制約: {}", this, constraint);
    }

    /**
     * 検証する。
     *
     * @param value 値。
     * @param context コンテキスト。
     * @return 正常かどうか。
     */
    @Override
    @SneakyThrows
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("プリンタIDを検証します。 => {}", value);

        // 値がない場合は正常とする
        if (value == null) {
            return true;
        }

        // プリンタIDが存在するか確認する
        val apiRequest = new CloudPrintApiSearchRequest();
        val apiResponse = api.call(userInfo.getId(), apiRequest);
        for (val printer : apiResponse.getPrinters()) {
            if (StringUtils.equals(value, printer.getId())) {
                return true;
            }
        }
        return false;
    }

}
