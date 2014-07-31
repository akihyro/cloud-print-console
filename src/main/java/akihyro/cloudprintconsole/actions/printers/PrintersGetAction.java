package akihyro.cloudprintconsole.actions.printers;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.printers.models.PrintersGetActionRes;
import akihyro.cloudprintconsole.actions.printers.models.PrintersGetActionResPrinter;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.models.CloudPrintApiSearchReq;
import akihyro.cloudprintconsole.models.UserInfo;

/**
 * プリンタリストGETアクション。
 */
@ApplicationScoped
@Path("printers")
@Slf4j
public class PrintersGetAction {

    /** API */
    @Inject
    protected CloudPrintApi api;

    /** ユーザ情報 */
    @Inject
    protected UserInfo userInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("プリンタリストGETアクションインスタンスを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("プリンタリストGETアクションインスタンスを終了します。 => {}", this);
    }

    /**
     * プリンタリストページを取得する。
     *
     * @return プリンタリストページ。
     * @throws Exception エラー。
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/printers/printers.html.jsp")
    public PrintersGetActionRes getAsHtml() throws Exception {

        // プリンタリストを取得する
        log.info("プリンタリストを取得します。");
        val apiReq = new CloudPrintApiSearchReq();
        val apiRes = api.call(userInfo.getId(), apiReq);
        val printers = new ArrayList<PrintersGetActionResPrinter>(apiRes.getPrinters().size());
        for (val apiPrinter : apiRes.getPrinters()) {
            val printer = new PrintersGetActionResPrinter();
            printer.setId(apiPrinter.getId());
            printer.setName(apiPrinter.getName());
            printer.setDisplayName(apiPrinter.getDisplayName());
            printer.setDescription(apiPrinter.getDescription());
            printer.setConnectionStatus(apiPrinter.getConnectionStatus());
            printers.add(printer);
        }
        val res = new PrintersGetActionRes();
        res.setPrinters(printers);

        return res;
    }

}
