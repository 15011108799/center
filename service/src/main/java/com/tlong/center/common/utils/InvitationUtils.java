package com.tlong.center.common.utils;

import java.util.UUID;

/**
 * 邀请码工具类
 */
public class InvitationUtils {

    /**
     * 生成邀请码
     * 生成规则  用户id+uuid第3.5.7位+补充uuid倒数从最后一位到前数(补充足够10位) 如果用户id长度大于1则先补充用户id再加上uuid倒数
     */
    public static String createInvitation(Long userId) {
        String strUUID = UUID.randomUUID().toString();
        String strUserId = String.valueOf(userId);
//        if (strUserId.length() == 1){
//            return strUserId + strUUID.charAt(2) + strUUID.charAt(4) + strUUID.charAt(6) +
//                    strUUID.substring(strUUID.length() - 7, strUUID.length());
//        }else if (strUserId.length() > 1){
//            if (strUserId.length() <=7){
//                System.out.println(strUUID);
                return strUserId + strUUID.charAt(2) + strUUID.charAt(4) + strUUID.charAt(6) +
                    strUUID.substring(strUUID.length() - (10-strUserId.length()-3), strUUID.length());
//            }else {
//                return strUserId + strUUID.charAt(2) + strUUID.charAt(4) + strUUID.charAt(6) +
//                        strUUID.substring(strUUID.length() - (10-strUserId.length()-3), strUserId.length());
//            }
//        }
//        return "编码生成失败";
    }
}
