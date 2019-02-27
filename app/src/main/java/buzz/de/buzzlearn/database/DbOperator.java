package buzz.de.buzzlearn.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import buzz.de.buzzlearn.R;

/**
 * Created by Marco on 11.09.2015.
 */
public class DbOperator extends AsyncTask<Void, Void, Void> {

    DbDataReceiver callback;
    Resources res;
    ProgressDialog progDialog;

    public interface DbDataReceiver {
        Context getContext();

        void receiveOperateDbResponse();
    }

    public DbOperator(DbDataReceiver callback) {
        this.callback = callback;
        res = callback.getContext().getResources();
    }

    @Override
    protected Void doInBackground(Void... values) {

        DatabaseHelper mDbHelper = new DatabaseHelper(callback.getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

//        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_CATEGORIES);
//        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_CONNECTIONS);
//        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_LANGUAGES);
//        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_WORDGROUPS);
//        db.execSQL(DatabaseDefinition.Definition.SQL_DROP_TABLE_WORDS);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... nothing) {
        progDialog = new ProgressDialog(callback.getContext());
        progDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DbOperator.this.cancel(true);
            }
        });

        progDialog.setMessage(res.getString(R.string.operating));
        progDialog.setIndeterminate(true);
        progDialog.setCancelable(true);
        progDialog.setCanceledOnTouchOutside(false);
        progDialog.show();
    }

    @Override
    protected void onPostExecute(Void value) {
        // Close idle-dialog
        if (progDialog != null && progDialog.isShowing()) {
            progDialog.dismiss();
        }

        callback.receiveOperateDbResponse();

    }
}
