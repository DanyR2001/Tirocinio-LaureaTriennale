package com.example.etlparser.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isCSV(File file){
        List<String> list =new ArrayList<String>(List.of(file.getName().split("\\.")));
        String extension = list.get(list.size()-1);
        return extension.equals("csv");
    }


    public static  boolean isEqualHeaderList(List<String> list1,List<String> list2){
        int len=list1.size()==list2.size()?list1.size():0;
        for (int i=0;i<len;i++) {
            String s1=list1.get(i);
            String s2=list2.get(i);
            int len1=s1.length()==s2.length()?s1.length():0;
            for(int j=0;j<len1;j++){
                if (s1.charAt(j)!=s2.charAt(j)){
                    System.out.println(s1+" ->"+s1.charAt(j)+" ,"+s2+"->"+s2.charAt(j));
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean boolanValue(String val){
        return val.equals("1");
    }
}
