package akihyro.cloudprintconsole.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import lombok.val;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.model.CloudPrintApiSubmitReq;
import akihyro.cloudprintconsole.service.model.SubmitServiceReq;

/**
 * サブミットサービス。
 */
@RequestScoped
@Path("/submit")
public class SubmitService extends BaseService {

    /** API */
    @Inject
    CloudPrintApi api;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /** リクエスト */
    @BeanParam
    SubmitServiceReq req;

    /**
     * サブミットする。
     *
     * @return プリントジョブ結果。
     * @throws Exception エラー。
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Template(name = "/printed")
    public String submit() throws Exception {
        val apiReq = new CloudPrintApiSubmitReq();
        apiReq.setPrinterId(req.getPrinterId());
        apiReq.setTitle(req.getTitle());
        apiReq.setContentType(req.getContentType());
        apiReq.setContent(req.getContent());
        return api.call(session.getId(), apiReq).toString();
    }

}
