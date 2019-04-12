package bo.alxmcr.app.httpclients.dbmanaged;

import android.content.res.Resources;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.constants.ConstantsMediaTypes;
import bo.alxmcr.app.constants.ConstantsMessages;
import bo.alxmcr.app.constants.ConstantsURLs;
import bo.alxmcr.app.extras.converts.ConvertsTypeWord;
import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.general.GeneralUtils;
import bo.alxmcr.utils.httpclient.MethodsHttpUtilsImpl;

/**
 * Created by JoseCoca-i7 on 24/05/2015.
 */
public class ListPIDPostgreSQLHttpClientImpl implements ListPIDPostgreSQLHttpClient {

    public static final String TAG = ListPIDPostgreSQLHttpClientImpl.class.getSimpleName();

    public static final String URL_API_HTTP_CLIENT_LIST_PID_POSTGRES = "postgreSQL/list_process_postgresql";

    private MethodsHttpUtilsImpl methodsHttpUtilsImpl;
    private String domainApp;

    public ListPIDPostgreSQLHttpClientImpl(String domainApp) {
        this.methodsHttpUtilsImpl = new MethodsHttpUtilsImpl();
        this.domainApp = domainApp;
    }

    @Override
    public ArrayList<Integer> listPIDPostgreSQL(Resources resources) throws AppServerNotAvailableException {
        Log.v(TAG, "Start listPIDPostgreSQL");

        HttpResponse response = null;
        String respStr = null;
        JSONArray arrayJson = null;
        ArrayList<Integer> pids = null;

        String url = GeneralUtils.getURLRestBasic(this.domainApp, ConstantsURLs.KEYWORD_API_PATH, URL_API_HTTP_CLIENT_LIST_PID_POSTGRES);
        String mediaType = ConstantsMediaTypes.APPLICATION_JSON;

        try {

            if (this.methodsHttpUtilsImpl != null) {
                response = this.methodsHttpUtilsImpl.doGet(url, mediaType);
            }

            if (response != null) {
                respStr = GeneralUtils.convertHttpResponseToString(response);
                Log.v(TAG, "Server: respStr->" + respStr);
            }

            if (respStr != null && !ConstantsMessages.ERROR_API_REST.equals(respStr)) {
                arrayJson = new JSONArray(respStr);
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_string_is_null);
                Log.e(typeMessage, msg);
            }

            if (arrayJson != null) {
                int lengthJSONArray = arrayJson.length();
                pids = new ArrayList<Integer>();
                for (int i = 0; i < lengthJSONArray; i++) {
                    pids.add(Integer.valueOf(arrayJson.getInt(i)));
                }
            } else {
                final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
                String msg = TAG + resources.getString(R.string.msg_error_json_array_is_null);
                Log.e(typeMessage, msg);
            }

        } catch (IOException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = e.getMessage();
            Log.e(TAG, typeMessage + msg, e);
        } catch (JSONException e) {
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            final String msg = e.getMessage();
            Log.e(TAG, typeMessage + msg, e);
        }


        Log.v(TAG, "End listPIDPostgreSQL");

        return pids;
    }
}
