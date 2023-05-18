package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hdf.hdf5lib.H5.*;
import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class S104Read {
    private long fileID;
    private Dcf dcf;
    private ArrayList<TabularAttr> attrs;

    private String identifier;

    ArrayList<ChartDataset> chartDatasets;

    public S104Read(String filePath) {
        this.fileID = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, H5P_DEFAULT);
        this.dcf = getDcf();
        this.attrs = new ArrayList<>();
        this.chartDatasets = new ArrayList<>();
        getInfo();
    }

    private void getInfo() {
        getTabular();
        getDataset();
    }

    private void getTabular() {
        getCommonAttr();
        switch (this.dcf) {
            case DCF1:
                DCF1Attr();
                break;
            case DCF2:
                DCF2Attr();
                break;
            case DCF3:
                DCF3Attr();
                break;
            case DCF7:
                DCF7Attr();
                break;
            case DCF8:
                DCF8Attr();
                break;
        }
    }

    private void getDataset() {
        switch (dcf) {
            case DCF1:
                getDdcf1();
            case DCF2:
                getDdcf2();
            case DCF3:
                getDdcf3();
            case DCF7:
                getDdcf7();
            case DCF8:
                getDdcf8();
        }
    }

    private void getDdcf8() {
        String waterLevelPath = "WaterLevel";
        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            ChartDataset cDataset = new ChartDataset();
            String waterL = oname[i];
            cDataset.setName("S104_dcf" + this.dcf.getId() + "_" + waterL.split("\\.")[1] + "_value");
            String waterLPath = waterLevelPath + "/" + waterL;
            int countSub = (int) H5.H5Gn_members(this.fileID, waterLPath);
            String[] onameSub = new String[countSub];
            int[] otypeSub = new int[countSub];
            int[] ltypeSub = new int[countSub];
            long[] orefsSub = new long[countSub];
            H5.H5Gget_obj_info_all(this.fileID, waterLPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
            onameSub = removeElement("Positioning", onameSub);
            onameSub = removeElement("uncertainty", onameSub);
            String PositiningPath = waterLPath + "/Positioning/geometryValues";
            double[][] getcoordinate = getcoordinate(PositiningPath);
            for (int j = 0; j < onameSub.length; j++) {
                String groupName = onameSub[j];
                String groupPath = waterLPath + "/" + groupName;
                long group0ID = H5.H5Gopen(fileID, groupPath, H5P_DEFAULT);
                int timeIntervalIndex = getIntAttr(group0ID, "timeIntervalIndex");
                if (timeIntervalIndex == 1) {
                    String startDateTime = getStringAttr(group0ID, "startDateTime");
                    int numberOfTimes = getShorttAttr(group0ID, "numberOfTimes");
//                    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddTmmssSS");
                    String datasetPath = groupPath + "/" + "values";
//                    readDataset(cDataset, datasetPath, timePoint, getcoordinate);
                }
            }
            this.chartDatasets.add(cDataset);
        }

    }

    private void getDdcf7() {
        getDdcf1();
    }

    private void getDdcf3() {
        getDdcf1();
    }

    private void getDdcf2() {
        String waterLevelPath = "WaterLevel";
        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            ChartDataset cDataset = new ChartDataset();
            String waterL = oname[i];
            cDataset.setName("S104_dcf" + this.dcf.getId() + "_" + waterL.split("\\.")[1] + "_value");
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            double eastBoundLongitude = getDoubleAttr(waterLevel0ID, "eastBoundLongitude");
            double westBoundLongitude = getDoubleAttr(waterLevel0ID, "westBoundLongitude");
            double northBoundLatitude = getDoubleAttr(waterLevel0ID, "northBoundLatitude");
            double southBoundLatitude = getDoubleAttr(waterLevel0ID, "southBoundLatitude");
            double gridSpacingLatitudinal = getDoubleAttr(waterLevel0ID, "gridSpacingLatitudinal");
            double gridSpacingLongitudinal = getDoubleAttr(waterLevel0ID, "gridSpacingLongitudinal");

            int numPointsLatitudinal = getShorttAttr(waterLevel0ID, "numPointsLatitudinal");
            int numPointsLongitudinal = getShorttAttr(waterLevel0ID, "numPointsLongitudinal");

            double[][] getcoordinate = new double[2][];
            double[] latitude = new double[numPointsLatitudinal * numPointsLongitudinal];
            double[] longitude = new double[numPointsLatitudinal * numPointsLongitudinal];
            for (int j = 0; j < numPointsLatitudinal; j++) {
                for (int k = 0; k < numPointsLongitudinal; k++) {
                    latitude[j * numPointsLongitudinal + k] = northBoundLatitude + gridSpacingLatitudinal * j;
                    longitude[j * numPointsLongitudinal + k] = westBoundLongitude + gridSpacingLongitudinal * k;
                }
            }
            getcoordinate[0] = longitude;
            getcoordinate[1] = latitude;
            int countSub = (int) H5.H5Gn_members(this.fileID, waterLPath);
            String[] onameSub = new String[countSub];
            int[] otypeSub = new int[countSub];
            int[] ltypeSub = new int[countSub];
            long[] orefsSub = new long[countSub];
            H5.H5Gget_obj_info_all(this.fileID, waterLPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
            onameSub = removeElement("uncertainty", onameSub);

            for (int j = 0; j < onameSub.length; j++) {
                String groupName = onameSub[j];
                String groupPath = waterLPath + "/" + groupName;
                long group0ID = H5.H5Gopen(fileID, groupPath, H5P_DEFAULT);
                String timePoint = getStringAttr(group0ID, "timePoint");
                H5.H5Gclose(group0ID);
                String datasetPath = groupPath + "/" + "values";
                readDataset(cDataset, datasetPath, timePoint, getcoordinate);
            }
            this.chartDatasets.add(cDataset);
        }
    }

    private void getDdcf1() {
        String waterLevelPath = "WaterLevel";
        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            ChartDataset cDataset = new ChartDataset();
            String waterL = oname[i];
            cDataset.setName("S104_dcf" + this.dcf.getId() + "_" + waterL.split("\\.")[1] + "_value");
            String waterLPath = waterLevelPath + "/" + waterL;
            int countSub = (int) H5.H5Gn_members(this.fileID, waterLPath);
            String[] onameSub = new String[countSub];
            int[] otypeSub = new int[countSub];
            int[] ltypeSub = new int[countSub];
            long[] orefsSub = new long[countSub];
            H5.H5Gget_obj_info_all(this.fileID, waterLPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
            onameSub = removeElement("Positioning", onameSub);
            onameSub = removeElement("uncertainty", onameSub);
            String PositiningPath = waterLPath + "/Positioning/geometryValues";
            double[][] getcoordinate = getcoordinate(PositiningPath);
            for (int j = 0; j < onameSub.length; j++) {
                String groupName = onameSub[j];
                String groupPath = waterLPath + "/" + groupName;
                long group0ID = H5.H5Gopen(fileID, groupPath, H5P_DEFAULT);
                String timePoint = getStringAttr(group0ID, "timePoint");
                H5.H5Gclose(group0ID);
                String datasetPath = groupPath + "/" + "values";
                readDataset(cDataset, datasetPath, timePoint, getcoordinate);
            }
            this.chartDatasets.add(cDataset);
        }
    }

    private double[][] getcoordinate(String datasetPath) {
        double[][] coordinate = new double[2][];
        long datasetID = H5.H5Dopen(fileID, datasetPath, H5P_DEFAULT);
        long space_id = H5.H5Dget_space(datasetID);
        long[] dims1 = {0, 0};
        long[] dims2 = {0, 0};
        long tid = H5.H5Dget_type(datasetID);
        H5.H5Sget_simple_extent_dims(space_id, dims1, dims2);
        int dims = H5Sget_simple_extent_ndims(space_id);
        int size = (int) (dims == 1 ? dims1[0] : dims1[0] * dims1[1]);
        double[] height = (double[]) readCompoundData(datasetID, tid, 0, size);
        double[] trend = (double[]) readCompoundData(datasetID, tid, 1, size);
        coordinate[0] = height;
        coordinate[1] = trend;
        return coordinate;
    }

    private void readDataset(ChartDataset cDataset, String datasetPath, String timePoint, double[][] coordinate) {
        long datasetID = H5.H5Dopen(fileID, datasetPath, H5P_DEFAULT);
        long space_id = H5.H5Dget_space(datasetID);
        long[] dims1 = {0, 0};
        long[] dims2 = {0, 0};
        H5.H5Sget_simple_extent_dims(space_id, dims1, dims2);
        long tid = H5.H5Dget_type(datasetID);
        int dims = H5Sget_simple_extent_ndims(space_id);
        int size = (int) (dims == 1 ? dims1[0] : dims1[0] * dims1[1]);
        float[] height = (float[]) readCompoundData(datasetID, tid, 0, size);
        int[] trend = (int[]) readCompoundData(datasetID, tid, 1, size);
        double[] longitude = coordinate[0];
        double[] latitude = coordinate[1];

        for (int i = 0; i < height.length; i++) {
            cDataset.addRow(new Record(height[i], trend[i], this.identifier, timePoint, longitude[i], latitude[i]));
        }
    }

    private Object readCompoundData(long datasetID, long typeID, int index, int len) {
        String member_name = H5.H5Tget_member_name(typeID, index);
        long member_type = H5Tget_member_type(typeID, index);
        int member_class_t = H5.H5Tget_class(member_type);
        long member_class_s = H5.H5Tget_size(member_type);
        long read_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_class_s);
        Object values;
        if (member_class_t == HDF5Constants.H5T_FLOAT) {
            if (member_class_s == 8) {
                H5.H5Tinsert(read_tid, member_name, 0, HDF5Constants.H5T_NATIVE_DOUBLE);
                values = new double[len];
            } else {
                H5.H5Tinsert(read_tid, member_name, 0, HDF5Constants.H5T_NATIVE_FLOAT);
                values = new float[len];
            }
        } else if (member_class_t == HDF5Constants.H5T_INTEGER) {
            H5.H5Tinsert(read_tid, member_name, 0, HDF5Constants.H5T_NATIVE_INT32);
            values = new int[len];
        } else if (member_class_t == HDF5Constants.H5T_STRING) {
            H5.H5Tinsert(read_tid, member_name, 0, HDF5Constants.H5T_NATIVE_CHAR);
            values = new String[len];
        } else {
            values = null;
            //Error
        }
        H5.H5Dread(datasetID, read_tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, values);
        return values;
    }

    private void DCF8Attr() {
        String waterLevelPath = "WaterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = String.valueOf(getShorttAttr(waterLevelID, "numInstances"));
        String pickPriorityType = getStringAttr(waterLevelID, "pickPriorityType");

        attrs.add(new TabularAttr("commonPointRule", waterLevelPath, commonPointRule));
        attrs.add(new TabularAttr("dataCodingFormat", waterLevelPath, dataCodingFormat));
        attrs.add(new TabularAttr("methodWaterLevelProduct", waterLevelPath, methodWaterLevelProduct));
        attrs.add(new TabularAttr("numInstances", waterLevelPath, numInstances));
        attrs.add(new TabularAttr("pickPriorityType", waterLevelPath, pickPriorityType));
        H5.H5Gclose(waterLevelID);
        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
            int countSub = (int) H5.H5Gn_members(this.fileID, waterLPath);
            String[] onameSub = new String[countSub];
            int[] otypeSub = new int[countSub];
            int[] ltypeSub = new int[countSub];
            long[] orefsSub = new long[countSub];
            H5.H5Gget_obj_info_all(this.fileID, waterLPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
            onameSub = removeElement("Positioning", onameSub);
            for (int j = 0; j < onameSub.length; j++) {
                String groupName = onameSub[j];
                String groupPath = waterLPath + "/" + groupName;
                long group0ID = H5.H5Gopen(fileID, groupPath, H5P_DEFAULT);
                String endDateTime = getStringAttr(group0ID, "endDateTime");
                String numberOfTime = String.valueOf(getShorttAttr(group0ID, "numberOfTime"));
                String startDateTime = String.valueOf(getIntAttr(group0ID, "startDateTime"));
                String stationIdentification = String.valueOf(getIntAttr(group0ID, "stationIdentification"));
                String stationName = getStringAttr(group0ID, "stationName");
                String timeIntervalIndex = String.valueOf(getIntAttr(group0ID, "timeIntervalIndex"));
                attrs.add(new TabularAttr("endDateTime", groupPath, endDateTime));
                attrs.add(new TabularAttr("numberOfTime", groupPath, numberOfTime));
                attrs.add(new TabularAttr("startDateTime", groupPath, startDateTime));
                attrs.add(new TabularAttr("stationIdentification", groupPath, stationIdentification));
                attrs.add(new TabularAttr("stationName", groupPath, stationName));
                attrs.add(new TabularAttr("timeIntervalIndex", groupPath, timeIntervalIndex));
                if (getIntAttr(group0ID, "timeIntervalIndex") == 1) {
                    String timeRecordInterval = String.valueOf(getShorttAttr(group0ID, "timeRecordInterval"));
                    attrs.add(new TabularAttr("timeRecordInterval", groupPath, timeRecordInterval));
                }
            }
        }
    }

    private void DCF7Attr() {
        String waterLevelPath = "WaterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = String.valueOf(getShorttAttr(waterLevelID, "numInstances"));

        attrs.add(new TabularAttr("commonPointRule", waterLevelPath, commonPointRule));
        attrs.add(new TabularAttr("dataCodingFormat", waterLevelPath, dataCodingFormat));
        attrs.add(new TabularAttr("interpolationType", waterLevelPath, interpolationType));
        attrs.add(new TabularAttr("methodWaterLevelProduct", waterLevelPath, methodWaterLevelProduct));
        attrs.add(new TabularAttr("numInstances", waterLevelPath, numInstances));
        H5.H5Gclose(waterLevelID);

        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String numberOfTriangles = String.valueOf(getShorttAttr(waterLevel0ID, "numberOfTriangles"));
            String timeRecordInterval = String.valueOf(getShorttAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("numberOfTriangles", waterLPath, numberOfTriangles));
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }

    private void DCF3Attr() {
        String waterLevelPath = "WaterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = String.valueOf(getShorttAttr(waterLevelID, "numInstances"));

        attrs.add(new TabularAttr("commonPointRule", waterLevelPath, commonPointRule));
        attrs.add(new TabularAttr("dataCodingFormat", waterLevelPath, dataCodingFormat));
        attrs.add(new TabularAttr("interpolationType", waterLevelPath, interpolationType));
        attrs.add(new TabularAttr("methodWaterLevelProduct", waterLevelPath, methodWaterLevelProduct));
        attrs.add(new TabularAttr("numInstances", waterLevelPath, numInstances));
        H5.H5Gclose(waterLevelID);

        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String timeRecordInterval = String.valueOf(getShorttAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }


    private void DCF2Attr() {
        String waterLevelPath = "WaterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = String.valueOf(getShorttAttr(waterLevelID, "numInstances"));
        String sequencingRule_scanDirection = getStringAttr(waterLevelID, "sequencingRule.scanDirection");
        String sequencingRule_type = getEnumAttr(waterLevelID, "sequencingRule.type");

        attrs.add(new TabularAttr("commonPointRule", waterLevelPath, commonPointRule));
        attrs.add(new TabularAttr("dataCodingFormat", waterLevelPath, dataCodingFormat));
        attrs.add(new TabularAttr("interpolationType", waterLevelPath, interpolationType));
        attrs.add(new TabularAttr("methodWaterLevelProduct", waterLevelPath, methodWaterLevelProduct));
        attrs.add(new TabularAttr("numInstances", waterLevelPath, numInstances));
        attrs.add(new TabularAttr("sequencingRule.scanDirection", waterLevelPath, sequencingRule_scanDirection));
        attrs.add(new TabularAttr("sequencingRule.type", waterLevelPath, sequencingRule_type));
        H5.H5Gclose(waterLevelID);

        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String timeRecordInterval = String.valueOf(getShorttAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }

    private void DCF1Attr() {
        String waterLevelPath = "WaterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = String.valueOf(getShorttAttr(waterLevelID, "numInstances"));

        attrs.add(new TabularAttr("commonPointRule", waterLevelPath, commonPointRule));
        attrs.add(new TabularAttr("dataCodingFormat", waterLevelPath, dataCodingFormat));
        attrs.add(new TabularAttr("methodWaterLevelProduct", waterLevelPath, methodWaterLevelProduct));
        attrs.add(new TabularAttr("numInstances", waterLevelPath, numInstances));
        H5.H5Gclose(waterLevelID);

        int count = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        oname = removeElement("axisNames", oname);
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }

    private void getCommonAttr() {
        String path = "/";
        long rootID = H5.H5Gopen(fileID, path, H5P_DEFAULT);
        String geographicIdentifier = getStringAttr(rootID, "geographicIdentifier");
        this.identifier = geographicIdentifier;
        String issueDate = getStringAttr(rootID, "issueDate");
        String issueTime = getStringAttr(rootID, "issueTime");
        String metadata = getStringAttr(rootID, "metadata");
        String verticalCS = String.valueOf(getShorttAttr(rootID, "verticalCS"));
        String verticalCoordinateBase = getEnumAttr(rootID, "verticalCoordinateBase");
        String verticalDatumReference = getEnumAttr(rootID, "verticalDatumReference");
        String waterLevelTrendThreshold = String.valueOf(getFloatAttr(rootID, "waterLevelTrendThreshold"));

        attrs.add(new TabularAttr("geographicIdentifier", path, geographicIdentifier));
        attrs.add(new TabularAttr("issueDate", path, issueDate));
        attrs.add(new TabularAttr("issueTime", path, issueTime));
        attrs.add(new TabularAttr("metadata", path, metadata));
//        attrs.add(new TabularAttr("verticalCS", path, verticalCS));
        attrs.add(new TabularAttr("verticalCoordinateBase", path, verticalCoordinateBase));
        attrs.add(new TabularAttr("verticalDatumReference", path, verticalDatumReference));
        attrs.add(new TabularAttr("waterLevelTrendThreshold", path, waterLevelTrendThreshold));
        H5.H5Gclose(rootID);
    }

    private String getEnumAttr(long rootID, String attrName) {
        // Enum 8bit 1byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        String value = H5Tget_member_name(attrTypeID, bytes[0]);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        return value;
    }

    private int getIntAttr(long rootID, String attrName) {
        //  int 4byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int value = byteBuffer.getInt();
        return value;
    }

    private short getShorttAttr(long rootID, String attrName) {
        //  short 2byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short value = byteBuffer.getShort();
        return value;
    }

    private long getLongAttr(long rootID, String attrName) {
        //  long 8byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long value = byteBuffer.getLong();
        return value;
    }

    private float getFloatAttr(long rootID, String attrName) {
        // float 4byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        float value = byteBuffer.getFloat();
        return value;
    }

    private double getDoubleAttr(long rootID, String attrName) {
        // double 8byte
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        double value = byteBuffer.getDouble();
        return value;
    }

    private String getStringAttr(long rootID, String attrName) {
        long attrID = H5.H5Aopen_by_name(rootID, ".", attrName, H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        H5.H5Aclose(attrID);
        H5.H5Tclose(attrTypeID);
        return new String(bytes);
    }


    private void getAttribution() {
        switch (this.dcf) {
            case DCF1:
                break;
            case DCF2:
                break;
            case DCF3:
                break;
            case DCF7:
                break;
            case DCF8:
                break;
        }
    }

    private Dcf getDcf() {
        String rootPath = "WaterLevel";
        long rootID = H5.H5Gopen(fileID, rootPath, H5P_DEFAULT);
        long attrID = H5.H5Aopen_by_name(rootID, ".", "dataCodingFormat", H5P_DEFAULT, H5P_DEFAULT);
        long attrTypeID = H5.H5Aget_type(attrID);
        int size = (int) H5.H5Aget_storage_size(attrID);
        byte[] bytes = new byte[size];
        H5.H5Aread(attrID, attrTypeID, bytes);
        return Dcf.getDcf(bytes[0]);
    }

    private String[] removeElement(String name, String[] elements) {
        List<String> strings = new ArrayList(Arrays.asList(elements));
        strings.remove(name);
        String[] newElements = strings.toArray(new String[0]);
        return newElements;
    }

    class TabularAttr {
        String name;
        String path;
        String Value;

        public TabularAttr(String name, String path, String value) {
            this.name = name;
            this.path = path;
            Value = value;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String getValue() {
            return Value;
        }
    }

    private class ChartDataset {
        String name;
        ArrayList<Record> records;

        public ChartDataset() {
            this.records = new ArrayList<>();
        }

        public ChartDataset(ArrayList<Record> records) {
            this.records = records;
        }

        public void addRow(Record record) {
            this.records.add(record);
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class Record {
        float Height;
        int Trend;
        String identifier;
        String timePoint;

        double longitude;
        double latitude;

        public Record(float height, int trend, String identifier, String timePoint, double longitude, double latitude) {
            Height = height;
            Trend = trend;
            this.identifier = identifier;
            this.timePoint = timePoint;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public float getHeight() {
            return Height;
        }

        public int getTrend() {
            return Trend;
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getTimePoint() {
            return timePoint;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }
    }
}
