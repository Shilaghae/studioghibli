package application.daggerttest;

import com.google.gson.annotations.SerializedName;

/**
 * @author anna
 */
public class User {

    @SerializedName("id")
    int mId;

    @SerializedName("name")
    String mName;

    public User(int id, String name ) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
