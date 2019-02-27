package buzz.de.buzzlearn.entities;

/**
 * Created by Marco on 06.01.2016.
 */
public class Word extends Entity {

    private String word;
    private Wordgroup wordgroup;
    private int wordgroupId;
    private int languageId;
    private int wordPoints;
    private boolean isGroupLabel;

    protected Word(int id, String word, int wordgroupId, int languageId, int wordPoints, boolean isGroupLabel){
        super(id);
        this.word = word;
        this.wordgroupId = wordgroupId;
        this.languageId = languageId;
        this.wordPoints = wordPoints;
        this.isGroupLabel = isGroupLabel;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getWordgroupId(){
        return this.wordgroupId;
    }

    public void addWordgroupReference(Wordgroup wg){
        this.wordgroup = wg;
    }

    public int getWordPoints() {
        return wordPoints;
    }

    public void setWordPoints(int wordPoints) {
        this.wordPoints = wordPoints;
    }

    public boolean getIsGroupLabel(){
        return this.isGroupLabel;
    }

}
