package org.example.hdf;

public class Attribute implements IAttribute{
    String name;
    INode node;
    Object data;
    public Attribute(String name, INode node, Object data) {
        this.name = name;
        this.node = node;
        this.data = data;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public INode getNode() {
        return node;
    }
    @Override
    public Object getData() {
        return data;
    }
}
