package csdc.in.greendao_generator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class Main {
    public static void main(String[] args) throws Exception{


        try {
            Schema schema = new Schema(1, "in.terek.photoapp.db");
            schema.enableKeepSectionsByDefault();


            Entity file = schema.addEntity("ImageFiles");
            file.addIdProperty();
            file.addLongProperty("fileName");
            file.addStringProperty("localUriPath");
            file.addStringProperty("serverUriPath");
            file.addDateProperty("creationDate");



            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
