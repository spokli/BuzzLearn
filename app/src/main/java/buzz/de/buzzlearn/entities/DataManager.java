package buzz.de.buzzlearn.entities;

import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import buzz.de.buzzlearn.database.DbDeleter;
import buzz.de.buzzlearn.database.DbReader;

/**
 * Created by Marco on 22.01.2016.
 */
public class DataManager implements DbCallback{
//    private static DataManager instance;
//    private DataManager(){}
//    public DataManager initialize(){
//        if(DataManager.instance == null){
//            DataManager.instance = new DataManager();
//        }
//
//        return DataManager.instance ;
//    }

    public CategoryManager categoryManager;
    public WordgroupManager wordgroupManager;
    public WordManager wordManager;
    public ConnectionManager connectionManager;
    public DbCallback uiCallback;

    private boolean categoriesArrived = false;
    private boolean wordgroupsArrived = false;
    private boolean wordsArrived = false;
    private boolean connectionsArrived = false;


    public DataManager(DbCallback callback) {

        uiCallback = callback;

        // Give this as callback for newEntitiesArrived
        categoryManager = new CategoryManager(this);
        wordgroupManager = new WordgroupManager(this);
        wordManager = new WordManager(this);
        connectionManager = new ConnectionManager(this);

    }

    @Override
    public void newEntitiesArrived(int newItemId, DbReader.SelectPurpose purpose) {
        switch(purpose){
            case ALL_CATEGORIES:
                categoriesArrived = true;
                checkForAllEntitiesArrived();
                break;
            case ALL_WORDGROUPS:
                wordgroupsArrived = true;
                checkForAllEntitiesArrived();
                break;
            case ALL_WORDS:
                wordsArrived = true;
                checkForAllEntitiesArrived();
                break;
            case ALL_CONNECTIONS:
                connectionsArrived = true;
                checkForAllEntitiesArrived();
                break;

            case SINGLE_CATEGORY:
                // Call uiCallback and tell it that the inserted Entity is available now
                uiCallback.newEntitiesArrived(newItemId, purpose);
                break;
            case SINGLE_WORDGROUP:
                // When a new wordgroup has been created, we have to add its reference to the
                // belonging category
                addNewWordgroupToCategory(newItemId);

                // Call uiCallback and tell it that the inserted Entity is available now
                uiCallback.newEntitiesArrived(newItemId, purpose);
                break;
            case SINGLE_WORD:
                // When a new word has been created, we have to add its reference to the
                // belonging wordgroup
                addNewWordToWordgroup(newItemId);

                uiCallback.newEntitiesArrived(newItemId, purpose);
                break;
            case SINGLE_CONNECTION:
                // Call uiCallback and tell it that the inserted Entity is available now
                uiCallback.newEntitiesArrived(newItemId, purpose);
                break;

            default:
                break;
        }

    }

    @Override
    public void entityDeleted(int deletedItemId, int secondaryKey, DbDeleter.DeletePurpose purpose) {
        switch (purpose) {
            case SINGLE_WORD:
                // Wort wurde aus DB gelöscht, also Wort auch aus Wordgroup löschen

                break;
        }
    }

    private void checkForAllEntitiesArrived(){
        if(categoriesArrived && wordgroupsArrived && wordsArrived && connectionsArrived){

            // Now fill the reference maps
            fillReferences();

            // Call uiCallback and tell it that we are ready to start with all entites
            uiCallback.newEntitiesArrived(-1, DbReader.SelectPurpose.ALL);

            categoriesArrived = false;
            wordgroupsArrived = false;
            wordsArrived = false;
            connectionsArrived = false;
        }
    }

    private void fillReferences(){
        // References between Category and Wordgroup
        for(Wordgroup wordgroup : wordgroupManager.getEntities()){
            Iterator<Integer> iter = wordgroup.getCategoryIds().keySet().iterator();
            while(iter.hasNext()){
                int categoryId = iter.next();
                Category category = categoryManager.getEntityById(categoryId);

                // Add wordgroup-reference to category-object
                category.addWordgroupReference(wordgroup);

                // Add category-reference to wordgroup-object
                wordgroup.addCategoryReference(category);
            }
        }

        // References between Word and Wordgroup
        for(Word word : wordManager.getEntities()){
            Wordgroup wordgroup = wordgroupManager.getEntityById(word.getWordgroupId());
            word.addWordgroupReference(wordgroup);

            wordgroup.addWord(word);

            if(word.getIsGroupLabel()){
                wordgroup.setGroupLabel(word.getWord());
            }
        }


        // References between Connection and Wordgroup
        for(Connection con : connectionManager.getEntities()){
            // Read both wordgroups from each connection and add reference of connection
            // to the wordgroup
            int wordgroup1Id = con.getRandomWordgroupId();
            Wordgroup wordgroup1 = wordgroupManager.getEntities().get(wordgroup1Id);
            wordgroup1.addConnectionId(con.getId());
            wordgroup1.addConnection(con);

            int wordgroup2Id = con.getOtherWordgroupId(wordgroup1Id);
            Wordgroup wordgroup2 = wordgroupManager.getEntities().get(wordgroup2Id);
            wordgroup2.addConnectionId(con.getId());
            wordgroup2.addConnection(con);

            // Add reference of wordgroups to connection
            con.setWordgroup1(wordgroup1);
            con.setWordgroup2(wordgroup2);

            // Add reference of Category to connection
            int categoryId = con.getCategoryId();
            Category cat = categoryManager.getEntities().get(categoryId);
            con.setCategory(cat);
        }
    }

    private void addNewWordgroupToCategory(int wordgroupId){
        Wordgroup wordgroup = wordgroupManager.getEntityById(wordgroupId);

        Iterator<Integer> iter = wordgroup.getCategoryIds().keySet().iterator();
        while(iter.hasNext()){
            int categoryId = iter.next();
            Category category = categoryManager.getEntityById(categoryId);
            category.addWordgroupReference(wordgroup);

            wordgroup.addCategoryReference(category);
        }
    }

    private void addNewWordToWordgroup(int wordId){
        Word word = wordManager.getEntityById(wordId);

        Wordgroup wordgroup = wordgroupManager.getEntityById(word.getWordgroupId());
        word.addWordgroupReference(wordgroup);
        wordgroup.addWord(word);

        if(word.getIsGroupLabel()){
            wordgroup.setGroupLabel(word.getWord());
        }
    }

    @Override
    public Context getContext() {
        return uiCallback.getContext();
    }
}
