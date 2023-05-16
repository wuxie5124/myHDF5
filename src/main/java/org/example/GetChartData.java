package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;
import hdf.hdf5lib.structs.H5O_info_t;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

import static hdf.hdf5lib.H5.*;
import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;

public class GetChartData {
    ArrayList<Attribution> attributions = new ArrayList<>();
    HashMap<String, Dataset> datasetMap = new HashMap<>();
    long fileID;

    public void getChartData(String filePath) {
        this.fileID = H5.H5Fopen(filePath, HDF5Constants.H5F_ACC_RDWR, H5P_DEFAULT);
        getGroupAttribute("/");
        String waterLevel = "WaterLevel";
        getGroupAttribute(waterLevel);


        int count = (int) H5.H5Gn_members(this.fileID, waterLevel);
        String[] oname = new String[count];
        int[] otype = new int[count];
        int[] ltype = new int[count];
        long[] orefs = new long[count];
        int index = -1;
        index = H5.H5Gget_obj_info_all(this.fileID, waterLevel, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);
        for (int i = 0; i < oname.length; i++) {
            if (otype[i] == 0) {
                String waterL = oname[i];
                String waterLPath = waterLevel + "/" + waterL;
                getGroupAttribute(waterLPath);
                String[] onameSub = new String[count];
                int[] otypeSub = new int[count];
                int[] ltypeSub = new int[count];
                long[] orefsSub = new long[count];
                H5Gget_obj_info_all(this.fileID, waterLPath, onameSub, otypeSub, ltypeSub, orefsSub, HDF5Constants.H5_INDEX_NAME);
                removeElement("Positioning",onameSub);
                for (String group : onameSub) {
                    String groupPath = waterLPath + "/" + group;
                    getGroupAttribute(groupPath);
                }
            }
        }
    }

    private boolean removeElement(String name, String[] names) {
        for (String na : names) {
            if (name.equals(na)) {
                return true;
            }
        }
        return false;
    }

    private void getGroupAttribute(String path) {
        long rootID = H5.H5Gopen(fileID, path, H5P_DEFAULT);
        getAttribution(rootID, path);
    }

    private void getDatasetAttribute(String path) {
        long rootID = H5.H5Dopen(fileID, path, H5P_DEFAULT);
        getAttribution(rootID, path);
    }

    private void getAttribution(long rootID, String path) {
        H5O_info_t info = H5Oget_info(fileID);
        for (int i = 0; i < info.num_attrs; i++) {
            //WaterLevel/WaterLevel.01/Group_001/values WaterLevel/axisNames Group_F/WaterLevel
            long attributeId = H5.H5Aopen_by_idx(rootID, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            String name = H5Aget_name(attributeId);
            int size = (int) H5.H5Aget_storage_size(attributeId);
            byte[] data = new byte[size];
            long attributeType = H5Aget_type(attributeId);
            boolean b = (H5Tget_class(attributeType)) == HDF5Constants.H5T_STRING;
            String s = H5Tget_member_name(attributeType, 0);
            H5.H5Aread(attributeId, attributeType, data);
            Object attrValue = getData(attributeType, data);
            Attribution attribution = new Attribution(name, attrValue, path);
            this.attributions.add(attribution);
            H5.H5Aclose(attributeId);
        }
    }

    private Object getData(long type, byte[] data) {
        if (H5Tget_class(type) == HDF5Constants.H5T_STRING) {
            return new String(data);
        } else if (H5Tget_class(type) == HDF5Constants.H5T_INTEGER) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            return byteBuffer.getInt();
        } else if (H5Tget_class(type) == HDF5Constants.H5T_FLOAT) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            return byteBuffer.getDouble();
        } else if (H5Tget_class(type) == HDF5Constants.H5T_ENUM) {
            return (int) data[0];
        }
        return null;
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

    class Dataset {
        private String name;
        private Object[][] value;

        public Dataset(String name, Object[][] value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object[][] getValue() {
            return value;
        }

        public void setValue(Object[][] value) {
            this.value = value;
        }
    }

    class Attribution {
        String name;
        Object value;
        String path;

        public Attribution(String name, Object value, String path) {
            this.name = name;
            this.value = value;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
