package buzz.de.buzzlearn.entities;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

import buzz.de.buzzlearn.database.DatabaseDefinition;
import buzz.de.buzzlearn.database.DbDeleterInformation;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 21.12.2015.
 */
public class CategoryManager extends EntityManager<Category>{

    public CategoryManager(DbCallback callback){
        super(callback);
    }

    protected void initialize(){
        selectAllEntities(DbReader.SelectPurpose.ALL_CATEGORIES);
    }

//
//    public void refreshEntities(){
//        selectAllEntities(DbReader.SelectPurpose.ALL_CATEGORIES);
//
////        selectWordgroupsForCategories(DbReader.SelectPurpose.ALL_WORDGROUPS);
//    }

    @Override
    protected DbReaderInformation createSelectAllStatement() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_CATEGORY_ID,
                DatabaseDefinition.Definition.COLUMN_CATEGORY,
                DatabaseDefinition.Definition.COLUMN_POINTS_SUM,
                DatabaseDefinition.Definition.COLUMN_POINTS_WIN,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                DatabaseDefinition.Definition.COLUMN_CATEGORY + " ASC";

        String where = null;
        String[] whereValues = null;

        DbReaderInformation values = new DbReaderInformation(
                true,                                               // Distinct values
                DatabaseDefinition.Definition.TABLE_CATEGORIES, // The table to query
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
                DatabaseDefinition.Definition.COLUMN_CATEGORY_ID,
                DatabaseDefinition.Definition.COLUMN_CATEGORY,
                DatabaseDefinition.Definition.COLUMN_POINTS_SUM,
                DatabaseDefinition.Definition.COLUMN_POINTS_WIN,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy = null;
        String where = DatabaseDefinition.Definition.COLUMN_CATEGORY_ID + "=?";
        String[] whereValues = { "" + id };

        DbReaderInformation values = new DbReaderInformation(
                true,                                               // Distinct values
                DatabaseDefinition.Definition.TABLE_CATEGORIES,     // The table to query
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
    protected DbDeleterInformation createDeleteStatement(Category category) {
        return null;
    }

    @Override
    public void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose purpose){
        if (purpose != null) switch (purpose) {
            case ALL_CATEGORIES:
                entities.clear();
                parseSelectResponseCursor(c);
                break;

            // Return from single category select, i.e. after insert
            case SINGLE_CATEGORY:
                parseSelectResponseCursor(c);
                break;
            default:
                break;
        }

        c.close();

        // Tell DataManager object that the entities have been updated
        callback.newEntitiesArrived(newItemId, purpose);

        // Set newItemId back to default in case it has been set in insertResponseReceived(int id)
        newItemId = -1;
    }

    private void parseSelectResponseCursor(Cursor c){
        while (c.moveToNext()) {
            int category_id = c.getInt(0);
            String category_name = c.getString(1);
            int sum = c.getInt(2);
            int win = c.getInt(3);

            Category category = new Category(category_id, category_name, sum, win);

            entities.put(category_id, category);
        }
    }

    public void insertNewCategory(String name, int pointsSum, int pointsWin) {

        // Create a new map of values, where column names are the keys
        ContentValues content = new ContentValues();
        content.put(DatabaseDefinition.Definition.COLUMN_CATEGORY, name);
        content.put(DatabaseDefinition.Definition.COLUMN_POINTS_SUM, pointsSum);
        content.put(DatabaseDefinition.Definition.COLUMN_POINTS_WIN, pointsWin);

        // Create the array to pass to the asyncTask
        DbWriterInformation values = new DbWriterInformation(
                DatabaseDefinition.Definition.TABLE_CATEGORIES,
                null,
                content
        );

        executeInsertStatement(values);
    }

    @Override
    public void receiveInsertDbResponse(Long newRowId) {
        newItemId = (int) (long) newRowId;

        selectSingleEntity(newItemId, DbReader.SelectPurpose.SINGLE_CATEGORY);

    }

    @Override
    public void receiveDeleteDbResponse(Long deletedRowId) {

    }
}
