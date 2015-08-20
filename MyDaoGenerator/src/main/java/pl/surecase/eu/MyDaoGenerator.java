package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.playing.lokasee");

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("object_id");
        user.addStringProperty("facebook_id");
        user.addStringProperty("name");
        user.addDoubleProperty("latitude");
        user.addDoubleProperty("longitude");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}