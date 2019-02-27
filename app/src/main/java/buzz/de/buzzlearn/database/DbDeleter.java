package buzz.de.buzzlearn.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import buzz.de.buzzlearn.R;

/**
 * Created by Marco on 11.09.2015.
 */
public class DbDeleter extends AsyncTask<DbDeleterInformation, Void, Long> {

    DbDataReceiver callback;
    Resources res;
    ProgressDialog progDialog;
    DeletePurpose purpose;

    public enum DeletePurpose{SINGLE_WORD, SINGLE_WORDGROUP, SINGLE_CATEGORY, SINGLE_CONNECTION};

    public interface DbDataReceiver {
        Context getContext();
        void receiveDeleteDbResponse(Long deletedRowId, int secondaryKey, DeletePurpose purpose);
    }

    public DbDeleter(DbDataReceiver callback, DeletePurpose purpose){
        this.callback = callback;
        res = callback.getContext().getResources();
        this.purpose = purpose;
    }

    @Override
    protected Long doInBackground(DbDeleterInformation... values) {

        DatabaseHelper mDbHelper = new DatabaseHelper(callback.getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long deletedRowId = -1;
        try {
            deletedRowId = db.delete(
                    values[0].tableName,
                    values[0].whereClause,
                    values[0].whereArgs);
        } catch(SQLException e){
            e.printStackTrace();
            // Zusatzerrorhandling möglich
        }

        return deletedRowId;
    }

    @Override
    protected void onProgressUpdate(Void... nothing){
//        progDialog = new ProgressDialog(callback.getContext());
//        progDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                DbWriter.this.cancel(true);
//            }
//        });
//
//        progDialog.setMessage(res.getString(R.string.inserting));
//        progDialog.setIndeterminate(true);
//        progDialog.setCancelable(true);
//        progDialog.setCanceledOnTouchOutside(false);
//        progDialog.show();
    }

    @Override
    protected void onPostExecute(Long deletedRowId){
        // Close idle-dialog
//        if (progDialog != null && progDialog.isShowing()) {
//            progDialog.dismiss();
//        }

        callback.receiveDeleteDbResponse(deletedRowId, -1, purpose);
    }
}
