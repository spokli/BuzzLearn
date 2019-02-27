package buzz.de.buzzlearn.entities;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbDeleterInformation;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriter;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 06.01.2016.
 */
public abstract class EntityManager<T extends Entity> implements EntityManagerInterface<T> {

    /*-------------------------------------------------------------
        Variables
    --------------------------------------------------------------*/
    protected HashMap<Integer, T> entities;
    protected int newItemId;
    protected DbCallback callback;

    /*-------------------------------------------------------------
        Constructor
    --------------------------------------------------------------*/
    public EntityManager(DbCallback callback) {
        newItemId = -1;
        this.callback = callback;
        this.entities = new HashMap<>();

        initialize();
    }

    /*-------------------------------------------------------------
        Public Methods
    --------------------------------------------------------------*/
//    @Override
//    abstract public void refreshEntities();

    @Override
    public T getEntityById(int id) {
        return entities.get(id);
    }

    @Override
    public ArrayList<T> getEntitiesByIds(Collection<Integer> ids) {
        ArrayList<T> result = new ArrayList<>();
        Iterator<Integer> iter = ids.iterator();
        Integer actualId;
        while (iter.hasNext()) {
            actualId = iter.next();
            result.add(getEntityById(actualId));
        }
        return result;
    }

    @Override
    public ArrayList<T> getEntities() {
        return new ArrayList<T>(entities.values());
    }

    @Override
    public abstract void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose p);

    @Override
    public abstract void receiveInsertDbResponse(Long newRowId);

    @Override
    public abstract void receiveDeleteDbResponse(Long deletedRowId, int secondaryKey, DbDeleter.DeletePurpose p);

    public void deleteEntity(T entity, DbDeleter.DeletePurpose purpose){
        DbDeleterInformation values = createDeleteStatement(entity);

        executeDeleteStatement(values, purpose);

    }

    /*-------------------------------------------------------------
        Protected Methods
    --------------------------------------------------------------*/
    abstract protected void initialize();

    abstract protected DbReaderInformation createSelectAllStatement();
    abstract protected DbReaderInformation createSelectSingleStatement(int id);
    abstract protected DbDeleterInformation createDeleteStatement(T entity);

    protected void selectAllEntities(DbReader.SelectPurpose purpose) {

        DbReaderInformation values = createSelectAllStatement();

        DbReader dbReader = new DbReader(this, purpose);
        dbReader.execute(values);
    }

    protected void selectSingleEntity(int id, DbReader.SelectPurpose purpose){
        DbReaderInformation values = createSelectSingleStatement(id);

        DbReader dbReader = new DbReader(this, purpose);
        dbReader.execute(values);
    }

    protected void executeInsertStatement(DbWriterInformation values) {
        // Start asynchronous DB-communication
        DbWriter dbWriter = new DbWriter(this);
        dbWriter.execute(values);
    }

    protected void executeDeleteStatement(DbDeleterInformation values, DbDeleter.DeletePurpose purpose){
        DbDeleter deleter = new DbDeleter(this, purpose);
        deleter.execute(values);
    }

    @Override
    public Context getContext() {
        return callback.getContext();
    }

}