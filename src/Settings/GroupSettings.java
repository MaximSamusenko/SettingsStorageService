package Settings;

public final class GroupSettings extends SettingsTreeItem {

    public GroupSettings(String name, String parent, String data){
        super(name, parent, data);
    }

    public GroupSettings() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (GroupSettings)obj != null &&
                super.equals(obj);
    }

    @Override
    public SettingsTreeItemType getItemType() {
        return SettingsTreeItemType.GroupSettings;
    }
}
