package bo.alxmcr.utils.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public interface MethodsHttpUtils {
    HttpResponse doGet(String url, String mediaType) throws AppServerNotAvailableException, IOException;
    HttpResponse doPost(String url, String mediaType, String dataJson) throws UnsupportedEncodingException, ClientProtocolException, AppServerNotAvailableException;
    HttpResponse doDelete(String url, String mediaType) throws AppServerNotAvailableException;
    HttpResponse doPut(String url, String mediaType, String dataJson) throws AppServerNotAvailableException;
}
