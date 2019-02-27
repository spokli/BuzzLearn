package buzz.de.buzzlearn.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import buzz.de.buzzlearn.R;
import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbReader;
import buzz.de.buzzlearn.entities.Category;
import buzz.de.buzzlearn.entities.Connection;
import buzz.de.buzzlearn.entities.DbCallback;
import buzz.de.buzzlearn.entities.DataManager;
import buzz.de.buzzlearn.entities.Word;
import buzz.de.buzzlearn.entities.Wordgroup;


public class InputActivity extends Activity implements DbCallback {

    TextView _text_category_value = null;
    TextView _text_wordgroup_label = null;
    EditText _etext_search_word = null;
    ListView _list_wordgroupSelector = null;
    TabHost _tabhost = null;
    CheckBox _cbox_mainbuzz = null;
    ListView _list_synonyms = null;
    ListView _list_links = null;
//    Button _btn_save = null;
//    Button _btn_cancel = null;

    String tmp_word;
    int tmp_points;

    private Category category;
    private Wordgroup wordgroup;

    HashMap<Integer, String> groupMap;
    ArrayList<Integer> wordIdsForList;
    ArrayList<Integer> wordgroupIdsForList;

//    CategoryManager categoryManager;
//    WordgroupManager wordgroupManager;
//    ConnectionManager connectionManager;

    DataManager dm;
    ProgressDialog progDialog;
    boolean tmp_doNothing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        getViewReferences();
        setEventListeners();

        groupMap = new HashMap<>();
        wordgroupIdsForList = new ArrayList<>();


        _tabhost.setup();
        _tabhost.addTab(_tabhost.newTabSpec("tabDetails").setContent(R.id.tabDetails).setIndicator("Details"));
        _tabhost.addTab(_tabhost.newTabSpec("tabSynonyms").setContent(R.id.tabSynonyms).setIndicator("Synonyme"));
        _tabhost.addTab(_tabhost.newTabSpec("tabLinks").setContent(R.id.tabLinks).setIndicator("Links"));
        _tabhost.setVisibility(View.GONE);

//        _btn_save.setVisibility(View.GONE);
//        _btn_cancel.setVisibility(View.GONE);

        _text_wordgroup_label.setVisibility(View.GONE);

        dm = new DataManager(this);

        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Selektiere");
        progDialog.setIndeterminate(true);
        progDialog.setCancelable(false);
        progDialog.setCanceledOnTouchOutside(false);
        progDialog.show();

    }

    private void getViewReferences() {
        _text_category_value = (TextView) this.findViewById(R.id.text_category_value);
        _text_wordgroup_label = (TextView) this.findViewById(R.id.text_wordgroup_label);
        _list_wordgroupSelector = (ListView) this.findViewById(R.id.list_wordgroupSelector);
        _tabhost = (TabHost) this.findViewById(android.R.id.tabhost);
        _cbox_mainbuzz = (CheckBox) this.findViewById(R.id.cbox_mainbuzz);
        _list_synonyms = (ListView) this.findViewById(R.id.list_synonyms);
        _list_links = (ListView) this.findViewById(R.id.list_links);
        _etext_search_word = (EditText) this.findViewById(R.id.etext_search_word);
//        _btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
//        _btn_save = (Button) this.findViewById(R.id.btn_save);

    }

    private void setEventListeners() {

        _etext_search_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                final String textBefore = s.toString();
//
//                // If we're in input mode
////                if (_btn_save.getVisibility() != View.VISIBLE) {
////                    return;
////                }
//                // Check if user did some input and ask if he really wants to exit
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Save changes?");
//                builder.setCancelable(false);
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//
//                        // Revert input
//                        _etext_search_word.setText(textBefore);
////                        _etext_search_word.cancelPendingInputEvents();
//                        tmp_doNothing = true;
//                    }
//                });
//
//                builder.setNeutralButton("Discard Changes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                // Set up the buttons
//                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        saveWordgroup();
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!tmp_doNothing) {
                    fillWordgroupSelector(s.toString());
                }
                tmp_doNothing = false;
            }
        });

        _list_wordgroupSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get real wordgroup-id from selected item position
                int wordgroupId = wordgroupIdsForList.get(position);

                // Empty wg-selector
                clearWordgroupSelector();

                if (wordgroupId == -1) {
                    // Open popup for new word to create new wordgroup
                    openWordPopup(true, null);

                    // Continuing in newEntitesArrived-Method
                } else {
                    showWordgroupDetails(dm.wordgroupManager.getEntityById(wordgroupId));
                }


            }
        });

        _tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
//                LinearLayout currentTabView = (LinearLayout) _tabhost.getCurrentTabView();
                switch (_tabhost.getCurrentTabTag()) {
                    case "tabDetails":
                        break;
                    case "tabSynonyms":
                        break;
                    case "tabLinks":
                        break;
                    default:
                        break;
                }
            }
        });

//        _text_wordgroup_value.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _text_wordgroup
//            }
//        })

        _list_synonyms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // New word
                    openWordPopup(false, null);

                } else {
                    // Edit item
                    Word clickedWord = dm.wordManager.getEntityById(wordIdsForList.get(position));
                    openSynonymOptionsPopup(clickedWord);
                }
            }
        });

//        _list_synonyms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });

//        _btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Create new or edit existing wordgroup
//
//
////                _tabhost.setVisibility(View.GONE);
//                _btn_save.setVisibility(View.GONE);
//            }
//        });
    }

    protected void fillWordgroupSelector(String search) {

        _tabhost.setVisibility(View.GONE);

        ArrayList<String> wordgroupList = new ArrayList<>();
        wordgroupIdsForList.clear();

        if (search == null) {
            search = "";
        }

        int i = 0;

        if (!search.isEmpty()) {
            wordgroupList = new ArrayList<>();
            wordgroupList.add("'" + search + "' hinzufügen...");
            wordgroupIdsForList.add(i++, -1);
        }

        // Get names of wordgroups to fill list
        Iterator<Wordgroup> iter = dm.wordgroupManager.getEntities().iterator();
        Wordgroup wg;
        while (iter.hasNext()) {
            wg = iter.next();
            String wordgroup = wg.getGroupLabel();
            int wordgroup_id = wg.getId();

            // Filter by input String
            if (!wordgroup.contains(search)) {
                continue;
            }
            wordgroupList.add(wordgroup);
            groupMap.put(wordgroup_id, wordgroup);
            wordgroupIdsForList.add(i++, wordgroup_id);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, wordgroupList);
        _list_wordgroupSelector.setAdapter(arrayAdapter);

        _list_wordgroupSelector.setSelection(0);
        _list_wordgroupSelector.invalidate();

    }

    private void clearWordgroupSelector() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        _list_wordgroupSelector.setAdapter(arrayAdapter);

        _list_wordgroupSelector.setSelection(0);
        _list_wordgroupSelector.invalidate();

        groupMap.clear();
        wordgroupIdsForList.clear();

        _tabhost.requestFocus();
    }

    private void showWordgroupDetails(Wordgroup wordgroup) {

        if (wordgroup != null) {
            this.wordgroup = wordgroup;
        }

        _tabhost.setVisibility(View.VISIBLE);

        // Fill wordgroup heading
        tmp_doNothing = true;
        _etext_search_word.setText(this.wordgroup.getGroupLabel());
        _text_wordgroup_label.setVisibility(View.VISIBLE);


        // Check if wordgroup is mainbuzz in this category
        _cbox_mainbuzz.setActivated(this.wordgroup.getCategoryIds().get(category.getId()));

        // Create Entries in synonyms- and links-lists
        ArrayList<String> synonymList = new ArrayList<>();
        synonymList.add("Synonym hinzufügen...");

        Iterator<Map.Entry<String, Word>> iter = this.wordgroup.getWords().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Word> entry = iter.next();
            synonymList.add(entry.getKey());
            wordIdsForList.add(entry.getValue().getId());
        }

        ArrayAdapter<String> synonymArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, synonymList);
        _list_synonyms.setAdapter(synonymArrayAdapter);
        _list_synonyms.setSelection(0);
        _list_synonyms.invalidate();


        ArrayList<String> linkList = new ArrayList<>();
        linkList.add("Verknüpfung hinzufügen...");

        for (Connection c : this.wordgroup.getConnections()) {
            linkList.add(c.getOtherWordgroup(this.wordgroup.getId()).getGroupLabel());
        }

        ArrayAdapter<String> linkArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, linkList);
        _list_links.setAdapter(linkArrayAdapter);
        _list_links.setSelection(0);
        _list_links.invalidate();

    }

    private void openWordPopup(boolean newWordgroup, Word word) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View popupView = getLayoutInflater().inflate(R.layout.popup_word, null);
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();

        TextView _txt_heading = (TextView) popupView.findViewById(R.id.txt_heading);
        Button _btn_save_synonym = (Button) popupView.findViewById(R.id.btn_save);
        TextView _txt_wordgroup = (TextView) popupView.findViewById(R.id.txt_wordgroup);
        final EditText _etxt_input = (EditText) popupView.findViewById(R.id.etxt_input);
        final NumberPicker _numpick_points = (NumberPicker) popupView.findViewById(R.id.numpick_points);
        Spinner _spn_language = (Spinner) popupView.findViewById(R.id.spn_language);

        final boolean newWordgroupFinal = newWordgroup;

        if (newWordgroup) {
            // The name of the new wordgroup has already been entered, so use it
            _txt_heading.setText("Neues Wort");
            _txt_wordgroup.setVisibility(View.INVISIBLE);
            _etxt_input.setText(_etext_search_word.getText());
        } else {
            _txt_heading.setText("Synonym für ");
            _txt_wordgroup.setText(wordgroup.getGroupLabel());

            // If a word is given, we are in edit-mode
            if (word != null) {
                _etxt_input.setText(word.getWord());
                _etxt_input.setEnabled(false);

                _numpick_points.setValue(word.getWordPoints());
//            _spn_language.setSelection();
            }
        }


        // Fill and configure popup-views
        _numpick_points.setMinValue(1);
        _numpick_points.setMaxValue(3);
        _numpick_points.setWrapSelectorWheel(false);

        _btn_save_synonym.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                tmp_word = _etxt_input.getText().toString();
                tmp_points = _numpick_points.getValue();

                dialog.dismiss();

                if (newWordgroupFinal) {
                    // Insert the wordgroup first. After this is done, we can insert the word (newEntitiesArrived(..))
                    dm.wordgroupManager.insertNewWordgroup(category.getId(), true);
                } else {
                    // If we only want to add a synonym, we need to add a word
                    dm.wordManager.insertNewWord(_etxt_input.getText().toString(), InputActivity.this.wordgroup.getId(), 0, _numpick_points.getValue(), false);
                }
            }
        });
        dialog.show();
    }

    private void openSynonymOptionsPopup(final Word clickedWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View popupView = getLayoutInflater().inflate(R.layout.popup_options_synonym, null);
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();

        Button _btn_editWord = (Button) popupView.findViewById(R.id.btn_editWord);
        Button _btn_deleteWord = (Button) popupView.findViewById(R.id.btn_deleteWord);

        _btn_editWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWordPopup(false, clickedWord);
            }
        });

        _btn_deleteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.wordManager.deleteEntity(clickedWord, DbDeleter.DeletePurpose.SINGLE_WORD);
            }
        });
    }

    @Override
    public void newEntitiesArrived(int newItemId, DbReader.SelectPurpose purpose) {

        switch (purpose) {
            case ALL:
                Intent i = this.getIntent();
                int categoryId = i.getIntExtra("categoryId", -1);
                category = dm.categoryManager.getEntityById(categoryId);
                _text_category_value.setText(category.getName());

                fillWordgroupSelector(null);

                progDialog.cancel();
                break;

            case SINGLE_WORDGROUP:

                // Add word
                dm.wordManager.insertNewWord(tmp_word, newItemId, 0, tmp_points, true);
                break;

            case SINGLE_WORD:

                showWordgroupDetails(dm.wordgroupManager.getEntityById(newItemId));
                break;
        }


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


    public Context getContext() {
        return this;
    }
}
