import Settings.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.json.JSONException;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SettingsStorageManager {

    private MongoDBConnector _dbConnector;
    public final String DB_COLLECTION_NAME = "Settings";

    public SettingsStorageManager(MongoDBConnector connector){
        _dbConnector = connector;
    }

    public void AddGroup(String parent, String name){
        _dbConnector.SaveToCollection(DB_COLLECTION_NAME,
                SettingsTreeItemFactory.convertToDocument(new GroupSettings(name, parent, "")));
    }

    public void AddUser(String parent, String name){
        _dbConnector.SaveToCollection(DB_COLLECTION_NAME,
                SettingsTreeItemFactory.convertToDocument(new UserSettings(name, parent, "")));
    }

    public void AddResource(String ownerName, String name) throws Exception {
        SettingsTreeItem owner = SettingsTreeItemFactory.convertToSetting(
                _dbConnector.GetFromCollection(DB_COLLECTION_NAME, ownerName));
        switch (owner.getItemType()){
            case UserSettings:
                _dbConnector.SaveToCollection(DB_COLLECTION_NAME,
                        SettingsTreeItemFactory.convertToDocument(new Resource(name, owner.get_parent(), "", ownerName)));
                break;
            case GroupSettings:
                _dbConnector.SaveToCollection(DB_COLLECTION_NAME,
                        SettingsTreeItemFactory.convertToDocument(new Resource(name, ownerName, "", ownerName)));
                break;
            default:
                throw new IllegalArgumentException("Wrong owner name");
        }
    }

    public void SetSetting(String settingName, String settings) throws Exception {
        SettingsTreeItem setting =
                SettingsTreeItemFactory.convertToSetting(_dbConnector.GetFromCollection(DB_COLLECTION_NAME, settingName));
        if(setting.get_parent() == ""){
            // if this is root item
            setting.set_data(settings);
        } else{
            // getting parent settings and substract from settings to set
            String parentSettings = GetSetting(setting.get_parent());
            settings = SettingsJSONHelper.Subtract(settings, parentSettings);
        }
        _dbConnector.UpdateCollection(DB_COLLECTION_NAME, settingName,
                SettingsTreeItemFactory.convertToDocument(setting));
    }

    public void RemoveSetting(String settingName) throws Exception {
        _dbConnector.RemoveFromCollection(DB_COLLECTION_NAME, settingName);
    }

    public String GetSetting(String settingName) throws Exception {
        Stack<String> settings = new Stack<String>();
        do{
            SettingsTreeItem curSetting = SettingsTreeItemFactory.convertToSetting(
                    _dbConnector.GetFromCollection(DB_COLLECTION_NAME, settingName));
            settings.push(curSetting.get_data());
            settingName = curSetting.get_parent();
        } while (settingName != "");
        String result = settings.pop();
        while (!settings.empty()) {
            result = SettingsJSONHelper.Apply(result, settings.pop());
        }
        return result;
    }

    public List<String> GetResources(String userName) throws Exception {
        SettingsTreeItem setting =
                SettingsTreeItemFactory.convertToSetting(_dbConnector.GetFromCollection(DB_COLLECTION_NAME, userName));
        String resourceParent;
        switch (setting.getItemType()){
            case UserSettings:
                resourceParent = setting.get_parent();
                break;
            case GroupSettings:
                resourceParent = userName;
                break;
            default:
                throw new IllegalArgumentException("Wrong settings type");
        }

        return _dbConnector.FilterCollection(DB_COLLECTION_NAME,
                Filters.eq(SettingsTreeItemFactory.SETTING_PARENT, resourceParent))
                .stream().map(a-> SettingsTreeItemFactory.convertToSetting(a))
                .filter (b->b.getItemType() == SettingsTreeItemType.Resource)
                .map(c->c.get_data())
                .collect(Collectors.toList());
    }

    //public SettingTree GetSettingsTree(){}
}
