package akihyro.cloudprintconsole;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.slf4j.bridge.SLF4JBridgeHandler;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Cloud Print Console アプリケーション。
 */
@ApplicationPath("")
@Slf4j
public class CloudPrintConsoleApplication extends ResourceConfig {

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("Cloud Print Console アプリケーションを初期化します。 => {}", this);
        installSlf4jBridge();
        setupProperties();
        registerComponents();
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("Cloud Print Console アプリケーションを終了します。 => {}", this);
        uninstallSlf4jBridge();
    }

    /**
     * SLF4Jブリッジをインストールする。
     */
    private void installSlf4jBridge() {
        log.info("SLF4Jブリッジをインストールします。");
        SLF4JBridgeHandler.install();
    }

    /**
     * SLF4Jブリッジをアンインストールする。
     */
    private void uninstallSlf4jBridge() {
        log.info("SLF4Jブリッジをアンインストールします。");
        SLF4JBridgeHandler.uninstall();
    }

    /**
     * プロパティを設定する。
     */
    private void setupProperties() {
        log.info("アプリケーションのプロパティを設定します。");

        // JerseyのBeanValidationサポートを無効にする
        // ※理由1.バリデーションが二重で実行されてしまう。
        // ※理由2.@Injectが効かない。
        property(ServerProperties.BV_FEATURE_DISABLE, true);

        // URIの拡張子によってレスポンスのメディアタイプを決める
        property(ServerProperties.MEDIA_TYPE_MAPPINGS, getMediaTypeMappings());

        // JSPテンプレートのベースパスをセットする
        property(JspMvcFeature.TEMPLATES_BASE_PATH, "/WEB-INF/templates");

    }

    /**
     * コンポーネントをスキャン/登録する。
     */
    private void registerComponents() {
        log.info("アプリケーションのコンポーネントをスキャン/登録します。");

        // アプリケーションのパッケージをスキャン/登録する
        packages(CloudPrintConsoleApplication.class.getPackage().getName());

        // マルチパートフォームデータを読み込み可能にする
        register(MultiPartFeature.class);

        // JSPテンプレートを有効にする
        register(JspMvcFeature.class);

    }

    /**
     * メディアタイプマッピングを取得する。
     *
     * @return メディアタイプマッピング。
     */
    private Map<String, MediaType> getMediaTypeMappings() {
        val mappings = new HashMap<String, MediaType>();
        mappings.put("html", MediaType.TEXT_HTML_TYPE.withCharset(StandardCharsets.UTF_8.name()));
        mappings.put("xml", MediaType.APPLICATION_XML_TYPE.withCharset(StandardCharsets.UTF_8.name()));
        mappings.put("json", MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name()));
        return Collections.unmodifiableMap(mappings);
    }

}
