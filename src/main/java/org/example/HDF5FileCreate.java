package org.example;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;

public class HDF5FileCreate {
    // The name of the file we'll create.
    private static String fname = "C:\\Users\\zjm\\Desktop\\104US00_ches_dcf1_20190703T00Z.h5";

    public static void main(String args[]) throws Exception {
        int file_id = -1;

        // Create a new file using default properties.
        try {
//            file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
//                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            file_id = H5.H5Fopen(fname, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
            int waterLevel;
            if (file_id >= 0) {
                int dataset_id = H5.H5Gopen(file_id, "Group_F", HDF5Constants.H5P_DEFAULT);
                if (dataset_id>0) {
                    waterLevel = H5.H5Dopen(dataset_id, "WaterLevel", HDF5Constants.H5P_DEFAULT);
                    System.out.println(waterLevel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to create file:" + fname);
            return;
        }

        // Close the file.
        try {
            if (file_id >= 0)
                H5.H5Fclose(file_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // End of example that creates an empty HDF5 file named H5FileCreate.h5.
    }
}