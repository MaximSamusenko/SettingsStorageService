import Settings.GroupSettings;
import Settings.Resource;
import Settings.UserSettings;
import org.bson.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTreeItemFactoryTest {
    @Test
    void convertToDocument() {
        UserSettings uSetting = new UserSettings("user", "group", "someData");
        Resource rSetting = new Resource("resource", "group", "resource data", "user");
        GroupSettings gSetting = new GroupSettings("group", "self", "group data");

        Document userDoc = SettingsTreeItemFactory.convertToDocument(uSetting);
        Document groupDoc = SettingsTreeItemFactory.convertToDocument(gSetting);
        Document resourceDoc = SettingsTreeItemFactory.convertToDocument(rSetting);

        Assert.assertEquals(uSetting, SettingsTreeItemFactory.convertToSetting(userDoc));
        Assert.assertEquals(gSetting, SettingsTreeItemFactory.convertToSetting(groupDoc));
        Assert.assertEquals(rSetting, SettingsTreeItemFactory.convertToSetting(resourceDoc));
    }

}