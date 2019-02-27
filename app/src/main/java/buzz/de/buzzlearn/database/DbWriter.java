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
public class DbWriter extends AsyncTask<DbWriterInformation, Void, Long> {

    DbDataReceiver callback;
    Resources res;
    ProgressDialog progDialog;

    public interface DbDataReceiver {
        Context getContext();
        void receiveInsertDbResponse(Long newRowId);
    }

    public DbWriter(DbDataReceiver callback){
        this.callback = callback;
        res = callback.getContext().getResources();
    }

    @Override
    protected Long doInBackground(DbWriterInformation... values) {

        DatabaseHelper mDbHelper = new DatabaseHelper(callback.getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow(
                    values[0].table,
                    values[0].nullColumnHack,
                    values[0].values);
        } catch(SQLException e){
            e.printStackTrace();
            // Zusatzerrorhandling möglich
        }

        return newRowId;
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
    protected void onPostExecute(Long newRowId){
        // Close idle-dialog
//        if (progDialog != null && progDialog.isShowing()) {
//            progDialog.dismiss();
//        }

        callback.receiveInsertDbResponse(newRowId);
    }
}
