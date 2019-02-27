package buzz.de.buzzlearn.database;

/**
 * Created by Marco on 11.09.2015.
 */
public class DbReaderInformation {

    public boolean distinct;
    public String table;
    public String[] columns;
    public String where;
    public String[] whereValues;
    public String groupBy;
    public String having;
    public String orderBy;
    public String limit;


    public DbReaderInformation(boolean distinct, String table, String[] columns,
                               String where, String[] whereValues, String groupBy,
                               String having, String orderBy, String limit) {
        this.distinct = distinct;
        this.table = table;
        this.columns = columns;
        this.where = where;
        this.whereValues = whereValues;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }
}
