package bo.alxmcr.utils.httpclient;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class MethodsHttpUtilsImpl implements MethodsHttpUtils {

    public static final String TAG = MethodsHttpUtilsImpl.class.getSimpleName();

    @Override
    public HttpResponse doGet(String url, String mediaType) throws IOException {

        Log.v(TAG, "URL->" + url);

        HttpResponse resp = null;

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//5s

        HttpClient httpClient = new DefaultHttpClient(httpParams);

        HttpGet get = new HttpGet(url);
        get.setHeader("content-type", mediaType);

        final String nameMethodHttp = get.getMethod();
        Log.v(TAG, "Petition ->" + nameMethodHttp);

        resp = httpClient.execute(get);

        return resp;

    }

    @Override
    public HttpResponse doPost(String url, String mediaType, String dataJson) throws UnsupportedEncodingException, ClientProtocolException, AppServerNotAvailableException {
        StringEntity entity = null;
        HttpResponse resp = null;

        Log.v(TAG, "URL->" + url);

        if (dataJson != null) {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//5s

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPost post = new HttpPost(url);
            post.setHeader("content-type", mediaType);

            try {
                final String nameMethodHttp = post.getMethod();
                Log.v("Petition ->", nameMethodHttp);

                Log.v("Post.dataJson->", dataJson);
                entity = new StringEntity(dataJson);
                post.setEntity(entity);
                resp = httpClient.execute(post);
            } catch (UnsupportedEncodingException e) {
                throw e;
            } catch (ClientProtocolException e) {
                throw e;
            } catch (IOException e) {
                throw new AppServerNotAvailableException("Server not available");
            }
        } else {
            Log.e(TAG, "Error. dataJson is null");
        }


        return resp;
    }

    @Override
    public HttpResponse doDelete(String url, String mediaType) throws AppServerNotAvailableException {
        Log.v(TAG, "URL->" + url);
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//5s

        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpDelete delete = new HttpDelete(url);

        delete.setHeader("content-type", mediaType);
        HttpResponse resp = null;
        try {
            final String nameMethodHttp = delete.getMethod();
            Log.v(TAG, "Petition ->" + nameMethodHttp);

            resp = httpClient.execute(delete);
        } catch (IOException e) {
            throw new AppServerNotAvailableException("Server not available");
        }

        return resp;
    }

    @Override
    public HttpResponse doPut(String url, String mediaType, String dataJson) throws AppServerNotAvailableException {
        HttpResponse resp = null;

        Log.v("URL->", url);

        if (dataJson != null) {

            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//5s

            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpPut put = new HttpPut(url);
            put.setHeader("content-type", mediaType);

            try {
                final String nameMethodHttp = put.getMethod();
                Log.v(TAG, "Petition ->" + nameMethodHttp);

                Log.v(TAG, "PUT.dataJson->" + dataJson);
                StringEntity entity = new StringEntity(dataJson);
                put.setEntity(entity);
                resp = httpClient.execute(put);
            } catch (IOException e) {
                throw new AppServerNotAvailableException("Server not available");
            }
        } else {
            Log.e(TAG, "dataJson is null");
        }

        return resp;
    }

}
