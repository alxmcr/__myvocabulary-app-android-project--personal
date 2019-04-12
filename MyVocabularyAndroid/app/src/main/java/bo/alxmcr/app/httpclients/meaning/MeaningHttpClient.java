package bo.alxmcr.app.httpclients.meaning;

import android.content.res.Resources;

import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.app.models.Word;

public interface MeaningHttpClient {
    boolean createMeaning(Resources resources, Meaning meaning) throws AppServerNotAvailableException;
    Meaning searchMeaningById(Resources resources, String idMeaning);
    boolean updateMeaning(Resources resources, Meaning meaning) throws AppServerNotAvailableException;
    boolean deleteMeaningById(Resources resources, String idMeaning) throws AppServerNotAvailableException;
}
