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
                + "退出登录: exit\r\n"
                + "移动场景: move id=3; id= 1:起源之地, 2:村子, 3:城堡, 4:森林\r\n"
                + "查看可创建角色: getRoleMsg\r\n"
                + "查看自身技能: ShowPlayer\r\n"
                + "创建角色: bindRole id=3\r\n"
                + "跟指定npc讲话 talkwithnpc id=0\r\n"
                + "玩家用技能攻打目标 skillAttack skillId=2&targetId=123028834101628942\r\n"
                + "玩家查看背包 showbag\r\n"
                + "玩家查看装备 showequip\r\n"
                + "玩家使用背包中非装备物品 useItem id=23432412323&count=1\r\n"
                + "玩家使用背包中装备 addEquip id=23432412323\r\n"
                + "玩家丢弃背包中装备 removeItem id=23432412323&count=1\r\n"
                + "玩家修复装备 repairEquip id=23432412323&value=100; value可以忽略，表示修满该装备耐力点\r\n"
                + "玩家脱去身上装备 removeEquip part=0; id代表: 头:0, 身体:1, 手:2, 手指:3, 腰:4, 脚:5, 背包:9\r\n"
        );
    }

    /**
     * 跟npc对话
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void talkToNpc(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            NpcPt.NpcReqInfo data = NpcPt.NpcReqInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setId(Long.valueOf((String) map.get("id")))
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

    /**
     * 角色移动
     * @param msgEntity
     * @param input
     * @throws Exception
     */
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



    /**
     * 角色攻击
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void skillAttack(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String,Object> map = splitCmdString(input);
            SkillPt.SkillReqInfo data = SkillPt.SkillReqInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setSkillId(Integer.valueOf((String) map.get("skillId")))
                    .setTargetId(Long.valueOf((String) map.get("targetId")))
                    .setTime(System.currentTimeMillis())
                    .build();
            msgEntity.setData(data.toByteArray());
        }

    }

    /**
     * 展示背包
     * @param msgEntity
     * @throws Exception
     */
    public void showBag(MsgEntity msgEntity) throws Exception {

        BagPt.BagReqInfo data = BagPt.BagReqInfo.newBuilder()
                .setMsgId(UUID.randomUUID().hashCode())
                .setTime(System.currentTimeMillis())
                .build();
        msgEntity.setData(data.toByteArray());


    }


    /**
     * 展示身上装备
     * @param msgEntity
     */
    public void showEquip(MsgEntity msgEntity) throws Exception {
        ItemPt.ItemRequestInfo data = ItemPt.ItemRequestInfo.newBuilder()
                .setMsgId(UUID.randomUUID().hashCode())
                .setTime(System.currentTimeMillis())
                .build();
        msgEntity.setData(data.toByteArray());
    }

    /**
     * 使用物品
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void useItem(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            ItemPt.ItemRequestInfo data = ItemPt.ItemRequestInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setId(Long.parseLong((String) map.get("id")))
                    .setCount(Integer.parseInt((String) map.get("count")))
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }

    /**
     * 使用装备
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void addEquip(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            ItemPt.ItemRequestInfo data = ItemPt.ItemRequestInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setId(Long.parseLong((String) map.get("id")))
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }
    /**
     * 移除物品请求
     * @param msgEntity
     * @param input
     */
    public void removeItem(MsgEntity msgEntity, String[] input) throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            ItemPt.ItemRequestInfo data = ItemPt.ItemRequestInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setId(Long.parseLong((String) map.get("id")))
                    .setCount(Integer.parseInt((String) map.get("count")))
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }

    /**
     * 脱去装备
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void removeEquip(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            ItemPt.ItemRequestInfo data = ItemPt.ItemRequestInfo.newBuilder()
                    .setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setPartValue(Integer.parseInt((String) map.get("part")))
                    .build();
            msgEntity.setData(data.toByteArray());
        }
    }
    /**
     * 修复装备
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void requirEquip(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);

            ItemPt.ItemRequestInfo.Builder builder = ItemPt.ItemRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setId(Long.parseLong((String) map.get("id")))
                    .build();
            Object value = map.get("value");
            if(value != null) {
                builder.setRepairValue(Integer.parseInt((String) value));
            }
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    /**
     * 进入副本
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void enterCopy(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);

            SenceMsgProto.SenceMsgRequestInfo.Builder builder = SenceMsgProto.SenceMsgRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setSenceId(Integer.parseInt((String) map.get("id")))
                    .build();
            msgEntity.setData(builder.build().toByteArray());
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


    /**
     * 重连校验
     * @param tokenFileName
     * @return
     */
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

    /**
     * 读取token文件
     * @return
     */
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
