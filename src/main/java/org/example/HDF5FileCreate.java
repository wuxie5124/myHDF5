package org.example;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
public class HDF5FileCreate {
    // The name of the file we'll create.
    private static String fname = "HDF5FileCreate.h5";

    public static void main(String args[]) throws Exception {
        int file_id = -1;

        // Create a new file using default properties.
        try {
            file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to create file:" + fname);
            return;
        }

        // Close the file.
        try {
            if (file_id >= 0)
                H5.H5Fclose(file_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // End of example that creates an empty HDF5 file named H5FileCreate.h5.
    }
}