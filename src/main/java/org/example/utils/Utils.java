package org.example.utils;

import java.util.Random;

public class Utils {

    public static String numberGen(int len, int dupCd) {
        Random rand = new Random();
        String numStr = "";
        for(int i=0;i<len;i++) {
            String ran = Integer.toString(rand.nextInt(10));
            if(dupCd==1) {
                numStr += ran;
            }else if(dupCd==2) {
                if(!numStr.contains(ran)) {
                    numStr += ran;
                }else {
                    i-=1;
                }
            }
        }
        return numStr;
    }

}
