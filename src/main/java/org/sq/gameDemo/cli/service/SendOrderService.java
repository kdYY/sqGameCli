package org.sq.gameDemo.cli.service;

import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SendOrderService {



    /**
     * 注册
     * @param msgEntity
     * @param input
     */
    public void register(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            UserProto.RequestUserInfo data = getRequestUserInfo(input);
            msgEntity.setData(data.toByteArray());
        }
    }

    private UserProto.RequestUserInfo getRequestUserInfo(String[] input) throws Exception {
        Map<String,Object> map = splitCmdString(input);
        return UserProto.RequestUserInfo.newBuilder()
                .setMsgId(UUID.randomUUID().hashCode())
                .setTime(System.currentTimeMillis())
                .setUser(UserProto.User.newBuilder()
                        .setName((String) map.get("name"))
                        .setPassword((String) map.get("password"))
                        .build())
                .build();
    }

    /**
     * 登陆
     * @param msgEntity
     * @param input
     */
    public void login(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            UserProto.RequestUserInfo data = getRequestUserInfo(input);
            msgEntity.setData(data.toByteArray());
        }
    }


    /**
     * 获取指令帮助
     */
    public void help() {
        System.out.println(
                "注册: register name=test&password=123456\r\n"
                + "登陆: login name=kevins&password=123\r\n"
                        + "查看可创建角色: getRoleMsg\r\n"
                + "创建角色: bindRole id=3\r\n"
                + "跟指定npc讲话 talkwithnpc id=0"
        );
    }

    //
    public void talkToNpc(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            NpcPt.NpcReqInfo data = NpcPt.NpcReqInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setId(Integer.valueOf((String) map.get("id")))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }

    }

    /**
     * 绑定角色
     * @param msgEntity
     * @param input
     */
    public void bindRole(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            UserEntityProto.UserEntityRequestInfo data = UserEntityProto.UserEntityRequestInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setTypeId(Integer.valueOf((String) map.get("id")))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }

    }

    public void aoi(MsgEntity msgEntity) {
        SenceMsgProto.SenceMsgRequestInfo data = SenceMsgProto.SenceMsgRequestInfo.newBuilder()
                .setMsgId(UUID.randomUUID().hashCode())
                .setTime(System.currentTimeMillis())
                .build();
        msgEntity.setData(data.toByteArray());

    }

    //移动
    public void move(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            SenceProto.RequestSenceInfo data = SenceProto.RequestSenceInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setSenceId(Integer.valueOf((String) map.get("id")))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }

    }

    // name=adfa&password=123456
    //map{name,adfa},{password,123456}
    private Map splitCmdString(String[] input) throws Exception{
        Map<String, Object> map = new HashMap<>();
        if(input.length >= 2) {
            try {
                String[] split = input[1].split("&");
                for (String s : split) {
                    String[] split1 = s.split("=");
                    map.put(split1[0], split1[1]);
                }
            } catch (Exception e) {
                System.out.println("参数有误");
                throw e;
            }
        }
        return map;
    }


    public MsgEntity checkToken(String tokenFileName) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setCmdCode(OrderEnum.CheckToken.getOrderCode());
        UserProto.RequestUserInfo.Builder builder = UserProto.RequestUserInfo.newBuilder();
        InputStream resourceAsStream = SendOrderService.class.getClassLoader().getResourceAsStream(tokenFileName);
        //
        String token = readFileByChars();
        builder.setToken(token)
                .setMsgId(UUID.randomUUID().hashCode())
                .setTime(System.currentTimeMillis());
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    public static String readFileByChars() {
        String token = "";
        String filePath = System.getProperty("user.dir")
                + "/conf/token.txt";
        File file = new File(filePath);
        if(!file.exists()) {
            System.out.println("token文件不存在");
            return "";
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(filePath));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                if (((char) tempchar) != '\r') {
                    // System.out.print((char) tempchar);
                    token += (char) tempchar + "";
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return token;
    }

}
