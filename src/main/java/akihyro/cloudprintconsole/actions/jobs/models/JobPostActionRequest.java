package akihyro.cloudprintconsole.actions.jobs.models;

import java.io.InputStream;

import javax.validation.constraints.NotNull;

import org.glassfish.jersey.media.multipart.FormDataParam;

import lombok.Data;

import akihyro.cloudprintconsole.validation.constraints.PrinterId;

/**
 * ジョブPOSTアクションリクエスト。
 */
@Data
public class JobPostActionRequest {

    /** プリンタID */
    @NotNull
    @PrinterId
    @FormDataParam("printer-id")
    private String printerId;

    /** タイトル */
    @FormDataParam("title")
    private String title;

    /** コンテンツタイプ */
    @FormDataParam("content-type")
    private String contentType;

    /** コンテンツ */
    @FormDataParam("content")
    private InputStream content;

}
