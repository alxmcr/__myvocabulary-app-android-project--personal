package bo.alxmcr.app.adapters.word;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.constants.ConstantsTypography;
import bo.alxmcr.app.models.Word;
import bo.alxmcr.utils.typography.TypographyUtils;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class WordAdapter extends ArrayAdapter<Word> {
    private Context context;
    private ArrayList<Word> words;

    public WordAdapter(Context context, int viewResourceId, ArrayList<Word> words) {
        super(context, viewResourceId, words);
        this.context = context;
        this.words = words;
    }

    static class ViewHolderListItemWord {
        public TextView idWord;
        public TextView textWord;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;

        try {
            if (item == null) {
                item = LayoutInflater.from(context).inflate(R.layout.activity_list_words_item_word, parent, false);
                ViewHolderListItemWord viewHolder = new ViewHolderListItemWord();
                viewHolder.idWord = (TextView) item.findViewById(R.id.textViewIdWord);
                viewHolder.textWord = (TextView) item.findViewById(R.id.textViewTextWord);
                item.setTag(viewHolder);

            }

            if (this.words == null) {
                this.words = new ArrayList<Word>();
                final String typeMessage = this.context.getResources().getString(R.string.type_message_produced_error);
                String msgError = this.context.getResources().getString(R.string.msg_error_list_is_null);
                Log.e(typeMessage, msgError);
            }

            if (!this.words.isEmpty()) {
                ViewHolderListItemWord viewHolder = (ViewHolderListItemWord) item.getTag();
                viewHolder.idWord.setText(this.words.get(position).getId());
                viewHolder.textWord.setText(this.words.get(position).getText());

                //Style
                AssetManager assets = this.context.getAssets();
                Resources resources = this.context.getResources();
                TypographyUtils.setTypographyForATextView(viewHolder.idWord, ConstantsTypography.FONT_TEXT_REGULAR, assets, resources);
                TypographyUtils.setTypographyForATextView(viewHolder.textWord, ConstantsTypography.FONT_TEXT_REGULAR, assets, resources);

            } else {
                final String typeMessage = this.context.getResources().getString(R.string.type_message_produced_error);
                String msgError = this.context.getResources().getString(R.string.msg_error_list_is_empty);
                Log.e(typeMessage, msgError);
            }

        } catch (Exception e) {
            final String typeMessage = this.context.getResources().getString(R.string.type_message_produced_exception);
            String msgError = e.getMessage();
            Log.e(typeMessage, msgError, e);
        }

        return item;
    }
}
