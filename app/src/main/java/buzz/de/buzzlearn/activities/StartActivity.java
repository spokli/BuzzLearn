package buzz.de.buzzlearn.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import buzz.de.buzzlearn.R;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.entities.Category;
import buzz.de.buzzlearn.entities.DbCallback;
import buzz.de.buzzlearn.entities.DataManager;

public class StartActivity extends Activity implements DbCallback {

    Button _btn_mode_input = null;
    Button _btn_mode_train = null;
    Button _btn_add_category = null;
    Spinner _spn_category_selector = null;
    HashMap<Integer, String> groupMap;
    ArrayList<Integer> categoryIds;

    DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        groupMap = new HashMap<>();
        categoryIds = new ArrayList<>();

        getViewReferences();
        setEventListeners();

        _btn_mode_input.setEnabled(false);
        _btn_mode_input.setActivated(false);
        _btn_mode_train.setEnabled(false);

        dm = new DataManager(this);

    }

    private void getViewReferences() {
        // Get button references
        _btn_mode_input = (Button) findViewById(R.id.btn_mode_input);
        _btn_mode_train = (Button) findViewById(R.id.btn_mode_train);
        _btn_add_category = (Button) findViewById(R.id.btn_add_category);
        _spn_category_selector = (Spinner) findViewById(R.id.spn_category_selector);
    }

    private void setEventListeners() {
        // Set event listeners
        _btn_mode_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedCategoryId() >= 0) {
                    Intent intent = new Intent(StartActivity.this, InputActivity.class);
                    intent.putExtra("categoryId", getSelectedCategoryId());
                    startActivity(intent);
                }
            }
        });

        _btn_mode_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, TrainActivity.class));
            }
        });

        _btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCategoryPopup();
            }
        });

        _spn_category_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category_text = "" + _spn_category_selector.getAdapter().getItem(position);

                // Set other buttons active
                _btn_mode_input.setEnabled(true);
                _btn_mode_train.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getSelectedCategoryId() {
        int itemPos = _spn_category_selector.getSelectedItemPosition();

        // Get actual item id from the itemId-index-map
        int categoryId = categoryIds.get(itemPos);

        return categoryId;
    }

    /*
    Should only be called by newCategoriesArrived-Method
     because it needs a filled dm.categoryManager.entities
     */
    protected void fillCategorySelector() {

        clearOldCategoryLists();

        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("Bitte Kategorie auswählen");

        // Read selected categories from DB
        ArrayList<Category> categories = dm.categoryManager.getEntities();

        // Get names of categories to fill spinner
        Iterator<Category> iter = categories.iterator();
        Category c;
        categoryIds.add(-1);
        while (iter.hasNext()) {
            c = iter.next();
            String category = c.getName();
            int category_id = c.getId();

            categoryList.add(category);
            groupMap.put(category_id, category);
            categoryIds.add(category_id);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryList);

        _spn_category_selector.setAdapter(arrayAdapter);

        _spn_category_selector.setSelection(0);

        arrayAdapter.notifyDataSetChanged();
        _spn_category_selector.invalidate();
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

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void newEntitiesArrived(int newItemId, DbReader.SelectPurpose purpose) {

        switch(purpose){
            case ALL:

                fillCategorySelector();
                break;

            case SINGLE_CATEGORY:
                fillCategorySelector();
                _spn_category_selector.setSelection(newItemId+1);
                break;

            default:
                break;
        }
    }

    private void openAddCategoryPopup(){
        // open popup with input fields
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Category");

        // Set up the input
        final EditText categoryNameInput = new EditText(getContext());

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        categoryNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(categoryNameInput);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dm.categoryManager.insertNewCategory(categoryNameInput.getText().toString(), 0, 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void clearOldCategoryLists() {
        groupMap.clear();
        categoryIds.clear();
    }

    public Context getContext() {
        return this;
    }

}
