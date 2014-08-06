package akihyro.cloudprintconsole.exceptions;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ObjectUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.exceptions.models.ValidationErrorResponse;

/**
 * バリデーションエラーハンドラ。
 */
@ApplicationScoped
@Provider
@Slf4j
public class ValidationErrorHandler implements ExceptionMapper<ConstraintViolationException> {

    /** レスポンスメディアタイプ */
    private static final MediaType[] RESPONSE_MEDIA_TYPES = {
        MediaType.APPLICATION_JSON_TYPE,
        MediaType.APPLICATION_XML_TYPE,
    };

    /** リクエスト */
    @Context
    private Request request;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("バリデーションエラーハンドラを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("バリデーションエラーハンドラを終了します。 => {}", this);
    }

    /**
     * レスポンスを取得する。
     *
     * @param exc 例外。
     * @return レスポンス。
     */
    @Override
    public Response toResponse(ConstraintViolationException exc) {
        log.error("バリデーションエラーが発生しました。 => {}", exc.getConstraintViolations());

        // エラーレスポンスを取得する
        val errorResponse = ValidationErrorResponse.valueOf(exc.getConstraintViolations());
        log.debug("エラーレスポンス => {}", errorResponse);

        // レスポンスを生成する
        val responseBuilder = Response.status(Status.BAD_REQUEST);
        responseBuilder.type(getMediaType());
        responseBuilder.entity(errorResponse);
        return responseBuilder.build();
    }

    /**
     * メディアタイプを取得する。
     *
     * @return メディアタイプ。
     */
    private MediaType getMediaType() {
        val variants = Variant.mediaTypes(RESPONSE_MEDIA_TYPES).build();
        val variant = ObjectUtils.defaultIfNull(request.selectVariant(variants), variants.get(0));
        return variant.getMediaType();
    }

}
