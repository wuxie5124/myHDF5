package org.example.hdf;


import java.util.Map;

public interface INode {
    String getName();
    IGroup getParent();
    Map<String, IAttribute> getAttributes();

    IAttribute getAtrribute(String name);
    NodeType getType();
    boolean isGroup();
    Hdf5File getH5File();
    int getAddress();
}
