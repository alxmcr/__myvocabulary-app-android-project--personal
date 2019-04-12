package bo.alxmcr.app.broadcast_receivers.word;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.adapters.word.WordAdapter;
import bo.alxmcr.app.asynctask.word.ReadWordTaskAsync;
import bo.alxmcr.app.constants.ConstantsIntentFilters;
import bo.alxmcr.app.models.Word;

/**
 * Created by JOSECOCA on 01/05/2015.
 */
public class ReadWordsBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = ReadWordsBroadcastReceiver.class.getSimpleName();
    private Context context;
    private ArrayList<Word> listWords;
    private ListView listViewWords;

    public ListView getListViewWords() {
        return listViewWords;
    }

    public void setListViewWords(ListView listViewWords) {
        this.listViewWords = listViewWords;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Word> getListWords() {
        return listWords;
    }

    public void setListWords(ArrayList<Word> listWords) {
        this.listWords = listWords;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceived");

        if (intent.getAction().equals(ConstantsIntentFilters.ACTION_START)) {
            Log.d(TAG, "start task");
            this.updateListView(this.listWords);
        } else if (intent.getAction().equals(ConstantsIntentFilters.ACTION_END)) {
            Log.d(TAG, "end task");
        }

    }

    private void updateListView(ArrayList<Word> words) {
        Log.d(TAG, "updateListView");
        if (words != null) {
            int size = words.size();
            Log.d(TAG, "#Words:" + String.valueOf(size));

            WordAdapter adapter = new WordAdapter(this.context, R.layout.activity_list_words_item_word, words);
            this.listViewWords.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "words is null");
            Resources resources = this.context.getResources();
            final String msg = resources.getString(R.string.msg_error_list_is_empty);
            final String typeMessage = TAG + resources.getString(R.string.type_message_produced_error);
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
