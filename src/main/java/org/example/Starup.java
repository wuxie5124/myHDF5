package org.example;
public class Starup {
    public static void main(String[] args) {

        String filePath = "C:\\Users\\zjm\\Desktop\\104US00_ches_dcf2_20190606T12Z.h5";
        GetChartData getChartData = new GetChartData();
        getChartData.getChartData(filePath);

//        getChartData(filePath);
    }
}
