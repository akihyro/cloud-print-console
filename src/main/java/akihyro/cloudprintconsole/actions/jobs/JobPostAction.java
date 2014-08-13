package akihyro.cloudprintconsole.actions.jobs;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.jobs.models.JobPostActionRequest;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.models.CloudPrintApiSubmitRequest;
import akihyro.cloudprintconsole.models.UserInfo;

/**
 * ジョブPOSTアクション。
 */
@ApplicationScoped
@Path("jobs")
@Slf4j
public class JobPostAction {

    /** API */
    @Inject
    CloudPrintApi api;

    /** ユーザ情報 */
    @Inject
    protected UserInfo userInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("ジョブPOSTアクションを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("ジョブPOSTアクションを終了します。 => {}", this);
    }

    /**
     * ジョブを登録する。
     *
     * @param request リクエスト。
     * @return ジョブ登録結果ページ。
     * @throws Exception エラー。
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/jobs/posted.html.jsp")
    public String getAsHtml(@BeanParam JobPostActionRequest request) throws Exception {
        val apiRequest = new CloudPrintApiSubmitRequest();
        apiRequest.setPrinterId(request.getPrinterId());
        apiRequest.setTitle(request.getTitle());
        apiRequest.setContentType(request.getContentType());
        apiRequest.setContent(request.getContent());
        return api.call(userInfo.getId(), apiRequest).toString();
    }

}
