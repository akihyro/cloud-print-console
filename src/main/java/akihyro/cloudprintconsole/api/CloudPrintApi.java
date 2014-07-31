package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.api.models.CloudPrintApiReq;
import akihyro.cloudprintconsole.api.models.CloudPrintApiRes;

/**
 * Cloud Print API。
 * 認証処理～APIコールまで担う。
 */
@ApplicationScoped
@Slf4j
public class CloudPrintApi {

    /** API情報 */
    @Getter
    private CloudPrintApiInfo apiInfo;

    /** 認証コードフロー */
    @Getter
    private AuthorizationCodeFlow codeFlow;

    /**
     * 初期化する。
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        log.info("Cloud Print API インスタンスを初期化します。 => {}", this);
        apiInfo = CloudPrintApiInfo.load();
        codeFlow = newAuthorizationCodeFlow();
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("Cloud Print API インスタンスを終了します。 => {}", this);
    }

    /**
     * 認証コードフローを生成する。
     *
     * @return 認証コードフロー。
     * @throws IOException IOエラー。
     */
    private AuthorizationCodeFlow newAuthorizationCodeFlow() throws IOException {
        val builder = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                newNetHttpTransportForAuth(),
                new JacksonFactory(),
                new GenericUrl(apiInfo.getTokenUri()),
                new ClientParametersAuthentication(apiInfo.getClientId(), apiInfo.getClientSecret()),
                apiInfo.getClientId(),
                apiInfo.getAuthUri().toString());
        builder.setScopes(apiInfo.getScopes());
        builder.setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance());
        return builder.build();
    }

    /**
     * 認証用HTTPトランスポートを生成する。
     *
     * @return 認証用HTTPトランスポート。
     */
    private NetHttpTransport newNetHttpTransportForAuth() {
        val builder = new NetHttpTransport.Builder();
        builder.setProxy(apiInfo.getProxy());
        return builder.build();
    }

    /**
     * 認証情報があるか判定する。
     *
     * @param userId ユーザID。
     * @return 認証情報があるかどうか。
     * @throws IOException IOエラー。
     */
    public boolean hasCredential(String userId) throws IOException {
        return codeFlow.loadCredential(userId) != null;
    }

    /**
     * 認可コードリクエストURIを生成する。
     *
     * @return 認可コードリクエストURI。
     */
    public URI newAuthCodeReqURI() {
        log.info("認可コードリクエストURIを生成します。");
        val url = codeFlow.newAuthorizationUrl();
        url.setRedirectUri(apiInfo.getRedirectUri().toString());
        val uri = url.toURI();
        log.info("認可コードリクエストURIを生成しました。 => {}", uri);
        return uri;
    }

    /**
     * 認可コードから認証情報を取得/保存する。
     *
     * @param userId ユーザID。
     * @param authCode 認可コード。
     * @throws IOException IOエラー。
     */
    public void storeCredential(String userId, String authCode) throws IOException {
        log.info("認証情報を取得/保存します。 => ユーザID: {}", userId);
        log.debug("認可コード: {}", authCode);
        val req = codeFlow.newTokenRequest(authCode);
        req.setRedirectUri(apiInfo.getRedirectUri().toString());
        val res = req.execute();
        val credential = codeFlow.createAndStoreCredential(res, userId);
        log.info("認証情報を取得/保存しました。");
        log.debug("認証情報: {}", credential);
    }

    /**
     * APIをコールする。
     *
     * @param userId ユーザID。
     * @param req APIリクエスト。
     * @return APIレスポンス。
     * @throws IOException IOエラー。
     */
    public <ResType extends CloudPrintApiRes> ResType
    call(String userId, CloudPrintApiReq<ResType> req) throws IOException {
        log.info("APIをコールします。 => ユーザID: {}, APIリクエスト: {}", userId, req);

        // 認証情報をロードする
        val credential = codeFlow.loadCredential(userId);

        // HTTPクライアントを生成する
        val builder = HttpClientBuilder.create();
        builder.setProxy(apiInfo.getProxyHttpHost());
        builder.setDefaultHeaders(Arrays.asList(
                new BasicHeader("Authorization", "OAuth " + credential.getAccessToken())));
        @Cleanup val httpClient = builder.build();

        // HTTPリクエストを発行し、HTTPレスポンスを取得する
        val httpReq = req.toHttpReq(apiInfo);
        log.debug("HTTPリクエストを発行します。 => {}", httpReq);
        @Cleanup val httpRes = httpClient.execute(httpReq);
        val httpResEntity = httpRes.getEntity();
        @Cleanup val httpResContent = httpResEntity.getContent();
        String httpResContentAsString = IOUtils.toString(httpResContent, CharEncoding.UTF_8);
        log.debug("HTTPレスポンスを取得しました。 => {}", StringUtils.removePattern(httpResContentAsString, "\r|\n"));

        // HTTPレスポンスをオブジェクトへ変換する
        val res = new ObjectMapper().readValue(httpResContentAsString, req.getResType());

        log.info("APIをコールしました。 => APIレスポンス: {}", res);
        return res;
    }

}
