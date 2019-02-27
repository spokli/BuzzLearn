package buzz.de.buzzlearn.database;

import android.content.ContentValues;

/**
 * Created by Marco on 11.09.2015.
 */
public class DbWriterInformation {

    public String table;
    public String nullColumnHack;
    public ContentValues values;

    public DbWriterInformation(String table, String nullColumnHack, ContentValues values) {
        this.table = table;
        this.nullColumnHack = nullColumnHack;
        this.values = values;
    }
}
