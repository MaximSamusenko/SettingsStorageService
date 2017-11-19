package Settings;

public abstract class SettingsTreeItem {
    protected String _name;
    protected String _parent;
    protected String _data;

    public SettingsTreeItem(){}

    public SettingsTreeItem(String name, String parent, String data){
        _name = name;
        _parent = parent;
        _data = data;
    }

    @Override
    public boolean equals(Object obj) {
        SettingsTreeItem treeItem = (SettingsTreeItem)obj;
        return treeItem != null &&
                _name == treeItem._name &&
                _parent == treeItem._parent &&
                _data == treeItem._data;
    }

    public abstract SettingsTreeItemType getItemType();

    public String get_data() {
        return _data;
    }

    public String get_name() {
        return _name;
    }

    public String get_parent() {
        return _parent;
    }

    public void set_parent(String parent) {
        this._parent = parent;
    }

    public void set_data(String data) {
        this._data = data;
    }

    public void set_name(String name) {
        this._name = name;
    }
}
