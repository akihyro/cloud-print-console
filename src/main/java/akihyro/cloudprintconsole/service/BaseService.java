package akihyro.cloudprintconsole.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.ObjectUtils;

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
        log.trace("@PostConstruct: {}", ObjectUtils.identityToString(this));
    }

    /**
     * 解放する。
     */
    @PreDestroy
    public void release() {
        log.trace("@PreDestroy: {}", ObjectUtils.identityToString(this));
    }

}
