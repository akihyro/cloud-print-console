package akihyro.cloudprintconsole.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

/**
 * ベースサービス。
 */
@Slf4j
public abstract class BaseService {

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.debug("サービスを初期化しました。 => {}", this);
    }

    /**
     * 解放する。
     */
    @PreDestroy
    public void release() {
        log.debug("サービスを解放しました。 => {}", this);
    }

}
