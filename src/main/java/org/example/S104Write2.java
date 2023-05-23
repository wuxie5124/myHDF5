package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;
import hdf.hdf5lib.exceptions.HDF5LibraryException;

import java.io.UnsupportedEncodingException;

import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class S104Write2 {
    public S104Write2() {
        String filePath = "C:\\Users\\zjm\\Desktop\\chart\\222224.h5";
        long fileID = H5.H5Fcreate(filePath, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        long rootID = H5.H5Gopen(fileID, "/", H5P_DEFAULT);
        String enum_type = "Enum_type";




        //字符串型属性
//        String value = "abcdefg";
//        String[] attribute = {value};
//        long sid = H5.H5Screate_simple(1, new long[]{1}, null);
//        long attrTypeID = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
//        H5.H5Tset_size(attrTypeID, value.length());
//        long eastBoundLongitudeA = H5.H5Acreate(rootID, "eastBoundLongitude", attrTypeID, sid, H5P_DEFAULT, H5P_DEFAULT);
//        H5.H5Awrite(eastBoundLongitudeA, attrTypeID, value.getBytes());
//
//        H5.H5Sclose(sid);
//        H5.H5Aclose(eastBoundLongitudeA);
//        H5.H5Gclose(rootID);

        //数值型属性
//        short value = 1;
//        short[] attribute = {value};
//        long sid = H5.H5Screate(HDF5Constants.H5S_SCALAR);
//        long eastBoundLongitudeA = H5.H5Acreate(rootID, "eastBoundLongitude", HDF5Constants.H5T_NATIVE_SHORT, sid, H5P_DEFAULT, H5P_DEFAULT);
//        H5.H5Awrite(eastBoundLongitudeA, HDF5Constants.H5T_NATIVE_SHORT, attribute);
//
//        H5.H5Sclose(sid);
//        H5.H5Aclose(eastBoundLongitudeA);
//        H5.H5Gclose(rootID);

        //枚举型属性
        long filetype_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, 1);
        byte[] enum_val = new byte[1];
        enum_val[0] = 1;
        H5.H5Tenum_insert(filetype_id, "seaSurface", enum_val);
        enum_val[0] = 2;
        H5.H5Tenum_insert(filetype_id, "verticalDatum", enum_val);
        enum_val[0] = 3;
        H5.H5Tenum_insert(filetype_id, "seaBottom", enum_val);

//        H5.H5Tcommit(fileID, enum_type, filetype_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        filetype_id = H5.H5Topen(fileID, enum_type, HDF5Constants.H5P_DEFAULT);
//        System.out.println(H5.H5Tget_member_index(filetype_id, "seaBottom") == 2);
//        Byte value = 1;
//        Byte[] attribute = {value};
//        long sid = H5.H5Screate(HDF5Constants.H5S_SCALAR);
//
//        long eastBoundLongitudeA = H5.H5Acreate(rootID, "eastBoundLongitude", filetype_id, sid, H5P_DEFAULT, H5P_DEFAULT);
//        H5.H5Awrite(eastBoundLongitudeA, filetype_id, attribute);
//        H5.H5Tclose(filetype_id);
//        H5.H5Sclose(sid);
//        H5.H5Aclose(eastBoundLongitudeA);
//        H5.H5Gclose(rootID);


        long groupF = H5.H5Gcreate(fileID, "Group_F", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel = H5.H5Gcreate(fileID, "WaterLevel", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel_01 = H5.H5Gcreate(waterLevel, "WaterLevel.01", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long group001 = H5.H5Gcreate(waterLevel_01, "Group_001", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long group002 = H5.H5Gcreate(waterLevel_01, "Group_002", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long group003 = H5.H5Gcreate(waterLevel_01, "Group_003", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long positioning = H5.H5Gcreate(waterLevel_01, "Positioning", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5.H5Gclose(group001);
        H5.H5Gclose(group002);
        H5.H5Gclose(group003);
        H5.H5Gclose(positioning);

        long waterLevel_02 = H5.H5Gcreate(waterLevel, "WaterLevel.02", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group001 = H5.H5Gcreate(waterLevel_02, "Group_001", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group002 = H5.H5Gcreate(waterLevel_02, "Group_002", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group003 = H5.H5Gcreate(waterLevel_02, "Group_003", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        positioning = H5.H5Gcreate(waterLevel_02, "Positioning", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5.H5Gclose(group001);
        H5.H5Gclose(group002);
        H5.H5Gclose(group003);
        H5.H5Gclose(positioning);

        long waterLevel_03 = H5.H5Gcreate(waterLevel, "WaterLevel.03", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group001 = H5.H5Gcreate(waterLevel_03, "Group_001", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group002 = H5.H5Gcreate(waterLevel_03, "Group_002", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        group003 = H5.H5Gcreate(waterLevel_03, "Group_003", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        positioning = H5.H5Gcreate(waterLevel_03, "Positioning", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5.H5Gclose(group001);
        H5.H5Gclose(group002);
        H5.H5Gclose(group003);
        H5.H5Gclose(positioning);

        H5.H5Gclose(waterLevel_01);
        H5.H5Gclose(waterLevel_02);
        H5.H5Gclose(waterLevel_03);
        H5.H5Gclose(waterLevel);
        H5.H5Gclose(groupF);
        H5.H5Fclose(fileID);


//        String fValue3[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//        String filePath = "C:\\Users\\zjm\\Desktop\\chart\\22222.h5";
//        long fileID = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, H5P_DEFAULT);
//        long datasetID = H5.H5Dopen(fileID, "value", H5P_DEFAULT);
//        long tid = H5.H5Dget_type(datasetID);
//        String member_name = H5.H5Tget_member_name(tid, 2);
//        long member_type = H5Tget_member_type(tid, 2);
//        int member_class_t = H5.H5Tget_class(member_type);
//        long member_class_s = H5.H5Tget_size(member_type);
//        long read_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_class_s);
//        H5.H5Tinsert(read_tid, member_name, 0, H5Tget_native_type(member_type));
//        H5.H5Dwrite_VLStrings(datasetID, read_tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue3);
//        H5.H5Tclose(member_type);
//        H5.H5Dclose(datasetID);
//        H5.H5Tclose(tid);
//        H5.H5Fclose(fileID);


//        String filePath = "C:\\Users\\zjm\\Desktop\\chart\\22222.h5";
//        long fileID = H5.H5Fcreate(filePath, HDF5Constants.H5F_ACC_TRUNC,
//                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        float fValue[] = new float[]{1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F};
//        int fValue2[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        String fValue3[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
//        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
//        H5.H5Tset_size(l, 4);
//        long tidCompound = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 4);
//        H5.H5Tinsert(tidCompound, "timePoint", 0, l);
//        long[] a = {10};
//        long sid = H5.H5Screate_simple(1, a, null);
//        long did = H5.H5Dcreate(fileID, "value", tidCompound, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
////        long member_class_s = H5.H5Tget_size(member_type);
//
//        long tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 4);
//        long l2 = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
////        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
//        H5.H5Tset_size(l2, 4);
//        H5.H5Tinsert(tidCompoundTmp, "timePoint", 0, l2);
//        H5.H5Dwrite(did, l2, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue3);

//        H5.H5Tclose(tidCompoundTmp);
//        H5.H5Tclose(l);
//        H5.H5Tclose(l2);
//        H5.H5Tclose(tidCompound);
//        H5.H5Dclose(did);
//        H5.H5Sclose(sid);
//        H5.H5Fclose(fileID);
    }
}
