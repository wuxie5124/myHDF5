package org.example.hdf;

import java.util.Iterator;
import java.util.Map;

public class Group implements IGroup {

    Map<String, IAttribute> attributeMap;
    String name;
    IGroup parentGroup;
    Hdf5File hdf5File;
    int Address;

    public Group(Map<String, IAttribute> attributeMap, String name, int address, Map<String, INode> childrean) {
        this.attributeMap = attributeMap;
        this.name = name;
        Address = address;
        this.childrean = childrean;
    }

    public void setParentGroup(IGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public void setHdf5File(Hdf5File hdf5File) {
        this.hdf5File = hdf5File;
    }

    Map<String, INode> childrean;

    @Override
    public Map<String, INode> getChildrean() {
        return childrean;
    }

    @Override
    public INode getChild(String childName) {
        return childrean.get(childName);
    }

    @Override
    public INode getChildByPath(String path) {
        String[] subPaths = path.split("/");
        Group group = this;
        for (int i = 0; i < subPaths.length - 1; i++) {
            INode child = this.getChild(subPaths[i]);
            if (child instanceof Group) {
                group = (Group) this.getChild(subPaths[i]);
            } else {
                return null;
            }
        }
        return group.getChild(subPaths[subPaths.length - 1]);
    }

    @Override
    public IDataset getDatasetByPath(String path) {
        return null;
    }

    @Override
    public Iterator<INode> iterator() {
        return this.getChildrean().values().iterator();
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public IGroup getParent() {
        return null;
    }

    @Override
    public Map<String, IAttribute> getAttributes() {
        return null;
    }

    @Override
    public IAttribute getAtrribute(String name) {
        return null;
    }

    @Override
    public NodeType getType() {
        return null;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public Hdf5File getH5File() {
        return null;
    }

    @Override
    public int getAddress() {
        return 0;
    }
}
