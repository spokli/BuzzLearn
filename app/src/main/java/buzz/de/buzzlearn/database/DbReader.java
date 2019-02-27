package buzz.de.buzzlearn.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import buzz.de.buzzlearn.R;

/**
 * Created by Marco on 11.09.2015.
 */
public class DbReader extends AsyncTask<DbReaderInformation, Void, Cursor> {

    DbDataReceiver callback;
    Resources res;
    ProgressDialog progDialog;
    SelectPurpose purpose;

    public enum SelectPurpose {ALL_CATEGORIES, ALL_WORDGROUPS, ALL_WORDS, ALL_CONNECTIONS, SINGLE_CATEGORY, SINGLE_WORDGROUP, SINGLE_WORD, SINGLE_CONNECTION, ALL};

    public interface DbDataReceiver {
        Context getContext();
        void receiveSelectDbResponse(Cursor c, SelectPurpose p);
    }

    public DbReader(DbDataReceiver callback, SelectPurpose purpose){
        this.callback = callback;
        res = callback.getContext().getResources();

        this.purpose = purpose;
    }

    @Override
    protected Cursor doInBackground(DbReaderInformation... values) {

        DatabaseHelper mDbHelper = new DatabaseHelper(callback.getContext());

        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.query(
                values[0].distinct,       // Distinct values
                values[0].table,          // The table to query
                values[0].columns,        // The columns to return
                values[0].where,          // The columns for the WHERE clause
                values[0].whereValues,    // The values for the WHERE clause
                values[0].groupBy,        // don't group the rows
                values[0].having,         // don't filter by row groups
                values[0].orderBy,        // The sort order
                values[0].limit           // Limit
        );

        return c;
    }

    @Override
    protected void onProgressUpdate(Void... nothing){
//        progDialog = new ProgressDialog(callback.getContext());
//        progDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                DbReader.this.cancel(true);
//            }
//        });
//
//        progDialog.setMessage(res.getString(R.string.selecting));
//        progDialog.setIndeterminate(true);
//        progDialog.setCancelable(true);
//        progDialog.setCanceledOnTouchOutside(false);
//        progDialog.show();
    }

    @Override
    protected void onPostExecute(Cursor c){
        // Close idle-dialog
//        if (progDialog != null && progDialog.isShowing()) {
//            progDialog.dismiss();
//        }

        callback.receiveSelectDbResponse(c, purpose);
    }
}
