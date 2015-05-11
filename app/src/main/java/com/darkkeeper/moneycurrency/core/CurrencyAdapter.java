package com.darkkeeper.moneycurrency.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.darkkeeper.moneycurrency.R;
import com.darkkeeper.moneycurrency.core.Currency;
import com.darkkeeper.moneycurrency.database.Connection;
import com.darkkeeper.moneycurrency.database.DataProvider;

import java.util.ArrayList;

/**
 * Created by andreipiatosin on 4/30/15.
 */
public class CurrencyAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<Currency> curenncies;
    private final boolean isShown;

    public CurrencyAdapter(Context context, ArrayList<Currency> currencies, boolean isShown) {
        super(context, R.layout.list_item, currencies);
        this.context = context;
        this.curenncies = currencies;
        this.isShown = isShown;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (isShown) {
            rowView = layoutInflater.inflate(R.layout.list_item, parent, false);
        } else {
            rowView = layoutInflater.inflate(R.layout.list_item_favorites, parent, false);
        }

        TextView nameTV = (TextView) rowView.findViewById( R.id.item_name );
        nameTV.setText( curenncies.get( position ).getName() );

        TextView textTV = (TextView) rowView.findViewById( R.id.item_text );
        textTV.setText( curenncies.get( position ).getOffName() );

        ImageView imageView = (ImageView) rowView.findViewById( R.id.item_image );
        imageView.setImageBitmap( curenncies.get( position ).getImage(context) );

        CheckBox checkBox = (CheckBox) rowView.findViewById( R.id.checkBox );

        if (isShown) {

            if (curenncies.get(position).getIsFavorite()) {

                checkBox.setChecked(true);

            } else {

                checkBox.setChecked(false);

            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    curenncies.get(position).setChecked(isChecked);
                    DataProvider dataProvider = new DataProvider(Connection.getConnection(context));
                    dataProvider.storeCurrency(curenncies.get(position));
                }
            });
        }

        return rowView;
    }
}
