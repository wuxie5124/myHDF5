package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class Example {
    private static String FILENAME ="C:\\Users\\zjm\\Desktop\\104US00_ches_dcf1_20190703T00Z.h5";//原始.h5文件路径
    private static String PATH="/";//传入跟组
    private static String DATASETNAME;//数据集名称
    private static final long DIM0 = 100;//设置数据集行数  之后会动态获得行数，实现对应缩容（建议初始值设置大些）
    protected static final int INTEGERSIZE = 4;//设置整型长度
    protected static final int LONGSIZE=8;//设置长整型长度
    protected static final int FLOATSIZE=4;//设置单精度长度
    protected static final int DOUBLESIZE = 8;//设置双精度型长度
    protected final static int MAXSTRINGSIZE = 80;//设置字符串最大长度

    static class Sensor_Datatype {
        static int numberMembers = 5;//表示列项数
        static int[] memberDims = { 1, 1, 1, 1 , 1 };
        static String[] memberNames = {  //定义表中所有字段名称
                "trading_day", "updatetime", "instrument_id", "gap_number" , "reserve"
        };//数据元素为具体的表字段名称

        static long[] memberMemTypes = {  //对应字段类型值
                HDF5Constants.H5T_NATIVE_INT, HDF5Constants.H5T_NATIVE_LONG, HDF5Constants.H5T_NATIVE_FLOAT, HDF5Constants.H5T_NATIVE_DOUBLE, HDF5Constants.H5T_C_S1
        };//用于定义每个字段元素的类型  指定字段元素类型的时候注意类型长度，如果小于存储数长度，则会发生数据溢出

        static long[] memberFileTypes = { //对应字段类型大小
                HDF5Constants.H5T_STD_I32BE, HDF5Constants.H5T_STD_I64BE, HDF5Constants.H5T_IEEE_F32BE, HDF5Constants.H5T_IEEE_F64BE, HDF5Constants.H5T_C_S1
        };//对应的字段类型的大小

        static int[] memberStorage = {  //对应字段类型值长度
                INTEGERSIZE, LONGSIZE, FLOATSIZE, DOUBLESIZE, MAXSTRINGSIZE
        };//定义对应类型的长度大小

        // Data size is the storage size for the members.
        static long getTotalDataSize() {
            long data_size = 0;
            for (int indx = 0; indx < numberMembers; indx++)
                data_size += memberStorage[indx] * memberDims[indx];
            return DIM0 * data_size;
        }

        static long getDataSize() {
            long data_size = 0;
            for (int indx = 0; indx < numberMembers; indx++)
                data_size += memberStorage[indx] * memberDims[indx];
            return data_size;
        }

        static int getOffset(int memberItem) {
            int data_offset = 0;
            for (int indx = 0; indx < memberItem; indx++)
                data_offset += memberStorage[indx];
            return data_offset;
        }
    }

    static class Sensor {
        public Integer trading_day;
        public Long updatetime;
        public Float instrument_id;
        public Double gap_number;
        public String reserve;

        public Sensor(Integer trading_day, Long updatetime, Float instrument_id, Double gap_number, String reserve) {
            super();
            this.trading_day = trading_day;
            this.updatetime = updatetime;
            this.instrument_id = instrument_id;
            this.gap_number = gap_number;
            this.reserve = reserve;
        }

        Sensor(ByteBuffer databuf, int dbposition) {
            readBuffer(databuf, dbposition);
        }

        //遍历.h5下边的所有数据集，并以数组的方式返回数据集名称
        private static String[] do_iterate() {
            int file_id = -1;

            // Open a file using default properties.
            try {
                file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Begin iteration.
            System.out.println("Objects in root group:");
            try {
                if (file_id >= 0) {
                    int count = (int) H5.H5Gn_members(file_id, PATH);
                    String[] oname = new String[count];
                    int[] otype = new int[count];
                    int[] ltype = new int[count];
                    long[] orefs = new long[count];
                    H5.H5Gget_obj_info_all(file_id, PATH, oname, otype, ltype, orefs, HDF5Constants.H5_INDEX_NAME);

                    // Get type of the object and display its name and type.
                    for (int indx = 0; indx < otype.length; indx++) {
                        switch (H5O_type.get(otype[indx])) {
                            case H5O_TYPE_GROUP:
                                System.out.print("  Group: " + oname[indx]+","+oname.length+" ");
                                break;
                            case H5O_TYPE_DATASET:
                                System.out.print("  Dataset: " + oname[indx]+","+oname.length+" ");
                                break;
                            case H5O_TYPE_NAMED_DATATYPE:
                                System.out.print("  Datatype: " + oname[indx]+","+oname.length+" ");
                                break;
                            default:
                                System.out.print("  Unknown: " + oname[indx]+","+oname.length+" ");
                        }
                    }
                    System.out.println();
                    return oname;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Close the file.
            try {
                if (file_id >= 0)
                    H5.H5Fclose(file_id);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return new String[]{"数据集遍历出错"};
        }

        //读数据
        void readBuffer(ByteBuffer databuf, int dbposition) {
            //0
            this.trading_day = databuf.getInt(dbposition + Sensor_Datatype.getOffset(0));
            //1
            this.updatetime = databuf.getLong(dbposition + Sensor_Datatype.getOffset(1));
            //2
            this.instrument_id= databuf.getFloat(dbposition + Sensor_Datatype.getOffset(2));
            //3
            this.gap_number = databuf.getDouble(dbposition + Sensor_Datatype.getOffset(3));
            //4
            ByteBuffer stringbuf_reserve = databuf.duplicate();
            stringbuf_reserve.position(dbposition + Sensor_Datatype.getOffset(4));
            stringbuf_reserve.limit(dbposition + Sensor_Datatype.getOffset(4) + MAXSTRINGSIZE);
            byte[] bytearr_reserve = new byte[stringbuf_reserve.remaining()];
            stringbuf_reserve.get(bytearr_reserve);
            this.reserve = new String(bytearr_reserve, Charset.forName("UTF-8")).trim();
        }

        //重写toString()方法
        @Override
        public String toString() {
            DecimalFormat df=new DecimalFormat("#0.0000");//浮点数保留4位小数，不足，用0补齐
            return  trading_day + ", " + updatetime + ", "
                    + df.format(instrument_id) + ", " + df.format(gap_number)+ ", " + reserve;
        }

    }

    //读取表结构数据集
    private static void ReadDataset() {
        int file_id = -1;
        int strtype_id = -1;
        int memtype_id = -1;
        int dataspace_id = -1;
        int dataset_id = -1;
        long[] dims = { DIM0 };
        Sensor[] object_data2 = new Sensor[(int) dims[0]];;
        byte[] dset_data;
        String[] dsetName= {};

        //遍历.h5文件下边的所有数据集
        dsetName=Sensor.do_iterate();
//        System.out.println("数组大小为："+dsetName.length);

        //遍历得到文件下边的各个数据集  Open an existing dataset.
        for(int i=0;i<dsetName.length;i++) {
            // Open an existing file.
            try {
                file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            DATASETNAME=dsetName[i];
//        	System.out.println("================="+DATASETNAME);
            try {
                if (file_id >= 0)
                    dataset_id = H5.H5Dopen(file_id, DATASETNAME, HDF5Constants.H5P_DEFAULT);
                //动态得到数据具体的行列数
//                int rcNum=H5.H5Tget_(dataset_id, new int[] {});
//                System.out.println("动态获取表的行列数："+rcNum);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Get dataspace and allocate memory for read buffer.
            try {
                if (dataset_id >= 0)
                    dataspace_id = H5.H5Dget_space(dataset_id);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (dataspace_id >= 0)
                    H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Create string datatype.
            try {
                strtype_id = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
                if (strtype_id >= 0)
                    H5.H5Tset_size(strtype_id, MAXSTRINGSIZE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Create the compound datatype for memory.
            try {
                memtype_id = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, Sensor_Datatype.getDataSize());
                if (memtype_id >= 0) {
                    for (int indx = 0; indx < Sensor_Datatype.numberMembers; indx++) {
                        int type_id = (int) Sensor_Datatype.memberMemTypes[indx];
                        if (type_id == HDF5Constants.H5T_C_S1)
                            type_id = strtype_id;
                        H5.H5Tinsert(memtype_id, Sensor_Datatype.memberNames[indx], Sensor_Datatype.getOffset(indx),
                                type_id);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // allocate memory for read buffer.
            dset_data = new byte[(int) dims[0] * (int)Sensor_Datatype.getDataSize()];

            // Read data.
            try {
                if ((dataset_id >= 0) && (memtype_id >= 0))
                    H5.H5Dread(dataset_id, memtype_id, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                            HDF5Constants.H5P_DEFAULT, dset_data);
                ByteBuffer inBuf = ByteBuffer.wrap(dset_data);
                inBuf.order(ByteOrder.nativeOrder());
                for (int indx = 0; indx < (int) dims[0]; indx++) {
                    object_data2[indx] = new Sensor(inBuf, indx * (int)Sensor_Datatype.getDataSize());//将读取到的数据存入该数组
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Output the data to the screen.
            for (int indx = 0; indx < dims[0]; indx++) {
                System.out.println(DATASETNAME + " [" + indx + "]:");
                System.out.println(object_data2[indx].toString());
            }
            System.out.println();

            //写到本地的文本中
            Example.writeLocalExcel(object_data2,DATASETNAME,dims[0]);

            try {
                if (dataset_id >= 0)
                    H5.H5Dclose(dataset_id);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Terminate access to the data space.
            try {
                if (dataspace_id >= 0)
                    H5.H5Sclose(dataspace_id);
            } catch (Exception e){
                e.printStackTrace();
            }

            // Terminate access to the mem type.
            try {
                if (memtype_id >= 0)
                    H5.H5Tclose(memtype_id);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (strtype_id >= 0)
                    H5.H5Tclose(strtype_id);
            }
            catch (Exception e) {
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

    enum H5O_type {
        H5O_TYPE_UNKNOWN(-1), // Unknown object type
        H5O_TYPE_GROUP(0), // Object is a group
        H5O_TYPE_DATASET(1), // Object is a dataset
        H5O_TYPE_NAMED_DATATYPE(2), // Object is a named data type
        H5O_TYPE_NTYPES(3); // Number of different object types
        private static final Map<Integer, H5O_type> lookup = new HashMap<Integer, H5O_type>();

        static {
            for (H5O_type s : EnumSet.allOf(H5O_type.class))
                lookup.put(s.getCode(), s);
        }

        private int code;

        H5O_type(int layout_type) {
            this.code = layout_type;
        }

        public int getCode() {
            return this.code;
        }

        public static H5O_type get(int code) {
            return lookup.get(code);
        }
    }

    //将读出的数据写到本地
    public static void writeLocalExcel(Sensor[] dataSensors,String datasetName,long dims) {
        //测试数据写出 io
        String url="C:\\Users\\zjm\\Desktop\\";
        String dsetUrl=datasetName;
        String path=url+dsetUrl+".txt";
        File file=new File(path);
        if(file.exists()) {
            file.delete();
        }else {
            file=new File(path);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//    	DecimalFormat df=new DecimalFormat("0.0000");
        //执行遍历写出
        try {
            PrintWriter pw=new PrintWriter(file,"UTF-8");
            pw.append(
                    "trading_day, updatetime, instrument_id, gap_number, reserve "
            );
            pw.print("\n");
            for(int i=0;i<dims;i++) {
                StringBuffer sb=new StringBuffer("");
                Sensor data=dataSensors[i];//当前获取的字段值
                if(i<dataSensors.length-1) {
                    sb.append(data).append(","+"\t");
                }else {
                    sb.append(data).append(" ");
                }
                if(null!=data) {//此时不做判断会发生空指针异常 定义的h5文件行数固定，各个文件行数不同
                    pw.print(data.toString()+"\n");
                }
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Example.ReadDataset();
    }
}