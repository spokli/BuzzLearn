package buzz.de.buzzlearn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buzz.de.buzzlearn.database.DbOperator;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbWriter;
import buzz.de.buzzlearn.database.FeedReaderContract;
import buzz.de.buzzlearn.entities.DbReaderInformation;
import buzz.de.buzzlearn.entities.DbWriterInformation;


public class InputActivity extends Activity implements DbWriter.DbDataReceiver,
        DbReader.DbDataReceiver,
        DbOperator.DbDataReceiver {

    private Button _addBuzz = null;
    private Button _clearDb = null;
    private Spinner _categorySelector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        _addBuzz = (Button) findViewById(R.id.btn_editCategory);
        _clearDb = (Button) findViewById(R.id.btn_clearDb);
        _categorySelector = (Spinner) findViewById(R.id.spn_categorySelector);

        // Disable add-button. Is only enabled if category has been selected
        _addBuzz.setEnabled(false);

        // Set event listeners of all views
        setAllEventListeners();

        // Fill category-spinner, so its height is set
        fillCategorySpinnerInitially();

        // Read Categorys from DB and fill spinner with real data
        selectDistinctCategorys(DbReader.SelectPurpose.CATEGORY_SELECTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeToDb(int id, String word, int points) {

        // Create a new map of values, where column names are the keys
        ContentValues content = new ContentValues();
        content.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ID, id);
        content.put(FeedReaderContract.FeedEntry.COLUMN_NAME_WORD, word);
        content.put(FeedReaderContract.FeedEntry.COLUMN_NAME_POINTS, points);

        // Create the array to pass to the asyncTask
        DbWriterInformation values = new DbWriterInformation(
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORYS,
                null,
                content
        );

        // Start asynchronous DB-communication
        DbWriter dbWriter = new DbWriter(this);
        dbWriter.execute(values);
    }

    private void readFromDb() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_WORD,
                FeedReaderContract.FeedEntry.COLUMN_NAME_POINTS,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                FeedReaderContract.FeedEntry.COLUMN_NAME_WORD + " DESC";

        String where = null;
        String[] whereValues = null;

//        String where = FeedReaderContract.FeedEntry.COLUMN_NAME_ID+"=?";
//        String[] whereValues = {"1"};

        DbReaderInformation values = new DbReaderInformation(
                false,                                               // Distinct values
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORYS, // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );

        DbReader dbReader = new DbReader(this, null);
        dbReader.execute(values);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void receiveOperateDbResponse() {
        Toast.makeText(this, "Daten gelöscht", Toast.LENGTH_LONG).show();
    }

    @Override
    public void receiveInsertDbResponse(Long newRowId) {
        if(newRowId >= 0) {
            Toast.makeText(this, "Neue Zeile " + newRowId + " eingefügt.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Dieser Eintrag ist schon vorhanden.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void receiveSelectDbResponse(Cursor c, SelectPurpose p) {

        Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        if(p != null) {
            switch (p) {
                default:
                    break;
            }
        }
        c.close();
    }





    private void setAllEventListeners(){
        _addBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View addBuzzView = getLayoutInflater().inflate(R.layout.popup_addbuzz, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
                builder.setTitle("NAME DES MAINBUZZ")
                        .setView(addBuzzView)
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.save),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {

                                        EditText wordView = (EditText) addBuzzView.findViewById(R.id.eT_synonymWord);
                                        EditText pointsView = (EditText) addBuzzView.findViewById(R.id.eT_synonymPoints);

                                        String word = wordView.getText().toString();
                                        int points = Integer.parseInt(pointsView.getText().toString());

                                        // Get ID of category from spinner
                                        int categoryId = Integer.parseInt("" + _categorySelector.getSelectedItem());

                                        // Save word to database
                                        writeToDb(categoryId, word, points);

                                        // read word from database
//                                        readFromDb();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        // Abbrechen
                                        dialog.cancel();
                                    }
                                });
                ;
                AlertDialog d = builder.create();
                d.show();
            }
        });

        _clearDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbOperator dbOperator = new DbOperator(InputActivity.this);
                dbOperator.execute();
            }
        });

        _categorySelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    // Initialstring wurde ausgewählt
                    _addBuzz.setEnabled(false);
                } else{
                    // Echter Eintrag wurde ausgewählt
                    _addBuzz.setEnabled(true);
                }
            }
        });
    }
}
