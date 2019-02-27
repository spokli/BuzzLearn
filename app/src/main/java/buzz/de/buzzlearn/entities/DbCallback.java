package buzz.de.buzzlearn.entities;

import android.content.Context;

import java.io.Serializable;

import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbReader;

/**
 * Created by Marco on 22.01.2016.
 */
public interface DbCallback{

    void newEntitiesArrived(int newItemId, DbReader.SelectPurpose purpose);
    void entityDeleted(int deletedItemId, int secondaryKey, DbDeleter.DeletePurpose purpose);
    Context getContext();
}
