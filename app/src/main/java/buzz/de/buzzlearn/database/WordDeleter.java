package buzz.de.buzzlearn.database;

/**
 * Created by Marco on 14.04.2016.
 */
public class WordDeleter extends DbDeleter {

    int wordgroupId;

    public WordDeleter(DbDataReceiver callback, DeletePurpose purpose, int wordgroupId){
        super(callback, purpose);
        this.wordgroupId = wordgroupId;
    }

    @Override
    protected void onPostExecute(Long deletedRowId){
        // Close idle-dialog
//        if (progDialog != null && progDialog.isShowing()) {
//            progDialog.dismiss();
//        }

        callback.receiveDeleteDbResponse(deletedRowId, wordgroupId, purpose);
    }

}
