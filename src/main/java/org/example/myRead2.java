package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;
import hdf.hdf5lib.exceptions.HDF5LibraryException;
import hdf.hdf5lib.structs.H5O_info_t;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import static hdf.hdf5lib.H5.*;
import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class myRead2 {
    // 读取数据结构
    // 读取数据集内容
    // 读取数据描述信息

    public static void main(String[] args) {
        String filePath = "C:\\Users\\zjm\\Desktop\\104US00_ches_dcf2_20190606T12Z.h5";
        String OriginPaths = "/";
        long file_id = -1;
        HashMap<String,Object> attributes = new HashMap<>();
        try {
            file_id = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, H5P_DEFAULT);
            if (file_id > 0) {
//                getStructure(file_id,OriginPaths);
//                long dataset_id = H5.H5Dopen(file_id, "/", H5P_DEFAULT);
                long dataset_id = H5Gopen(file_id, "/", H5P_DEFAULT);
                //WaterLevel/WaterLevel.01/Group_001/values WaterLevel/axisNames Group_F/WaterLevel
                long[] dims = {0, 0};
                long[] dims2 = {0, 0};
                H5O_info_t info = H5Oget_info(file_id);
                for (int i = 0; i < info.num_attrs; i++) {
                    //WaterLevel/WaterLevel.01/Group_001/values WaterLevel/axisNames Group_F/WaterLevel
                    long attributeId = H5.H5Aopen_by_idx(dataset_id, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
                    String name = H5Aget_name(attributeId);
                    int size = (int) H5.H5Aget_storage_size(attributeId);
                    byte[] data = new byte[size];
                    long attributeType = H5Aget_type(attributeId);
                    boolean b = H5Tget_class(attributeType) == HDF5Constants.H5T_STRING;
                    String s = H5Tget_member_name(attributeType, 0);
                    H5.H5Aread(attributeId,attributeType,data);
                    attributes.put(name,data);
                    H5.H5Aclose(attributeId);
                }
                ByteBuffer byteBuffer = ByteBuffer.wrap((byte[]) attributes.get("eastBoundLongitude"));
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                double anInt = byteBuffer.getDouble();
                long space_id = H5.H5Dget_space(dataset_id);
                H5.H5Sget_simple_extent_dims(space_id, dims, dims2);
                int dim = H5.H5Sget_simple_extent_ndims(space_id);
                String[] strings = new String[(int) dims[0]];
                long tid = H5.H5Dget_type(dataset_id);
                long ntid = H5.H5Tget_native_type(tid);
                boolean b = H5.H5Tis_variable_str(ntid);
                long i = H5.H5Tget_size(ntid);
                int i2 = H5.H5Tget_nmembers(tid);//返回复杂属性的成员数目
                String s = H5Tget_member_name(tid, 0);//返回名称
                int codeindex = H5Tget_member_index(tid, "code");
                boolean b1 = H5Tget_class(tid) == HDF5Constants.H5T_COMPOUND;

                String[] ss = new String[3];
                H5.H5Dread_VLStrings(dataset_id, tid,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, ss);
//                long size = 0;
//                H5.H5Dvlen_get_buf_size(dataset_id, tid , size);
                String[] stringsA = new String[30];
                H5.H5DreadVL(dataset_id, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, stringsA);
                int nums = 0;
                int i1 = H5.H5Tget_member_class(tid, 6);
                int length = strings.length;


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
            System.out.println(childName);
//         (-1), // Unknown object type
//         (0), // Object is a group
//         (1), // Object is a dataset
//         (2) // Object is a named data type
            if (otype[i] == 0) {
                getStructure(fileId, originPaths + childName + "/");
            }
        }
    }
}
