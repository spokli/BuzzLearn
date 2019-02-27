package buzz.de.buzzlearn.entities;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Marco on 20.12.2015.
 */
public class Category extends Entity {

    private int id;
    private String name;
    private int sum;
    private int win;
    private HashMap<Integer, Wordgroup> wordgroups;

    protected Category(int id, String name, int sum, int win) {
        super(id);
        this.name = name;
        this.sum = sum;
        this.win = win;
        this.wordgroups = new HashMap<>();
    }


    public String getName() {
        return name;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public HashMap<Integer, Wordgroup> getWordgroups(){
        return this.wordgroups;
    }

    public void addWordgroupReference(Wordgroup w){
        this.wordgroups.put(w.getId(), w);
    }

}
