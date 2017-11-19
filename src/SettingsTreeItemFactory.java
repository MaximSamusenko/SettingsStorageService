import Settings.*;
import org.bson.Document;
import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

public final class SettingsTreeItemFactory {
    public static final String SETTING_NAME = "name";
    public static final String SETTING_PARENT = "parent";
    public static final String SETTING_TYPE = "type";
    public static final String SETTING_DATA = "data";
    public static final String SETTING_OWNER = "owner";

    @Contract(value = "null -> fail", pure = true)
    public static Document convertToDocument(SettingsTreeItem setting) throws IllegalArgumentException {
        Document result;

        switch(setting.getItemType()){

            case UserSettings:
                result = convertToDocument((UserSettings)setting);
                break;
            case GroupSettings:
                result = convertToDocument((GroupSettings)setting);
                break;
            case Resource:
                result = convertToDocument((Resource)setting);
                break;
                default:
                    throw new IllegalArgumentException(String.format("convertToDocument: invalid setting type {0}",
                            setting.getItemType()));
        }

        return  result;
    }

    private static Document convertToDocumentBase(SettingsTreeItem setting){
        Document result = new Document("Id", setting.get_name());

        result.append(SETTING_NAME, setting.get_name());
        result.append(SETTING_PARENT, setting.get_parent());
        result.append(SETTING_TYPE, setting.getItemType().name());
        result.append(SETTING_DATA, setting.get_data());

        return result;
    }

    private static Document convertToDocument(UserSettings setting){
        return convertToDocumentBase(setting);
    }

    private static Document convertToDocument(GroupSettings setting){
        return convertToDocumentBase(setting);
    }

    private static Document convertToDocument(Resource setting){
        Document result = convertToDocumentBase(setting);
        result.append(SETTING_OWNER, setting.get_owner());
        return result;
    }

    @Contract(value = "null -> fail", pure = true)
    public static SettingsTreeItem convertToSetting(Document doc) throws IllegalArgumentException {
        String settingTypeStr = doc.getString(SETTING_TYPE);
        if(settingTypeStr != null &&
                Stream.of(SettingsTreeItemType.values()).anyMatch(item->settingTypeStr.equals(item.name()))) {
            SettingsTreeItem result;
            SettingsTreeItemType type = SettingsTreeItemType.valueOf(settingTypeStr);
            switch(type){
                case UserSettings:
                    result = convertToUserSetting(doc);
                    break;
                case GroupSettings:
                    result = convertToGroupSetting(doc);
                    break;
                case Resource:
                    result = convertToResourceSetting(doc);
                    break;
                default:
                    throw new IllegalArgumentException("Empty or unknown setting type");
            }

            return result;
        }else{
            throw new IllegalArgumentException("Empty or unknown setting type");
        }
    }

    private static SettingsTreeItem convertToUserSettingBase(Document doc, SettingsTreeItem setting)
    throws IllegalArgumentException {
        String[] settingNames = new String[] {SETTING_NAME, SETTING_DATA, SETTING_PARENT};
        SettingsTreeItem result = setting;
        for (String settingName: settingNames) {
            if(!doc.containsKey(settingName))
            {
                throw new IllegalArgumentException(String.format("Document douesn't contains key {0}", settingName));
            }
        }

        result.set_name(doc.getString(SETTING_NAME));
        result.set_parent(doc.getString(SETTING_PARENT));
        result.set_data(doc.getString(SETTING_DATA));

        return result;
    }

    private static SettingsTreeItem convertToUserSetting(Document doc) {
        return convertToUserSettingBase(doc, new UserSettings());
    }

    private static SettingsTreeItem convertToGroupSetting(Document doc) {
        return convertToUserSettingBase(doc, new GroupSettings());
    }

    private static SettingsTreeItem convertToResourceSetting(Document doc) {
        Resource result = (Resource)convertToUserSettingBase(doc, new Resource());
        if(!doc.containsKey(SETTING_OWNER))
        {
            throw new IllegalArgumentException(String.format("Document douesn't contains key {0}", SETTING_OWNER));
        }
        result.set_owner(doc.getString(SETTING_OWNER));
        return result;
    }

}
