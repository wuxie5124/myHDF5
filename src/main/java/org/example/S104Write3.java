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
        createGroup();
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
