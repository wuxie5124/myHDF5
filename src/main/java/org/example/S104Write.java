package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

import static hdf.hdf5lib.H5.H5Tget_member_type;
import static hdf.hdf5lib.H5.H5Tget_native_type;

public class S104Write {
    public S104Write() {
//        String filePath = "C:\\Users\\zjm\\Desktop\\chart\\22222.h5";
        String filePath = "C:\\Users\\zjm\\Desktop\\chart\\222224.h5";
        long fileID = H5.H5Fcreate(filePath, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        float fValue[] = new float[]{1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F};
        int fValue2[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String fValue3[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        byte[][] bytes = new byte[][]{"1".getBytes(), "1".getBytes(), "1".getBytes(), "1".getBytes(), "1".getBytes(), "1".getBytes(), "1".getBytes(), "2".getBytes(), "3".getBytes(), "4".getBytes()};
        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
        H5.H5Tset_size(l, 2);
        long tidCompound = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 10);
        H5.H5Tinsert(tidCompound, "height", 0, HDF5Constants.H5T_NATIVE_FLOAT);
        H5.H5Tinsert(tidCompound, "trend", 4, HDF5Constants.H5T_NATIVE_INT);
        H5.H5Tinsert(tidCompound, "timePoint", 8, l);
        long[] a = {10};
        long sid = H5.H5Screate_simple(1, a, null);
        long did = H5.H5Dcreate(fileID, "value", tidCompound, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);


        long tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 4);
        H5.H5Tinsert(tidCompoundTmp, "height", 0, HDF5Constants.H5T_NATIVE_FLOAT);
        H5.H5Dwrite(did, tidCompoundTmp,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue);
        H5.H5Tclose(tidCompoundTmp);

        tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 4);
        H5.H5Tinsert(tidCompoundTmp, "trend", 0, HDF5Constants.H5T_NATIVE_INT);
        H5.H5Dwrite(did, tidCompoundTmp,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue2);
        H5.H5Tclose(tidCompoundTmp);

        tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 2);
        H5.H5Tinsert(tidCompoundTmp, "timePoint", 0, l);
        H5.H5Dwrite_string(did, tidCompoundTmp,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue3);
        H5.H5Tclose(tidCompoundTmp);
        //        H5.H5Dwrite(did, tidCompoundTmp,
//                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, write_data);

//        long typeID = H5.H5Dget_type(did);
//        long member_type = H5Tget_member_type(typeID, 2);
////        long member_class_s = H5.H5Tget_size(member_type);
//        long read_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, -1);
//        H5.H5Tinsert(read_tid, "timePoint", 0, H5Tget_native_type(member_type));
//        H5.H5Dwrite_VLStrings(did,read_tid,
//                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue3);


//        tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, -1);
//        H5.H5Tinsert(tidCompoundTmp, "timePoint", 0, l);
//
//        byte[][] write_data =  new byte[10][4];
//        for (int indx = 0; indx <10 ; indx++) {
//            for (int jndx = 0; jndx < 4; jndx++) {
//                if (jndx < fValue3[indx].length())
//                    write_data[indx][jndx] = (byte) fValue3[indx].charAt(jndx);
//                else
//                    write_data[indx][jndx] = 0;
//            }
//        }
//        H5.H5Dwrite(did, tidCompoundTmp,
//                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, write_data);
//        H5.H5Dwrite_string(did, tidCompoundTmp, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue3);
        ;


        H5.H5Tclose(tidCompound);
        H5.H5Tclose(l);
        H5.H5Dclose(did);
        H5.H5Sclose(sid);
        H5.H5Fclose(fileID);
    }
}
