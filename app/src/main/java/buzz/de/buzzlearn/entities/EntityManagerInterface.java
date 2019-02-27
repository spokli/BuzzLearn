package buzz.de.buzzlearn.entities;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbReaderInformation;
import buzz.de.buzzlearn.database.DbWriter;
import buzz.de.buzzlearn.database.DbWriterInformation;

/**
 * Created by Marco on 06.01.2016.
 */
interface EntityManagerInterface<T> extends DbReader.DbDataReceiver, DbWriter.DbDataReceiver, DbDeleter.DbDataReceiver{

    // Getter
    T getEntityById(int id);
    ArrayList<T> getEntitiesByIds(Collection<Integer> ids);
    ArrayList<T> getEntities();

    // Select
//    void refreshEntities();

}
