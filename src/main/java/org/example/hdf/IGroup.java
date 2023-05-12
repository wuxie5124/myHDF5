package org.example.hdf;

import java.util.Map;

public interface IGroup extends INode, Iterable<INode> {
    Map<String, INode> getChildrean();
    INode getChild(String childName);
    INode getChildByPath(String path);
    IDataset getDatasetByPath(String path);
}
