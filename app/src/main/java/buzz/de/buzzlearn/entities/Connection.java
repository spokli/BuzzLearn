package buzz.de.buzzlearn.entities;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Marco on 10.09.2015.
 */
public class Connection extends Entity{

    private static HashMap<Integer, Connection> connections;

    private int wordgroup1id;
    private Wordgroup wordgroup1;
    private int wordgroup2id;
    private Wordgroup wordgroup2;
    private int categoryId;
    private Category category;
    private String description;
    private int points;

    protected Connection(int id, int wordgroup1id, int wordgroup2id, int categoryId, String description, int points){
        super(id);
        this.wordgroup1id = wordgroup1id;
        this.wordgroup1 = null;
        this.wordgroup2id = wordgroup2id;
        this.wordgroup2 = null;
        this.categoryId = categoryId;
        this.category = null;
        this.description = description;
        this.points = points;
    }

    public static void initialize(Context context){
        // Get all connections from db and store them into connections-map
    }

    public static Connection getConnectionById(int id){
        return connections.get(id);
    }

    public static Connection createNewConnection(int wordgroup1id, int wordgroup2id, int categoryId, String description, int points){
        // write to db
        int id = 0;

        // Create instance
        Connection newConnection = new Connection(id, wordgroup1id, wordgroup2id, categoryId, description, points);

        // add to connections-map
        connections.put(id, newConnection);

        return newConnection;
    }

    public int getRandomWordgroupId(){
        if(Math.random() < 0.5)
            return wordgroup1id;
        else return wordgroup2id;
    }

    public Wordgroup getRandomWordgroup(){
        if(Math.random() < 0.5)
            return wordgroup1;
        else return wordgroup2;
    }

    public int getOtherWordgroupId(int currentWordgroupId){
        if(wordgroup1id == currentWordgroupId)
            return wordgroup2id;
        else return wordgroup1id;
    }

    public Wordgroup getOtherWordgroup(int currentWordgroupId){
        if(wordgroup1id == currentWordgroupId)
            return wordgroup2;
        else return wordgroup1;
    }

    public int getPoints(){
        return points;
    }

    public String getDescription(){
        return description;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public static ArrayList<Connection> getConnectionsByIds(Collection<Integer> ids){
        ArrayList<Connection> result = new ArrayList<>();
        Iterator<Integer> iter = ids.iterator();
        Integer actualId;
        while((actualId = iter.next()) != null){
            result.add(connections.get(actualId));
        }
        return result;
    }

    protected void setWordgroup1(Wordgroup wg){
        this.wordgroup1 = wg;
    }

    protected void setWordgroup2(Wordgroup wg){
        this.wordgroup2 = wg;
    }

    protected void setCategory(Category c){
        this.category = c;
    }

}
