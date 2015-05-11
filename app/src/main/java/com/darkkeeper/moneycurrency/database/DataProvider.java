package com.darkkeeper.moneycurrency.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.darkkeeper.moneycurrency.core.Currencies;
import com.darkkeeper.moneycurrency.core.Currency;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreipiatosin on 5/7/15.
 */
public class DataProvider {
    protected SQLiteDatabase database;
    protected Connection connection;

    public DataProvider(Connection dbConnection)
    {
        this.database = dbConnection.getWritableDatabase();
        this.connection = dbConnection;
    }
    /**
     *
     * @return
     */
    public ArrayList<Currency> getAll()
    {
        //Creating DB cursor
        Cursor cursor = database
                .query( connection.getDbName(), null, null, null, null, null, "name" );

        return buildList( cursor, true );
    }
    /**
     *
     * @return
     */
    public ArrayList<Currency> getFavorites()
    {
        //Creating DB cursor
        Cursor cursor = database.query( connection.getDbName(), null, "isFavorite = ?", new String[]{"true"}, null, null, "name");
        return buildList( cursor, true );
    }

    public void addRow(){
        database.execSQL("ALTER TABLE moneyCurrency RENAME TO TempOldTableA;");
        database.execSQL("CREATE TABLE moneyCurrency (code text primary key, name text, name_official text, flag text, isFavorite text DEFAULT false);");
        database.execSQL("INSERT INTO moneyCurrency (code, name, name_official, flag) SELECT code, name, name_official, flag FROM TempOldTableA;");
    }

    /**
     *
     * @param cursor
     * @param autoclose
     * @return
     */
    protected ArrayList<Currency> buildList( Cursor cursor, boolean autoclose )
    {
        ArrayList<Currency> currencies = new ArrayList<Currency>();

        cursor.moveToFirst();

        //Fetching all categories
        while ( !cursor.isAfterLast() )
        {
            Currency currency = new Currency();
            currency.apply(cursor);

            //Saving to list
            currencies.add(currency);

            //Next row
            cursor.moveToNext();
        }

        if ( autoclose )
        {
            cursor.close();
        }

        return currencies;
    }
    /**
     *
     * @return
     */
    public void storeCurrency( Currency currency )
    {

        database.insertWithOnConflict( this.connection.getDbName(), "code", currency.extract(), SQLiteDatabase.CONFLICT_REPLACE );

    }

}
