package acadgild.imdb.Model;

import java.io.Serializable;

/**
 * Created by Tungenwar on 15/05/2015.
 */
public class Casts implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String character;
    private String order;
    private String cast_id;
    private String profile_path;

    public Casts() {
        this.setId("");
        this.setName("");
        this.setCharacter("");
        this.setOrder("");
        this.setCastId("");
        this.setProfilePath("");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCastId() {
        return cast_id;
    }

    public void setCastId(String cast_id) {
        this.cast_id = cast_id;
    }

    public String getProfilePath() {
        return profile_path;
    }

    public void setProfilePath(String profile_path) {
        this.profile_path = profile_path;
    }
}
