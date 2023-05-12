package org.example;


import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import io.jhdf.HdfFile;
import io.jhdf.api.Dataset;
import io.jhdf.api.Group;
import io.jhdf.api.Node;

import static java.util.stream.Collectors.joining;

public class Hdf5LibBenchmark {

    private static final String TEST_FILE = "C:\\Users\\zjm\\Desktop\\104US00_ches_dcf1_20190703T00Z.h5";
    private static final int REPEATS = 1000;

    private static final Map<Class<?>, Integer> hdf5TypesMap;
    static {
        hdf5TypesMap = new HashMap<>();
        hdf5TypesMap.put(int.class, HDF5Constants.H5T_NATIVE_INT32);
        hdf5TypesMap.put(double.class, HDF5Constants.H5T_NATIVE_DOUBLE);
    }

    public static void main(String[] args) throws Exception {

        List<DatasetInfo> datasets = new ArrayList<>();

        try (HdfFile hdfFile = new HdfFile(new File(TEST_FILE))) {
            recursivlyGetDatasets(hdfFile, datasets);
            System.out.println("Datasets to benchmark: "
                    + datasets.stream().map(DatasetInfo::getPath).collect(joining(", ")));
        } // Closes the file here

        for (DatasetInfo datasetInfo : datasets) {
            System.out.println("Benchmarking: " + datasetInfo.getPath());

            Object jHdfData;

            // First do jHDF
            try (HdfFile hdfFile = new HdfFile(new File(TEST_FILE))) {
                Dataset dataset = hdfFile.getDatasetByPath(datasetInfo.getPath());

                Object data = null;

                double[] results = new double[REPEATS];
                for (int i = 0; i < REPEATS; i++) {

                    // Here we actually time reading the data from the file into a Java object
                    long start = System.currentTimeMillis();

                    data = dataset.getData();

                    long took = System.currentTimeMillis() - start;

                    results[i] = took;
                }
                DescriptiveStatistics stats = new DescriptiveStatistics(results);
                System.out.println("jHDF results for: " + datasetInfo.getPath());
                System.out.println(stats.toString());

                // Keep the last time the data is read for equivalence check
                jHdfData = data;

            } // Close the jHDF file here

            // Now test the HDF5 group libiary

            // Open file using the default properties.
            int fileId = H5.H5Fopen(TEST_FILE, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
            // Open dataset using the default properties.
            int datasetId = H5.H5Dopen(fileId, datasetInfo.getPath(), HDF5Constants.H5P_DEFAULT);
            // Make the array to fill. jHDF does this internally
            Object data = Array.newInstance(datasetInfo.getType(), datasetInfo.getDims());

            double[] results = new double[REPEATS];
            for (int i = 0; i < REPEATS; i++) {

                // Here we actually time reading the data from the file into a Java object
                long start = System.currentTimeMillis();

                H5.H5Dread(datasetId, hdf5TypesMap.get(datasetInfo.getType()), HDF5Constants.H5S_ALL,
                        HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, data);

                long took = System.currentTimeMillis() - start;

                results[i] = took;
            }
            DescriptiveStatistics stats = new DescriptiveStatistics(results);
            System.out.println("HDF5 Group results for: " + datasetInfo.getPath());
            System.out.println(stats.toString());

            // Close the dataset.
            H5.H5Dclose(datasetId);
            // Close the file.
            H5.H5Fclose(fileId);

            // Now a sanity check compare th
            System.out.println(
                    "Arrays are equal? " + Arrays.deepEquals(new Object[] { data }, new Object[] { jHdfData }));

        }
    }

    private static void recursivlyGetDatasets(Group group, List<DatasetInfo> datasets) {
        for (Node node : group) {
            if (node instanceof Group) {
                Group group2 = (Group) node;
                recursivlyGetDatasets(group2, datasets);
            } else if (node instanceof Dataset) {
                datasets.add(new DatasetInfo((Dataset) node));
            }
        }
    }

    private static class DatasetInfo {

        private final String path;
        private final int[] dims;
        private final Class<?> type;

        public DatasetInfo(Dataset dataset) {
            this.path = dataset.getPath();
            this.dims = dataset.getDimensions();
            this.type = dataset.getJavaType();
        }

        public String getPath() {
            return path;
        }

        public int[] getDims() {
            return dims;
        }

        public Class<?> getType() {
            return type;
        }

    }

}
