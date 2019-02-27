package buzz.de.buzzlearn.entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marco on 10.09.2015.
 */
public class Wordgroup extends Entity {

    private HashMap<String, Word> words;
    private String groupLabel;
    private ArrayList<Integer> connectionIds;
    private ArrayList<Connection> connections;
    private HashMap<Integer, Category> categoryReferences;
    private HashMap<Integer, Boolean> categoryIds;

    protected Wordgroup(int id, int categoryId, boolean isMainBuzz){
        super(id);

        this.categoryIds = new HashMap<>();
        this.categoryIds.put(categoryId, isMainBuzz);

        this.words = new HashMap<>();
        this.groupLabel = "Untitled wordgroup";
        this.connectionIds = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.categoryReferences = new HashMap<>();
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public HashMap<String, Word> getWords(){
        return words;
    }

    public ArrayList<Integer> getConnectionIds(){
        return connectionIds;
    }

    public ArrayList<Connection> getConnections(){
        return this.connections;
    }

    protected void addWord(Word word){
        this.words.put(word.getWord(), word);
    }

    protected void addConnectionId(int id){
        this.connectionIds.add(id);
    }

    protected void addConnection(Connection c){
        this.connections.add(c);
    }

    protected void addCategoryReference(Category c){
        this.categoryReferences.put(c.getId(), c);
    }

    protected void addCategoryId(int categoryId, boolean isMainBuzz){
        this.categoryIds.put(categoryId, isMainBuzz);
    }

    public HashMap<Integer, Boolean> getCategoryIds(){
        return categoryIds;
    }

    protected void setGroupLabel(String groupLabel){
        this.groupLabel = groupLabel;
    }

}
