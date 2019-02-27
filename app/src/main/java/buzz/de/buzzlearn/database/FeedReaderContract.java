package buzz.de.buzzlearn.database;

import android.provider.BaseColumns;

/**
 * Created by Marco on 11.09.2015.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME_CATEGORIES = "categories";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_POINTS = "points";

        public static final String SQL_CREATE_ENTRIES_CATEGORIES =
                "CREATE TABLE IF NOT EXISTS " +
                        FeedEntry.TABLE_NAME_CATEGORIES + " (" +
                        FeedEntry.COLUMN_NAME_ID + " INTEGER NOT NULL, " +
                        FeedEntry.COLUMN_NAME_WORD + " VARCHAR(100) NOT NULL, " +
                        FeedEntry.COLUMN_NAME_POINTS + " INTEGER NOT NULL," + "" +
                        "PRIMARY KEY (" + FeedEntry.COLUMN_NAME_ID + ", " +
                        FeedEntry.COLUMN_NAME_WORD + ")" +
                        ");";


        public static final String SQL_DELETE_ENTRIES_CATEGORIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_CATEGORIES + ";";
    }
}