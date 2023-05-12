package org.example.hdf;

import java.io.File;
import java.util.Map;

public class Dataset implements IDataset {
    Map<String, IAttribute> attributeMap;
    Object[][] data;
    String name;
    IGroup parentGroup;
    Hdf5File hdf5File;
    int Address;

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public int[] getShape() {
        if (data != null) {
            return new int[]{data.length, data[0].length};
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IGroup getParent() {
        return parentGroup;
    }

    @Override
    public Map<String, IAttribute> getAttributes() {
        return attributeMap;
    }

    @Override
    public IAttribute getAtrribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public NodeType getType() {
        return NodeType.DATASET;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public Hdf5File getH5File() {
        return hdf5File;
    }

    @Override
    public int getAddress() {
        return Address;
    }
}
