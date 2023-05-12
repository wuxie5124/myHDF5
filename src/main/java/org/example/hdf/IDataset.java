package org.example.hdf;

import java.util.ArrayList;

public interface IDataset extends INode {
     Object getData();

     int[] getShape();
}
