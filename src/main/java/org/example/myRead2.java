package org.example;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

public class myRead2 {
    // 读取数据结构
    // 读取数据集内容
    // 读取数据描述信息

    public static void main(String[] args) {
        String filePath = "C:\\Users\\zjm\\Desktop\\104US00_ches_dcf1_20190703T00Z.h5";
        String OriginPaths = "/";
        int file_id = -1;

        try {
            file_id = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
            if (file_id > 0) {
                getStructure(file_id,OriginPaths);
            }
        } catch (HDF5LibraryException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getStructure(int fileId, String originPaths) {
        int dataset_id = -1;
        int count = (int) H5.H5Gn_members(fileId, originPaths);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        dataset_id = H5.H5Gget_obj_info_all(fileId, originPaths, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        for (int i = 0; i < oname.length; i++) {
            String childName = oname[i];
            System.out.println();
//         (-1), // Unknown object type
//         (0), // Object is a group
//         (1), // Object is a dataset
//         (2) // Object is a named data type
            if (otype[i] == 0) {
                getStructure(fileId,originPaths + childName +"/");
            }
        }
    }
}
