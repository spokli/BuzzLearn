package buzz.de.buzzlearn.entities;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

import buzz.de.buzzlearn.database.DatabaseDefinition;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 21.12.2015.
 */
public class WordgroupManager extends EntityManager<Wordgroup> {

    private int highestWordgroupId = -1;
    private int categoryId = -1;

//    private boolean queryIsRunning = false;

    /*---------------------------------------
      Constructor
    ---------------------------------------*/
    public WordgroupManager(DbCallback callback) {
        super(callback);
    }

    /*---------------------------------------
      Select
    ---------------------------------------*/
    protected void initialize() {
        selectAllEntities(DbReader.SelectPurpose.ALL_WORDGROUPS);
    }


    @Override
    public DbReaderInformation createSelectAllStatement() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID,
                DatabaseDefinition.Definition.COLUMN_CATEGORY_ID,
                DatabaseDefinition.Definition.COLUMN_IS_MAINBUZZ,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID + " ASC" + ", " +
                        DatabaseDefinition.Definition.COLUMN_CATEGORY_ID + " ASC";

        String where = null;
        String[] whereValues = null;

        return new DbReaderInformation(
                true,                                               // Distinct values
                DatabaseDefinition.Definition.TABLE_WORDGROUPS,     // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );

    }

    @Override
    protected DbReaderInformation createSelectSingleStatement(int wordgroupId) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID,
                DatabaseDefinition.Definition.COLUMN_CATEGORY_ID,
                DatabaseDefinition.Definition.COLUMN_IS_MAINBUZZ,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy = null;
        String where =          DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID + "=?" +
                        " AND " + DatabaseDefinition.Definition.COLUMN_CATEGORY_ID + "=?";
        String[] whereValues = {"" + wordgroupId, "" + this.categoryId};

        this.categoryId = -1;

        return new DbReaderInformation(
                false,                                              // Distinct values
                DatabaseDefinition.Definition.TABLE_WORDGROUPS,     // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );
    }

    @Override
    public void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose purpose) {
        if (purpose != null) switch (purpose) {
            case ALL_WORDGROUPS:
                entities.clear();
                parseSelectResponseCursor(c);
                break;

            // Return from single wordgroup select, i.e. after insert
            case SINGLE_WORDGROUP:
                parseSelectResponseCursor(c);
                break;

            default:
                break;
        }

        c.close();

        // Tell DataManager that the entities have been updated
        callback.newEntitiesArrived(newItemId, purpose);

        // Set newItemId back to default in case it has been set in insertResponseReceived(int id)
        newItemId = -1;
    }

    private void parseSelectResponseCursor(Cursor c) {
        while (c.moveToNext()) {
            int wordgroupId = c.getInt(0);
            int categoryId = c.getInt(1);
            boolean isMainBuzz = c.getString(2).equals("X");

            if (highestWordgroupId < wordgroupId) {
                highestWordgroupId = wordgroupId;
            }

            if(entities.containsKey(wordgroupId)){
                // If wordgroup-object is already existing, just add category to its map
                entities.get(wordgroupId).addCategoryId(categoryId, isMainBuzz);
            } else{
                // Create new Wordgroup and add categoryId
                entities.put(wordgroupId, new Wordgroup(wordgroupId, categoryId, isMainBuzz));
            }
        }
    }

//    private void parseSelectResponseCursor(Cursor c){
//        while (c.moveToNext()) {
//            int wordgroupId = c.getInt(0);
//            String wordString = c.getString(1);
//            int languageId = c.getInt(2);
//            int wordPoints = c.getInt(3);
//            boolean isGroupLabel = c.getString(4).equals("X");
//
//            if(highestWordgroupId < wordgroupId){
//                highestWordgroupId = wordgroupId;
//            }
//
//            Wordgroup wordgroup;
//            Word word = new Word(wordString, languageId, wordPoints);
//
//            if (entities.containsKey(wordgroupId)) {
//                // wordgroup-object is already existing, just add words to maps
//                wordgroup = entities.get(wordgroupId);
//                wordgroup.addWord(word);
//                if (isGroupLabel) {
//                    wordgroup.setGroupLabel(wordString);
//                }
//
//            } else {
//                // Create new wordgroup
//                HashMap<String, Word> words = new HashMap<>();
//                words.put(wordString, word);
//                wordgroup = new Wordgroup(wordgroupId, words, wordString);
//
//                entities.put(wordgroupId, wordgroup);
//            }
//        }
//    }


    /*---------------------------------------
        Insert
    ---------------------------------------*/
    public void insertNewWordgroup(int categoryId, boolean isMainBuzz) {

        // Create a new map of values, where column names are the keys
        ContentValues content = new ContentValues();

        // Get ID manually because we can't use the auto-increment feature of sqlite for this table
        // due to the composite primary key
        newItemId = highestWordgroupId + 1;

        this.categoryId = categoryId;

        content.put(DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID, newItemId);
        content.put(DatabaseDefinition.Definition.COLUMN_CATEGORY_ID, categoryId);
        String isMainBuzzString = "";
        if (isMainBuzz)
            isMainBuzzString = "X";
        content.put(DatabaseDefinition.Definition.COLUMN_IS_MAINBUZZ, isMainBuzzString);

        // Create the array to pass to the asyncTask
        DbWriterInformation values = new DbWriterInformation(
                DatabaseDefinition.Definition.TABLE_WORDGROUPS,
                null,
                content
        );

        executeInsertStatement(values);
    }

    @Override
    public void receiveInsertDbResponse(Long newRowId) {
        // NOTE: Only use the newRowId to check if insert was successful
        // It's not nessesarily the new wordgroup's id because of composite primary keys

        selectSingleEntity(newItemId, DbReader.SelectPurpose.SINGLE_WORDGROUP);
    }

}
