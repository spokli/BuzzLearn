package buzz.de.buzzlearn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import buzz.de.buzzlearn.database.DbOperator;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.database.DbWriter;
import buzz.de.buzzlearn.database.FeedReaderContract;
import buzz.de.buzzlearn.entities.DbReaderInformation;


public class StartActivity extends Activity implements DbReader.DbDataReceiver{

    private Button _btn_mode_input = null;
    private Button _btn_mode_train = null;
    private Button _btn_add_category = null;
    private Spinner _spinner_category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Get button references
        _btn_mode_input = (Button) findViewById(R.id.btn_mode_input);
        _btn_mode_train = (Button) findViewById(R.id.btn_mode_train);
        _btn_add_category = (Button) findViewById(R.id.btn_add_category);
        _spinner_category = (Spinner) findViewById(R.id.spinner_category);

        // Disable mode-buttons. Is only enabled if category has been selected
        _btn_mode_input.setEnabled(false);
        _btn_mode_train.setEnabled(false);

        // Set event listeners of all views
        setAllEventListeners();

        // Fill category-spinner, so its height is set
        fillCategorySpinnerInitially();

        // Read Categorys from DB and fill spinner with real data
        selectDistinctCategorys(DbReader.SelectPurpose.CATEGORY_SELECTOR);
    }

    public void setAllEventListeners(){
        // Set event listeners
        _spi

        _btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _btn_mode_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, InputActivity.class));
            }
        });

        _btn_mode_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, TrainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mode_selector, menu);
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

    public void receiveSelectDbResponse(Cursor c, DbReader.SelectPurpose p) {

        Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        if(p != null) {
            switch (p) {
                case CATEGORY_SELECTOR:
                    ArrayList<String> groups = new ArrayList<String>();
                    while (c.moveToNext() == true) {
                        groups.add("" + c.getInt(0));
                    }
                    fillCategorySpinner(groups);
                    break;
                default:
                    break;
            }
        }

        c.close();
    }

    private void selectDistinctCategorys(DbReader.SelectPurpose purpose) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_ID,
        };

        // How you want the results sorted in the resulting Cursor
        String orderBy =
                FeedReaderContract.FeedEntry.COLUMN_NAME_ID + " ASC";

        String where = null;
        String[] whereValues = null;

//        String where = FeedReaderContract.FeedEntry.COLUMN_NAME_ID+"=?";
//        String[] whereValues = {"1"};

        DbReaderInformation values = new DbReaderInformation(
                true,                                               // Distinct values
                FeedReaderContract.FeedEntry.TABLE_NAME_CATEGORIES, // The table to query
                columns,                                            // The columns to return
                where,                                              // The columns for the WHERE clause
                whereValues,                                        // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                orderBy,                                            // The sort order
                null                                                // Limit
        );

        DbReader dbReader = new DbReader(this, purpose);
        dbReader.execute(values);
    }

    private void fillCategorySpinnerInitially(){
        ArrayList<String> initialString = new ArrayList<String>();
        initialString.add(this.getResources().getString(R.string.add_category));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, initialString);
        _spinner_category.setAdapter(arrayAdapter);
        _spinner_category.setSelection(0);
    }

    private void fillCategorySpinner(ArrayList<String> groups) {

        ArrayAdapter<String> adapter = (ArrayAdapter <String>) _spinner_category.getAdapter();

        adapter.addAll(groups);

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, groups);
//        _categorySelector.setAdapter(arrayAdapter);

        _spinner_category.setSelection(0);

        adapter.notifyDataSetChanged();
        _spinner_category.invalidate();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
