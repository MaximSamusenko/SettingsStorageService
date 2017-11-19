package Settings;

public final class Resource extends SettingsTreeItem{
    private String _owner;

    public Resource(String name, String parent, String data, String owner){
        super(name, parent, data);
        _owner = owner;
    }

    public Resource() {

    }

    @Override
    public SettingsTreeItemType getItemType() {
        return SettingsTreeItemType.Resource;
    }

    @Override
    public boolean equals(Object obj) {
        Resource resource = (Resource)obj;
        return resource != null &&
                super.equals(obj) &&
                resource._owner == _owner;
    }

    public String get_owner() {
        return _owner;
    }

    public void set_owner(String owner) {
        this._owner = owner;
    }
}
