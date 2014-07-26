package akihyro.cloudprintconsole.service;

import java.io.ByteArrayInputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import lombok.Cleanup;
import lombok.val;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.model.CloudPrintApiSubmitReq;

/**
 * ジョブサブミットサービス。
 */
@RequestScoped
@Path("/submit-job")
public class JobSubmitService {

    /** API */
    @Inject
    CloudPrintApi api;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /**
     * プリンタID。
     */
    @QueryParam("printer-id")
    String printerId;

    /**
     * プリンタをリストアップする。
     *
     * @return プリントジョブ結果。
     * @throws Exception エラー。
     */
    @GET
    @Template(name = "/printed")
    public String submitJob() throws Exception {
        val submitReq = new CloudPrintApiSubmitReq();
        submitReq.setPrinterId(printerId);
        submitReq.setTitle("Cloud Print Console: Test Print.");
        submitReq.setContentType("text/html");
        @Cleanup val content = new ByteArrayInputStream(
                ("<h1>Cloud Print Console: Test Print.</h1><p>Printer ID: " + printerId + "</p>").getBytes());
        submitReq.setContent(content);
        return api.call(session.getId(), submitReq).toString();
    }

}
