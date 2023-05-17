package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import static hdf.hdf5lib.H5.H5Tget_member_name;
import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class S104Read {
    private long fileID;
    private Dcf dcf;

    public S104Read(String filePath) {
        this.fileID = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, H5P_DEFAULT);
        this.dcf = getDcf();
        this.attrs = new ArrayList<>();
        getInfo();
    }

    private void getInfo() {
        getDataset();
        getTabular();
    }

    ArrayList<TabularAttr> attrs;

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

    private void DCF8Attr() {
        String waterLevelPath = "waterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = getStringAttr(waterLevelID, "numInstances");
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
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
            int countSub = (int) H5.H5Gn_members(this.fileID, waterLevelPath);
            String[] onameSub = new String[countSub];
            int[] otypeSub = new int[countSub];
            int[] ltypeSub = new int[countSub];
            long[] orefsSub = new long[countSub];
            H5.H5Gget_obj_info_all(this.fileID, waterLevelPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
            for (int j = 0; j < onameSub.length; j++) {
                String groupName = onameSub[j];
                String groupPath = waterLPath + "/" + groupName;
                long group0ID = H5.H5Gopen(fileID, groupPath, H5P_DEFAULT);
                String endDateTime = getStringAttr(group0ID, "endDateTime");
                String numberOfTime = String.valueOf(getIntAttr(group0ID, "numberOfTime"));
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
                    String timeRecordInterval = String.valueOf(getIntAttr(group0ID, "timeRecordInterval"));
                    attrs.add(new TabularAttr("timeRecordInterval", groupPath, timeRecordInterval));
                }
            }
        }
    }

    private void DCF7Attr() {
        String waterLevelPath = "waterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = getStringAttr(waterLevelID, "numInstances");

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
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String numberOfTriangles = String.valueOf(getIntAttr(waterLevel0ID, "numberOfTriangles"));
            String timeRecordInterval = String.valueOf(getIntAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("numberOfTriangles", waterLPath, numberOfTriangles));
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }

    private void DCF3Attr() {
        String waterLevelPath = "waterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = getStringAttr(waterLevelID, "numInstances");

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
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String timeRecordInterval = String.valueOf(getIntAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }


    private void DCF2Attr() {
        String waterLevelPath = "waterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String interpolationType = getEnumAttr(waterLevelID, "interpolationType");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = getStringAttr(waterLevelID, "numInstances");
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
        for (int i = 0; i < oname.length; i++) {
            String waterL = oname[i];
            String waterLPath = waterLevelPath + "/" + waterL;
            long waterLevel0ID = H5.H5Gopen(fileID, waterLPath, H5P_DEFAULT);
            String timeRecordInterval = String.valueOf(getIntAttr(waterLevel0ID, "timeRecordInterval"));
            String typeOfWaterLevelData = getEnumAttr(waterLevel0ID, "typeOfWaterLevelData");
            attrs.add(new TabularAttr("timeRecordInterval", waterLPath, timeRecordInterval));
            attrs.add(new TabularAttr("typeOfWaterLevelData", waterLPath, typeOfWaterLevelData));
            H5.H5Gclose(waterLevel0ID);
        }
    }

    private void DCF1Attr() {
        String waterLevelPath = "waterLevel";
        long waterLevelID = H5.H5Gopen(fileID, waterLevelPath, H5P_DEFAULT);
        String commonPointRule = getStringAttr(waterLevelID, "commonPointRule");
        String dataCodingFormat = getEnumAttr(waterLevelID, "dataCodingFormat");
        String methodWaterLevelProduct = getStringAttr(waterLevelID, "methodWaterLevelProduct");
        String numInstances = getStringAttr(waterLevelID, "numInstances");

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
        String issueDate = getStringAttr(rootID, "issueDate");
        String issueTime = getStringAttr(rootID, "issueTime");
        String metadata = getStringAttr(rootID, "metadata");
        String verticalCS = String.valueOf(getIntAttr(rootID, "verticalCS"));
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

    private void getDataset() {


    }

    private String getEnumAttr(long rootID, String attrName) {
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

    private float getFloatAttr(long rootID, String attrName) {
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
}
