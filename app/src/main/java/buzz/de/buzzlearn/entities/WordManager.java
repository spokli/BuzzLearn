package buzz.de.buzzlearn.entities;

import android.content.ContentValues;
import android.database.Cursor;

import buzz.de.buzzlearn.database.DatabaseDefinition;
import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbDeleterInformation;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 31.01.2016.
 */
public class WordManager extends EntityManager<Word> {

    private int highestWordId = -1;
    private int wordgroupId = -1;

    public WordManager(DbCallback callback){
        super(callback);
    }

    @Override
    protected void initialize() {
        selectAllEntities(DbReader.SelectPurpose.ALL_WORDS);
    }

    @Override
    public void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose p) {
        if (p != null) switch (p) {
            case ALL_WORDS:
                entities.clear();
                parseSelectResponseCursor(c);
                break;

            // Return from single word select, i.e. after insert
            case SINGLE_WORD:
                parseSelectResponseCursor(c);
                break;
            default:
                break;
        }

        c.close();

        // Tell DataManager object that the entities have been updated
        callback.newEntitiesArrived(newItemId, p);

        // Set newItemId back to default in case it has been set in insertResponseReceived(int id)
        newItemId = -1;
    }

    private void parseSelectResponseCursor(Cursor c){
        while (c.moveToNext()) {
            int wordId = c.getInt(0);
            int wordgroupId = c.getInt(1);
            String wordString = c.getString(2);
            int languageId = c.getInt(3);
            int points = c.getInt(4);
            boolean isGroupLabel = c.getString(5).equals("X");

            if (highestWordId < wordId) {
                highestWordId = wordId;
            }

            Word word = new Word(wordId, wordString, wordgroupId, languageId, points, isGroupLabel);

            entities.put(wordId, word);
        }
    }

    @Override
    public void receiveInsertDbResponse(Long newRowId) {
        selectSingleEntity(newItemId, DbReader.SelectPurpose.SINGLE_WORD);
    }

    @Override
    public void receiveDeleteDbResponse(Long deletedRowId, int secondaryKey, DbDeleter.DeletePurpose purpose) {
        if(deletedRowId >= 0){
            callback.entityDeleted(deletedRowId.intValue(), secondaryKey, purpose);
        } else {
            // TODO: Löschen nicht erfolgreich
        }
    }


    @Override
    protected DbReaderInformation createSelectAllStatement() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_WORD_ID,
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID,
                DatabaseDefinition.Definition.COLUMN_WORD,
                DatabaseDefinition.Definition.COLUMN_LANGUAGE_ID,
                DatabaseDefinition.Definition.COLUMN_WORD_POINTS,
                DatabaseDefinition.Definition.COLUMN_IS_GROUPLABEL,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                DatabaseDefinition.Definition.COLUMN_WORD + " ASC";

        String where = null;
        String[] whereValues = null;

        DbReaderInformation values = new DbReaderInformation(
                true,                                               // Distinct values
                DatabaseDefinition.Definition.TABLE_WORDS,          // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );
        return values;
    }

    @Override
    protected DbReaderInformation createSelectSingleStatement(int id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_WORD_ID,
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID,
                DatabaseDefinition.Definition.COLUMN_WORD,
                DatabaseDefinition.Definition.COLUMN_LANGUAGE_ID,
                DatabaseDefinition.Definition.COLUMN_WORD_POINTS,
                DatabaseDefinition.Definition.COLUMN_IS_GROUPLABEL,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy = null;
        String where =            DatabaseDefinition.Definition.COLUMN_WORD_ID + "=?" +
                        " AND " + DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID + "=?";
        String[] whereValues = {"" + id, "" + this.wordgroupId};

        this.wordgroupId = -1;

        return new DbReaderInformation(
                false,                                              // Distinct values
                DatabaseDefinition.Definition.TABLE_WORDS,          // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );
    }

    /*---------------------------------------
        Insert
    ---------------------------------------*/
    public void insertNewWord(String word, int wordgroupId, int languageId, int points, boolean isGroupLabel) {

        // Create a new map of values, where column names are the keys
        ContentValues content = new ContentValues();

        // Get ID manually because we can't use the auto-increment feature of sqlite for this table
        // due to the composite primary key
        newItemId = highestWordId + 1;

        this.wordgroupId = wordgroupId;

        content.put(DatabaseDefinition.Definition.COLUMN_WORD_ID, newItemId);
        content.put(DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID, wordgroupId);
        content.put(DatabaseDefinition.Definition.COLUMN_WORD, word);
        content.put(DatabaseDefinition.Definition.COLUMN_LANGUAGE_ID, languageId);
        content.put(DatabaseDefinition.Definition.COLUMN_WORD_POINTS, points);
        String isGroupLabelString = "";
        if (isGroupLabel)
            isGroupLabelString = "X";
        content.put(DatabaseDefinition.Definition.COLUMN_IS_GROUPLABEL, isGroupLabelString);

        // Create the array to pass to the asyncTask
        DbWriterInformation values = new DbWriterInformation(
                DatabaseDefinition.Definition.TABLE_WORDS,
                null,
                content
        );

        executeInsertStatement(values);
    }

    @Override
    protected DbDeleterInformation createDeleteStatement(Word word){

        String where = DatabaseDefinition.Definition.COLUMN_WORD_ID + "=? AND "
                     + DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID + "=?";
        String[] whereValues = {"" + word.getId(), "" + word.getWordgroupId() };

        return new DbDeleterInformation(
                DatabaseDefinition.Definition.TABLE_WORDS,          // The table to query
                where,                                              // The columns for the WHERE clause
                whereValues                                       // The values for the WHERE clause
        );

    }
}
