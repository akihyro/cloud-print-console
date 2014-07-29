package akihyro.cloudprintconsole;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import lombok.extern.slf4j.Slf4j;

/**
 * アプリケーション。
 */
@ApplicationPath("/")
@Slf4j
public class CloudPrintConsoleApplication extends Application {

    /**
     * コンストラクタ。
     */
    public CloudPrintConsoleApplication() {
    }

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.debug("アプリケーションを初期化しました。 => {}", this);
    }

    /**
     * 解放する。
     */
    @PreDestroy
    public void release() {
        log.debug("アプリケーションを解放しました。 => {}", this);
    }

}
