import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MongoDBConnector {
    public final String ID = "Id";
    MongoDatabase _db;
    public MongoDBConnector(String host, String dbName){
        MongoClient client = new MongoClient(host);
        _db = client.getDatabase(dbName);
    }

    public void SaveToCollection(String db_collection_name, Document data) {
        MongoCollection collection = _db.getCollection(db_collection_name);
        collection.insertOne(data);
    }

    public Document GetFromCollection(String db_collection_name, String key) throws Exception {
        List<Document> result = FilterCollection(db_collection_name, Filters.eq(ID, key));
        if(result.size() != 1){
            throw new Exception(String.format("Can't get element with key {0}", key));
        }
        return result.iterator().next();
    }

    public void UpdateCollection(String db_collection_name, String settingName, Document document) throws Exception {
        MongoCollection collection = _db.getCollection(db_collection_name);
        collection.updateOne(Filters.eq(ID, settingName), document);
    }

    public void RemoveFromCollection(String db_collection_name, String settingName) throws Exception {
        MongoCollection collection = _db.getCollection(db_collection_name);
        collection.deleteOne(Filters.eq(ID, settingName));
    }

    public List<Document> FilterCollection(String db_collection_name, Bson filter) {
        List<Document> result = new LinkedList<Document>();

        MongoCollection collection = _db.getCollection(db_collection_name);

        for(Iterator<Document> it = collection.find(filter).iterator(); it.hasNext();){
            result.add(it.next());
        }

        return result;
    }
}
