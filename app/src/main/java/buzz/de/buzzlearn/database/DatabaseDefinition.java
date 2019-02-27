package buzz.de.buzzlearn.database;

import android.provider.BaseColumns;

/**
 * Created by Marco on 11.09.2015.
 */
public final class DatabaseDefinition {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseDefinition() {
    }

    public static abstract class Definition implements BaseColumns {

        // Table names
        public static final String TABLE_CATEGORIES = "CATEGORIES";
        public static final String TABLE_WORDGROUPS = "WORDGROUPS";
        public static final String TABLE_LANGUAGES = "LANGUAGES";
        public static final String TABLE_WORDS = "WORDS";
        public static final String TABLE_CONNECTIONS = "CONNECTIONS";

        // Key fields
        public static final String COLUMN_CATEGORY_ID = "CATEGORY_ID";
        public static final String COLUMN_WORDGROUP_ID = "WORDGROUP_ID";
        public static final String COLUMN_LANGUAGE_ID = "LANGUAGE_ID";
        public static final String COLUMN_CONNECTION_ID = "CONNECTION_ID";

        // Table CATEGORIES
        // Key fields: COLUMN_CATEGORY_ID
        public static final String COLUMN_CATEGORY = "CATEGORY";
        public static final String COLUMN_POINTS_SUM = "POINTS_SUM";
        public static final String COLUMN_POINTS_WIN = "POINTS_WIN";

        // Table WORDGROUPS
        // Key fields: COLUMN_CATEGORY_ID, COLUMN_WORDGROUP_ID
        public static final String COLUMN_IS_MAINBUZZ = "IS_MAINBUZZ";

        // Table LANGUAGES
        // Key fields: COLUMN_LANGUAGE_ID
        public static final String COLUMN_LANGUAGE = "LANGUAGE";

        // Table WORDS
        // Key fields: COLUMN_WORD_ID, COLUMN_WORD, COLUMN_WORDGROUP_ID
        // Foreign keys: COLUMN_WORDGROUP_ID, COLUMN_LANGUAGE_ID
        public static final String COLUMN_WORD_ID = "WORD_ID";
        public static final String COLUMN_WORD = "WORD";
        public static final String COLUMN_WORD_POINTS = "WORD_POINTS";
        public static final String COLUMN_IS_GROUPLABEL = "IS_GROUPLABEL";

        // Table CONNECTIONS
        // Key fields: COLUMN_CONNECTION_ID
        // Foreign keys: COLUMN_WORDGROUP_ID_A, COLUMN_WORDGROUP_ID_B, COLUMN_CATEGORY_ID
        public static final String COLUMN_WORDGROUP_ID_A = "WORDGROUP_ID_A";
        public static final String COLUMN_WORDGROUP_ID_B = "WORDGROUP_ID_B";
        public static final String COLUMN_CONNECTION_DESCRIPTION = "CONNECTION_DESCRIPTION";
        public static final String COLUMN_CONNECTION_POINTS = "CONNECTION_POINTS";

        //---
        // Create-Statements
        //---
        public static final String SQL_CREATE_TABLE_CATEGORIES =
                "CREATE TABLE IF NOT EXISTS " + Definition.TABLE_CATEGORIES +
                        " (" +
                                    Definition.COLUMN_CATEGORY_ID +  " INTEGER PRIMARY KEY" +
                            ", " +  Definition.COLUMN_CATEGORY +     " VARCHAR(20) NOT NULL" +
                            ", " +  Definition.COLUMN_POINTS_SUM +   " INTEGER NOT NULL" +
                            ", " +  Definition.COLUMN_POINTS_WIN +   " INTEGER NOT NULL" +
                        ");";

        public static final String SQL_CREATE_TABLE_WORDGROUPS =
                "CREATE TABLE IF NOT EXISTS " + Definition.TABLE_WORDGROUPS +
                        " (" +
                                    Definition.COLUMN_WORDGROUP_ID + " INTEGER NOT NULL" +
                            ", " +  Definition.COLUMN_CATEGORY_ID +  " INTEGER NOT NULL" +
                            ", " +  Definition.COLUMN_IS_MAINBUZZ +  " VARCHAR(1) NOT NULL" +

                            ", PRIMARY KEY (" +
                                        Definition.COLUMN_WORDGROUP_ID +
                                " , " + Definition.COLUMN_CATEGORY_ID +
                            ")" + " , " +

                            " FOREIGN KEY (" + Definition.COLUMN_CATEGORY_ID + ") " +
                                "REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
                        ");";

        public static final String SQL_CREATE_TABLE_LANGUAGES =
                "CREATE TABLE IF NOT EXISTS " + Definition.TABLE_LANGUAGES +
                        " (" +
                                    Definition.COLUMN_LANGUAGE_ID + " INTEGER PRIMARY KEY" +
                            ", " +  Definition.COLUMN_LANGUAGE +    " VARCHAR(30) NOT NULL" +
                        ");";

        public static final String SQL_CREATE_TABLE_WORDS =
                "CREATE TABLE IF NOT EXISTS " + Definition.TABLE_WORDS +
                        " (" +
                                    Definition.COLUMN_WORD_ID +         " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_WORD +            " VARCHAR(100) NOT NULL" +
                            " , " + Definition.COLUMN_WORDGROUP_ID +    " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_LANGUAGE_ID +     " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_WORD_POINTS +     " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_IS_GROUPLABEL +   " VARCHAR(1) NOT NULL" +

                            ", PRIMARY KEY (" +
                                            Definition.COLUMN_WORD_ID +
//                                " , " +     Definition.COLUMN_WORD +
                                " , " +     Definition.COLUMN_WORDGROUP_ID +
                            ")" + " , " +
                            " FOREIGN KEY (" + Definition.COLUMN_LANGUAGE_ID + ") " +
                                "REFERENCES " + TABLE_LANGUAGES + "(" + COLUMN_LANGUAGE_ID + ")" +
                            " FOREIGN KEY (" + Definition.COLUMN_WORDGROUP_ID + ") " +
                                "REFERENCES " + TABLE_WORDGROUPS + "(" + COLUMN_WORDGROUP_ID + ")" +
                        ");";

        public static final String SQL_CREATE_TABLE_CONNECTIONS =
                "CREATE TABLE IF NOT EXISTS " + Definition.TABLE_CONNECTIONS +
                        " (" +
                                    Definition.COLUMN_CONNECTION_ID +           " INTEGER PRIMARY KEY" +
                            " , " + Definition.COLUMN_WORDGROUP_ID_A +          " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_WORDGROUP_ID_B +          " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_CATEGORY_ID +             " INTEGER NOT NULL" +
                            " , " + Definition.COLUMN_CONNECTION_DESCRIPTION +  " VARCHAR(200) NOT NULL" +
                            " , " + Definition.COLUMN_CONNECTION_POINTS +       " INTEGER NOT NULL" +

                            " , " +
                            " FOREIGN KEY (" + Definition.COLUMN_WORDGROUP_ID_A + ") " +
                                "REFERENCES " + TABLE_WORDGROUPS + "(" + COLUMN_WORDGROUP_ID + ")" +
                            " FOREIGN KEY (" + Definition.COLUMN_WORDGROUP_ID_B + ") " +
                                "REFERENCES " + TABLE_WORDGROUPS + "(" + COLUMN_WORDGROUP_ID + ")" +
                        ");";

        public static final String SQL_DROP_TABLE_CATEGORIES =
                "DROP TABLE IF EXISTS " + Definition.TABLE_CATEGORIES + ";";
        public static final String SQL_DROP_TABLE_WORDGROUPS =
                "DROP TABLE IF EXISTS " + Definition.TABLE_WORDGROUPS + ";";
        public static final String SQL_DROP_TABLE_LANGUAGES =
                "DROP TABLE IF EXISTS " + Definition.TABLE_LANGUAGES + ";";
        public static final String SQL_DROP_TABLE_WORDS =
                "DROP TABLE IF EXISTS " + Definition.TABLE_WORDS + ";";
        public static final String SQL_DROP_TABLE_CONNECTIONS =
                "DROP TABLE IF EXISTS " + Definition.TABLE_CONNECTIONS + ";";
    }
}