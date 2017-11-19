import Settings.Resource;
import Settings.SettingsTreeItemType;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MongoDBConnectorTest {

    @Test
    void firstJavaTestEver() {
        assertEquals(2, 1+1);
    }

    @Test
    void mongoDbWriteTest(){
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("test");
        db.createCollection("testCollection");
        MongoCollection collection = db.getCollection("testCollection");
        Document object = new Document("first", "one");
        collection.insertOne(object);
        client.close();
    }

    @Test
    void mongoDbReadTest(){
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("test");
        MongoCollection collection = db.getCollection("testCollection");
        FindIterable iterable = collection.find();
        Iterator it = iterable.iterator();
        List<String> result = new LinkedList<>();
        while (it.hasNext()){
            result.add(it.next().toString());
        }
    }

    @Test
    void mongoDbUpdateTest(){
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("test");
        MongoCollection collection = db.getCollection("testCollection");
        //Document object = new Document("second", new Resource("123", "samumax", "noone"));
        //collection.updateOne(Filters.eq("id", "first"), object);
        Document object = new Document("first", "one");
        collection.insertOne(object);
        client.close();
    }

    @Test
    void objectToJsonStringAndBack(){
        //Document doc = new Document("key", new Resource("123", "samumax", "noone"));
    }

    @Test
    void enumConversion()
    {
        String enumStr = SettingsTreeItemType.GroupSettings.name();
        SettingsTreeItemType enumItem = SettingsTreeItemType.valueOf(enumStr);
        //SettingsTreeItemType nullEnum = SettingsTreeItemType.valueOf("some text");
    }

    @Test
    void documentParse()
    {
        Document doc = new Document("first", "one").append("second", "two");
        String existedParam = doc.getString("first");
        existedParam = doc.getString("second");
        String notExistedParam = doc.getString("third");
    }

}