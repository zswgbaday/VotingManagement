package com.zsw.common.util;

import com.csvreader.CsvWriter;
import com.zsw.pojo.user.User;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil<T> {

    public static<T> ByteArrayOutputStream process(List<T> datalist) {

        if (datalist.size() == 0) {
            return null;
        }
        Class clazz = datalist.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> csvHeaderList = new ArrayList<>();
        for (Field field: fields) {
            csvHeaderList.add( field.getName());
        }
        //创建流
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CsvWriter csvWriter = new CsvWriter(output, ',', Charset.forName("GBK"));
        try {
            //写表头
            String[] tableheader = csvHeaderList.toArray(new String[csvHeaderList.size()]);
            csvWriter.writeRecord(tableheader);
            //写内容
            for (T data : datalist) {
                List<String> csvBodyList = new ArrayList<>();
                for (Field field: fields) {
                   
                        field.setAccessible(true);
                        Object obj = field.get(data);
                        if (obj != null) {
                            csvBodyList.add(obj.toString());
                        }
                   
                }
                csvWriter.writeRecord( csvBodyList.toArray(new String[csvBodyList.size()]));
            }
            csvWriter.close();
            output.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static  void  main (String [] a) throws IOException {
        List<User> ll = new ArrayList<>();
        for (int i = 0; i < 10; ++ i) {
            User user = new User();
            user.setId("1");
            user.setName("哈哈哈");
            ll.add(user);
        }
        ByteArrayOutputStream b =  process(ll);
        File f = new File("C:/Users/zswgbaday/Desktop/download.csv");
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(b.toByteArray());
        fo.flush();
        fo.close();
    }
}
