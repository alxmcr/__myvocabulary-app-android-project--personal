package bo.alxmcr.app.httpclients.dbmanaged;

import android.content.res.Resources;

import java.util.ArrayList;

import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;

/**
 * Created by JoseCoca-i7 on 24/05/2015.
 */
public interface ListPIDPostgreSQLHttpClient {

    ArrayList<Integer> listPIDPostgreSQL(Resources resources) throws AppServerNotAvailableException;
}
