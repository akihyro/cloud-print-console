package akihyro.cloudprintconsole.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;

/**
 * ジョブサブミットサービス。
 */
@Named
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
    @Getter @Setter
    private String printerID;

    /**
     * プリンタをリストアップする。
     *
     * @return プリントジョブ結果。
     * @throws Exception エラー。
     */
    @GET
    @Template(name = "/printed")
    public String submitJob() throws Exception {
        return api.submitJob(session.getId(), printerID);
    }

}
