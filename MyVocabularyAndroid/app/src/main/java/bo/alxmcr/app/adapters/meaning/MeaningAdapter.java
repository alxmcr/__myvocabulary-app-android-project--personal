package bo.alxmcr.app.adapters.meaning;

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
import java.util.List;

import alxmcr.bo.vocabularyapp_v8.R;
import bo.alxmcr.app.constants.ConstantsTypography;
import bo.alxmcr.app.models.Meaning;
import bo.alxmcr.utils.typography.TypographyUtils;

/**
 * Created by JoseCoca-i7 on 16/05/2015.
 */
public class MeaningAdapter extends ArrayAdapter<Meaning> {

    private Context context;
    private List<Meaning> meanings;

    public MeaningAdapter(Context context, int viewResourceId, List<Meaning> Meanings) {
        super(context, viewResourceId, Meanings);
        this.context = context;
        this.meanings = Meanings;
    }

    static class ViewHolderListItemMeaning {
        public TextView idMeaning;
        public TextView textMeaning;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;

        try {
            if (item == null) {
                item = LayoutInflater.from(context).inflate(R.layout.activity_list_meanings_item_meaning, parent, false);
                ViewHolderListItemMeaning viewHolder = new ViewHolderListItemMeaning();
                viewHolder.idMeaning = (TextView) item.findViewById(R.id.textViewIdMeaning);
                viewHolder.textMeaning = (TextView) item.findViewById(R.id.textViewTextMeaning);
                item.setTag(viewHolder);

            }

            if (this.meanings == null) {
                this.meanings = new ArrayList<Meaning>();
                final String typeMessage = this.context.getResources().getString(R.string.type_message_produced_error);
                String msgError = this.context.getResources().getString(R.string.msg_error_list_is_null);
                Log.e(typeMessage, msgError);
            }

            if (!this.meanings.isEmpty()) {
                ViewHolderListItemMeaning viewHolder = (ViewHolderListItemMeaning) item.getTag();
                viewHolder.idMeaning.setText(this.meanings.get(position).getId());
                viewHolder.textMeaning.setText(this.meanings.get(position).getText());

                //Style
                AssetManager assets = this.context.getAssets();
                Resources resources = this.context.getResources();
                TypographyUtils.setTypographyForATextView(viewHolder.idMeaning, ConstantsTypography.FONT_TEXT_REGULAR, assets, resources);
                TypographyUtils.setTypographyForATextView(viewHolder.textMeaning, ConstantsTypography.FONT_TEXT_REGULAR, assets, resources);

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
