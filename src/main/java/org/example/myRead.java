package org.example;

import io.jhdf.HdfFile;
import io.jhdf.api.Dataset;
import io.jhdf.api.Group;
import io.jhdf.api.Node;
import io.jhdf.dataset.chunked.DatasetInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class myRead {
    private static final String File_Path = "D:\\DATA\\海图\\S104\\104US00_ches_dcf2_20190606T12Z.h5";
    public static void main(String[] args) {
        List<DatasetInfo> datasets = new ArrayList<>();
        HdfFile hdfFile = new HdfFile(new File(File_Path));
        recursivlyGetDatasets(hdfFile, datasets);
        for (DatasetInfo dataset : datasets) {
            Dataset datasetByPath = hdfFile.getDatasetByPath(dataset.getPath());
            Object data = datasetByPath.getData();
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
