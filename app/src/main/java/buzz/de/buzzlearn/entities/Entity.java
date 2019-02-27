package buzz.de.buzzlearn.entities;

/**
 * Created by Marco on 14.04.2016.
 */
public abstract class Entity {

    private int id;

    protected Entity(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
