package buzz.de.buzzlearn.database;

/**
 * Created by Marco on 14.02.2016.
 */
public class DbDeleterInformation {

    public String tableName;
    public String whereClause;
    public String[] whereArgs;

    public DbDeleterInformation(String tableName, String whereClause, String[] whereArgs){
        this.tableName = tableName;
        this.whereClause = whereClause;
        this.whereArgs = whereArgs;
    }


}
