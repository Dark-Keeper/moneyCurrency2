package com.darkkeeper.moneycurrency.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by andreipiatosin on 4/30/15.
 */
public class Currency {

    private String code;
    private String name;
    private String name_official;
    private String flag;
    private boolean isFavorite;

    public void apply( Cursor cursor  )
    {
        code = cursor.getString(cursor.getColumnIndex("code"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        name_official = cursor.getString(cursor.getColumnIndex("name_official"));
        flag = cursor.getString(cursor.getColumnIndex("flag"));
        isFavorite = Boolean.valueOf( cursor.getString( cursor.getColumnIndex("isFavorite") ) );
    }

    public ContentValues extract()
    {
        ContentValues args = new ContentValues();

        args.put( "code", code );
        args.put( "name", name );
        args.put( "name_official", name_official );
        args.put( "flag", flag );
        args.put( "isFavorite", Boolean.toString(isFavorite) );

        return args;
    }


    public String getName(){
        return this.name;
    }

    public String getOffName(){
        return this.name_official;
    }

    public String getCode() { return this.code; }

    public Bitmap getImage(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open("flags/" + flag));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public boolean getIsFavorite () {
        return this.isFavorite;
    }

    public void setChecked(boolean isChecked){
        isFavorite = isChecked;
    }
}
