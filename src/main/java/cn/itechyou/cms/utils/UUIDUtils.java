package cn.itechyou.cms.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDUtils {
    
    public static String getPrimaryKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * java生成随机数字和字母组合
     * 
     * @param length[生成随机数的长度]
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            if (random.nextInt(2) % 2 == 0) { // 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            }
            else { //num
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val.toLowerCase();
    }
}
