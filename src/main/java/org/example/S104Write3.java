package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

import java.util.ArrayList;
import java.util.List;

import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class S104Write3 {
    private long fileID;
    private Dcf dcf;
    private ArrayList<S104Read.TabularAttr> attrs;
    private String identifier;
    ArrayList<S104Read.ChartDataset> chartDatasets;

    public S104Write3(String filePath) {
        this.fileID = H5.H5Fcreate(filePath, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        createGroup();
        dcf = Dcf.DCF1;
        createGroupTest();
        createDataset();
        H5.H5Fclose(this.fileID);
    }

    private void createGroupTest() {
        long groupF = H5.H5Gcreate(fileID, "Group_F", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel = H5.H5Gcreate(fileID, "WaterLevel", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel01 = H5.H5Gcreate(waterLevel, "WaterLevel.01", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long group001 = H5.H5Gcreate(waterLevel01, "Group_001", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5.H5Gclose(group001);
        H5.H5Gclose(waterLevel01);
        H5.H5Gclose(groupF);
        H5.H5Gclose(waterLevel);
    }

    private void createDataset() {
        commonDataset();
        switch (dcf) {
            case DCF1:
                datasetdcf1();
            case DCF2:
                datasetdcf2();
            case DCF3:
                datasetdcf3();
            case DCF7:
                datasetdcf7();
            case DCF8:
                datasetdcf8();
        }
    }

    private void datasetdcf8() {
    }

    private void datasetdcf7() {
    }

    private void datasetdcf3() {
    }

    private void datasetdcf2() {
    }

    private void datasetdcf1() {
        long group_f = H5.H5Gopen(fileID, "WaterLevel/WaterLevel.01/Group_001", H5P_DEFAULT);
        String[] names = {"waterLevelHeight", "waterLevelTrend"};
        long[] native_Type = {HDF5Constants.H5T_NATIVE_FLOAT, HDF5Constants.H5T_NATIVE_INT};
        Object[] value = new Object[2];
        int[] a = {1, 2, 3, 4,1, 2, 3, 4,};
        float[] b = {1.1f, 2.1f, 3.1f, 4.1f,1.1f, 2.1f, 3.1f, 5.1f};
        value[0] = b;
        value[1] = a;

        createHeightAndTrend(group_f, names, native_Type, new long[]{4,2}, value);
        H5.H5Gclose(group_f);
    }

    private void createHeightAndTrend(long groupID, String[] names, long[] nativeType, long[] shape, Object[] values) {
        long totalSize = 0;
        for (long l : nativeType) {
            totalSize += H5.H5Tget_size(l);
        }
        long tidCompound = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, totalSize);
        long offset = 0;
        for (int i = 0; i < names.length; i++) {
            H5.H5Tinsert(tidCompound, names[i], offset, nativeType[i]);
            offset += H5.H5Tget_size(nativeType[i]);
        }
        long sid = H5.H5Screate_simple(shape.length, shape, null);
        long did = H5.H5Dcreate(groupID, "values", tidCompound, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        for (int i = 0; i < names.length; i++) {
            long tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, H5.H5Tget_size(nativeType[i]));
            H5.H5Tinsert(tidCompoundTmp, names[i], 0, nativeType[i]);
            H5.H5Dwrite(did, tidCompoundTmp,
                    HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, values[i]);
            H5.H5Tclose(tidCompoundTmp);
        }
        H5.H5Sclose(sid);
        H5.H5Tclose(tidCompound);
        H5.H5Dclose(did);
    }

    private void commonDataset() {
        long group_f = H5.H5Gopen(fileID, "Group_F", H5P_DEFAULT);
        long size = 256;
        long tidCompound = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, size * 8);
        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
        H5.H5Tset_size(l, size);
        H5.H5Tinsert(tidCompound, "code", size * 0, l);
        H5.H5Tinsert(tidCompound, "name", size * 1, l);
        H5.H5Tinsert(tidCompound, "uom.name", size * 2, l);
        H5.H5Tinsert(tidCompound, "fillValue", size * 3, l);
        H5.H5Tinsert(tidCompound, "dataType", size * 4, l);
        H5.H5Tinsert(tidCompound, "lower", size * 5, l);
        H5.H5Tinsert(tidCompound, "upper", size * 6, l);
        H5.H5Tinsert(tidCompound, "closure", size * 7, l);
        long[] a = {3};
        long sid = H5.H5Screate_simple(1, a, null);
        long did = H5.H5Dcreate(group_f, "WaterLevel", tidCompound, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

        String[] code = {"waterLevelHeight", "waterLevelTrend", "waterLevelTime"};
        String[] name = {"Water level height", "Water level trend", "Water level time"};
        String[] uom_name = {"metres", "", "DateTime"};
        String[] fillValue = {"-9999.0", "0", ""};
        String[] dataType = {"H5T_FLOAT", "H5T_ENUM", "H5T_STRING"};
        String[] lower = {"-99.99", "", "19000101T000000Z"};
        String[] upper = {"99.99", "", "21500101T000000Z"};
        String[] closure = {"closedInterval", "", "closedInterval"};

        createBuff(did, "code", code, size);
        createBuff(did, "name", name, size);
        createBuff(did, "uom.name", uom_name, size);
        createBuff(did, "fillValue", fillValue, size);
        createBuff(did, "dataType", dataType, size);
        createBuff(did, "lower", lower, size);
        createBuff(did, "upper", upper, size);
        createBuff(did, "closure", closure, size);
        H5.H5Tclose(l);
        H5.H5Sclose(sid);
        H5.H5Tclose(tidCompound);
        H5.H5Dclose(did);

        String[] feature = {"WaterLevel"};
        sid = H5.H5Screate_simple(1, new long[]{1}, null);
        long l1 = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(l1, feature[0].length());
        did = H5.H5Dcreate(group_f, "featureCode", l1, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Dwrite_string(did, l1, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, feature);
        H5.H5Tclose(l1);
        H5.H5Sclose(sid);
        H5.H5Dclose(did);

        H5.H5Gclose(group_f);
    }

    private void createBuff(long did, String name, String[] strings, long size) {
        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
        H5.H5Tset_size(l, size);

        long tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, size);
        H5.H5Tinsert(tidCompoundTmp, name, 0, l);
        H5.H5Dwrite(did, tidCompoundTmp,
                HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, strings);
        H5.H5Tclose(l);
        H5.H5Tclose(tidCompoundTmp);
    }

    public byte[][] stingsToByte(String[] strings) {
        int length = strings.length;
        byte[][] a = new byte[length][];
        for (int i = 0; i < length; i++) {
            a[i] = strings[i].getBytes();
        }
        return a;
    }

    private static void duplicate(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!newList.contains(list.get(i))) {
                newList.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(newList);
    }

    private void createGroup() {
        long groupF = H5.H5Gcreate(fileID, "Group_F", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel = H5.H5Gcreate(fileID, "WaterLevel", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        if (dcf != Dcf.DCF8) {


        } else {
            for (int i = 1; i <= this.chartDatasets.size(); i++) {
                long waterLevel_0x = H5.H5Gcreate(waterLevel, "WaterLevel." + String.format("%02d", i), H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
                S104Read.ChartDataset chartDataset = chartDatasets.get(i);
                ArrayList<S104Read.Record> records = chartDataset.getRecords();
                List<String> times = new ArrayList<>();
                for (S104Read.Record record : records) {
                    times.add(record.getTimePoint());
                }
                duplicate(times);
                for (int j = 1; j <= times.size(); j++) {
                    long groupID = H5.H5Gcreate(waterLevel_0x, "Group_" + String.format("%03d", j), H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
                    H5.H5Gclose(groupID);
                }
                if (dcf != Dcf.DCF2) {
                    long positioning = H5.H5Gcreate(waterLevel_0x, "Positioning", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
                    H5.H5Gclose(positioning);
                }
                H5.H5Gclose(waterLevel_0x);
            }
        }
        H5.H5Gclose(groupF);
        H5.H5Fclose(fileID);
    }
}
