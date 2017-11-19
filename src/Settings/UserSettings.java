package Settings;

public final class UserSettings extends SettingsTreeItem{

    public UserSettings(String name, String parent, String data){
        super(name, parent, data);
    }

    public UserSettings() {

    }

    @Override
    public SettingsTreeItemType getItemType() {
        return SettingsTreeItemType.UserSettings;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (UserSettings)obj != null &&
                super.equals(obj);
    }
}
