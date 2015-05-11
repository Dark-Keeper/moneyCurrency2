package com.darkkeeper.moneycurrency.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by andreipiatosin on 4/24/15.
 */
public class Connection extends SQLiteOpenHelper {

    private boolean invalidDatabaseFile = false;

    private static final String dbName = "moneyCurrency";
    private static final String LOG_TAG = "myLog";

    private static int version;
    private static Connection dbConnection = null;
    private Context context;
    private String databaseFilename;
    private File databaseFile;

    public Connection(Context context, int version) {
        super(context, dbName, null, version);
        this.version = version;

        this.context            = context;
        this.databaseFilename   = dbName;

        SQLiteDatabase db = null;
        try
        {
            //Trying to read the database to process callbacks
            db = getReadableDatabase();
            if ( db != null )
            {
                db.close();
            }

            //Getting path to database file
            this.databaseFile = context.getDatabasePath( this.databaseFilename );

            //If database should be copied from assets
            if ( invalidDatabaseFile )
            {
                this.copyDatabase();
            }
        }
        catch (SQLiteException e)
        {

        }
        finally
        {
            if ( db != null && db.isOpen() )
            {
                db.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        invalidDatabaseFile = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void copyDatabase()
    {
        AssetManager assetManager = context.getResources().getAssets();

        InputStream in      = null;
        OutputStream out    = null;

        try
        {
            in  = assetManager.open("database/" + this.databaseFilename );
            out = new FileOutputStream( this.databaseFile );

            byte[] buffer = new byte[ 1024 ];
            int read = 0;

            while ( (read = in.read(buffer)) != -1 )
            {
                out.write(buffer, 0, read);
            }
        }
        catch (IOException e)
        {
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e) {}
            }

            if (out != null)
            {
                try
                {
                    out.close();
                } catch (IOException e) {}
            }
        }

       // setDatabaseVersion();
        invalidDatabaseFile = false;
    }

    public static Connection getConnection( Context context )
    {
        if ( dbConnection == null )
        {
            dbConnection = new Connection( context.getApplicationContext(), version);
        }

        return dbConnection;
    }

    public int getVersion(){
        return version;
    }

    public String getDbName(){
        return dbName;
    }
}