package akihyro.cloudprintconsole.models;

import java.io.Serializable;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ユーザ情報。
 */
@SessionScoped
@Data
@Slf4j
public class UserInfo implements Serializable {

    /** ID */
    private String id;

    /** 認証後のリダイレクトURI */
    private URI redirectUriForAuth;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("ユーザ情報インスタンスを初期化します。 => {}", this);

        // 取り敢えずセッション単位で異なるユーザと見なしとく
        id = Integer.toHexString(System.identityHashCode(this));

    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("ユーザ情報インスタンスを終了します。 => {}", this);
    }

}
