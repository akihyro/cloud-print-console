package akihyro.cloudprintconsole;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import lombok.extern.slf4j.Slf4j;

/**
 * Cloud Print Console アプリケーション。
 */
@ApplicationPath("")
@Slf4j
public class CloudPrintConsoleApplication extends Application {

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("Cloud Print Console アプリケーションインスタンスを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("Cloud Print Console アプリケーションインスタンスを終了します。 => {}", this);
    }

}
