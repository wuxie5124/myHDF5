package org.example;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.HDF5Constants;

public class Hdf5FileOutputWriter {
    private static String fname = "rec1498945830.h5";
    private static String dsname = "dvs";
    private static long[] dims2D = {10, 10};

    public static void main(String args[]) throws Exception {
        long file_id = -1;
        long dataset_id = -1;

        // create the file and add groups and dataset into the file
        createFile();

        // Open file using the default properties.
        try {
            file_id = H5.H5Fopen(fname, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Open dataset using the default properties.
        try {
            if (file_id >= 0)
                dataset_id = H5.H5Dopen(file_id, dsname, HDF5Constants.H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Allocate array of pointers to two-dimensional arrays (the
        // elements of the dataset.
//        int[][] dataRead = new int[(int) dims2D[0]][(int) (dims2D[1])];
        long[] dims = {0,0};
        long[] dims2 = {0,0};
        long space_id = H5.H5Dget_space(dataset_id);
        H5.H5Sget_simple_extent_dims(space_id,dims,dims2);


        int[][] dataRead = new int[10][10];
        try {
            if (dataset_id >= 0)
                H5.H5Dread(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, dataRead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // print out the data values
        System.out.println("\n\nOriginal Data Values");
        for (int i = 0; i < (int) dims2D[0]; i++) {
            System.out.print("\n" + dataRead[i][0]);
            for (int j = 1; j < (int) dims2D[1]; j++) {
                System.out.print(", " + dataRead[i][j]);
            }
        }

        // change data value and write it to file.
        for (int i = 0; i < (int) dims2D[0]; i++) {
            for (int j = 0; j < (int) dims2D[1]; j++) {
                dataRead[i][j]++;
            }
        }

        // Write the data to the dataset.
        try {
            if (dataset_id >= 0)
                H5.H5Dwrite(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
                        dataRead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // reload the data value
        int[][] dataModified = new int[(int) dims2D[0]][(int) (dims2D[1])];

        try {
            if (dataset_id >= 0)
                H5.H5Dread(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, dataModified);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // print out the modified data values
        System.out.println("\n\nModified Data Values");
        for (int i = 0; i < (int) dims2D[0]; i++) {
            System.out.print("\n" + dataModified[i][0]);
            for (int j = 1; j < (int) dims2D[1]; j++) {
                System.out.print(", " + dataModified[i][j]);
            }
        }

        // Close the dataset.
        try {
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the file.
        try {
            if (file_id >= 0)
                H5.H5Fclose(file_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create the file and add groups ans dataset into the file, which is the
     * same as javaExample.H5DatasetCreate
     *
     * @throws Exception
     * @see HDF5DatasetCreate.H5DatasetCreate
     */
    private static void createFile() throws Exception {
        long file_id = -1;
        long dataspace_id = -1;
        long dataset_id = -1;

        // Create a new file using default properties.
        try {
            file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the data space for the dataset.
        try {
            dataspace_id = H5.H5Screate_simple(2, dims2D, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the dataset.
        try {
            if ((file_id >= 0) && (dataspace_id >= 0))
                dataset_id = H5.H5Dcreate(file_id, dsname,
                        HDF5Constants.H5T_STD_I32LE, dataspace_id,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Terminate access to the data space.
        try {
            if (dataspace_id >= 0)
                H5.H5Sclose(dataspace_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set the data values
        int[] dataIn = new int[(int) dims2D[0] * (int) dims2D[1]];
        for (int i = 0; i < (int) dims2D[0]; i++) {
            for (int j = 0; j < (int) dims2D[1]; j++) {
                dataIn[i * (int) dims2D[1] + j] = i * 100 + j;
            }
        }

        // Write the data to the dataset.
        try {
            if (dataset_id >= 0)
                H5.H5Dwrite(dataset_id, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, dataIn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // End access to the dataset and release resources used by it.
        try {
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the file.
        try {
            if (file_id >= 0)
                H5.H5Fclose(file_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}