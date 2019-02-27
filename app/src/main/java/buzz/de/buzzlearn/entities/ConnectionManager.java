package buzz.de.buzzlearn.entities;

import android.content.Context;
import android.database.Cursor;

import buzz.de.buzzlearn.database.DatabaseDefinition;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 06.01.2016.
 */
public class ConnectionManager extends EntityManager<Connection> {

    public ConnectionManager(DbCallback callback){
        super(callback);
    }

    @Override
    protected void initialize(){
        selectAllEntities(DbReader.SelectPurpose.ALL_CONNECTIONS);
    }

//    @Override
//    public void refreshEntities() {
//
//    }

    @Override
    public DbReaderInformation createSelectAllStatement() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                DatabaseDefinition.Definition.COLUMN_CONNECTION_ID,
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID_A,
                DatabaseDefinition.Definition.COLUMN_WORDGROUP_ID_B,
                DatabaseDefinition.Definition.COLUMN_CATEGORY_ID,
                DatabaseDefinition.Definition.COLUMN_CONNECTION_DESCRIPTION,
                DatabaseDefinition.Definition.COLUMN_CONNECTION_POINTS,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                DatabaseDefinition.Definition.COLUMN_CONNECTION_ID + " ASC";

        String where = null;
        String[] whereValues = null;

//        String where = DatabaseDefinition.Definition.COLUMN_ID+"=?";
//        String[] whereValues = {"1"};

        DbReaderInformation values = new DbReaderInformation(
                true,                                               // Distinct values
                DatabaseDefinition.Definition.TABLE_CONNECTIONS, // The table to query
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
        return null;
    }

    @Override
    public void executeInsertStatement(DbWriterInformation values) {

    }

    @Override
    public void receiveInsertDbResponse(Long newRowId) {

    }

    @Override
    public void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose purpose) {
        if (purpose != null) switch (purpose) {
            case ALL_CONNECTIONS:

                entities.clear();

                while (c.moveToNext()) {

                    int connectionId = c.getInt(0);
                    int wordgroup1Id = c.getInt(1);
                    int wordgroup2Id = c.getInt(2);
                    int categoryId = c.getInt(3);
                    String description = c.getString(4);
                    int points = c.getInt(5);

                    Connection connection = new Connection(connectionId, wordgroup1Id, wordgroup2Id, categoryId, description, points);

                    entities.put(connectionId, connection);
                }
                break;
            default:
                break;
        }

        c.close();

        // Tell DataManager object that the entities have been updated
        callback.newEntitiesArrived(newItemId, purpose);
    }

    @Override
    public Context getContext(){
        return callback.getContext();
    }
}
