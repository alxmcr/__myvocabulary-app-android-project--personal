package bo.alxmcr.app.httpclients.word;


import android.content.res.Resources;

import java.util.ArrayList;

import bo.alxmcr.app.extras.exceptions.connectivity.AppServerNotAvailableException;
import bo.alxmcr.app.models.Word;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public interface WordHttpClient {

    ArrayList<Word> readWords(Resources resources) throws AppServerNotAvailableException;
    boolean createWord(Resources resources, Word word) throws AppServerNotAvailableException;
    ArrayList<Word> searchWordByText(Resources resources, String textWord);
    Word searchWordById(Resources resources, String idWord);
    boolean deleteWordById(Resources resources, String idWord) throws AppServerNotAvailableException;
    boolean updateWord(Resources resources, Word word) throws AppServerNotAvailableException;
}
