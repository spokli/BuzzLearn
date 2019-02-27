package buzz.de.buzzlearn.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import buzz.de.buzzlearn.database.DatabaseDefinition.Definition;

/**
 * Created by Marco on 11.09.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "BuzzLearn.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Definition.SQL_CREATE_TABLE_CATEGORIES);
        db.execSQL(DatabaseDefinition.Definition.SQL_CREATE_TABLE_WORDGROUPS);
        db.execSQL(Definition.SQL_CREATE_TABLE_LANGUAGES);
        db.execSQL(Definition.SQL_CREATE_TABLE_WORDS);
        db.execSQL(Definition.SQL_CREATE_TABLE_CONNECTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(Definition.SQL_DROP_TABLE_CATEGORIES);
        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_WORDGROUPS);
        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_LANGUAGES);
        db.execSQL(Definition.SQL_DROP_TABLE_WORDS);
        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_CONNECTIONS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
