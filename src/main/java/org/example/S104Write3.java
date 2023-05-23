package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

import java.util.ArrayList;
import java.util.HashSet;
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
        createGroupTest();
        createDataset();
        H5.H5Fclose(this.fileID);
    }

    private void createGroupTest() {
        long groupF = H5.H5Gcreate(fileID, "Group_F", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        long waterLevel = H5.H5Gcreate(fileID, "WaterLevel", H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5.H5Gclose(groupF);
        H5.H5Gclose(waterLevel);
    }

    private void createDataset() {
        commonDataset();
    }

    private void commonDataset() {
        long group_f = H5.H5Gopen(fileID, "Group_F", H5P_DEFAULT);
        long tidCompound = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 18);
        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
        H5.H5Tset_size(l, 2);
        H5.H5Tinsert(tidCompound, "code", 0, l);
        H5.H5Tinsert(tidCompound, "name", 2, l);
        H5.H5Tinsert(tidCompound, "uom.name", 4, l);
        H5.H5Tinsert(tidCompound, "fillValue", 8, l);
        H5.H5Tinsert(tidCompound, "dataType", 10, l);
        H5.H5Tinsert(tidCompound, "lower", 12, l);
        H5.H5Tinsert(tidCompound, "upper", 14, l);
        H5.H5Tinsert(tidCompound, "closure", 16, l);
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

        createBuff(did, "code", code);
        createBuff(did, "name", name);
        createBuff(did, "uom.name", uom_name);
        createBuff(did, "fillValue", fillValue);
        createBuff(did, "dataType", dataType);
        createBuff(did, "lower", lower);
        createBuff(did, "upper", upper);
        createBuff(did, "closure", closure);
        H5.H5Tclose(l);
        H5.H5Sclose(sid);
        H5.H5Gclose(group_f);
        H5.H5Tclose(tidCompound);
        H5.H5Dclose(did);
    }

    private void createBuff(long did, String name, String[] strings) {
        long l = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_cset(l, HDF5Constants.H5T_CSET_UTF8);
        H5.H5Tset_size(l, 2);

        long tidCompoundTmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 2);
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
