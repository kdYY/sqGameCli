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
                + "退出登录: exit\r\n\n"
                + "移动场景: move id=3; id= 1:起源之地, 2:村子, 3:城堡, 4:森林\r\n"
                + "查看可创建角色: getRoleMsg\r\n"
                + "查看自身技能: ShowPlayer\r\n"
                + "创建角色: bindRole id=3\r\n"
                + "跟指定npc讲话 talkwithnpc id=0\r\n\n"
                + "玩家用技能攻打目标 skillAttack skillId=2&targetId=123028834101628942 targetId=#{怪物id, 玩家id}\r\n\n"
                + "玩家查看所有物品种类信息 "+ OrderEnum.SHOW_ITEMINFO.getOrder() +" \r\n"
                + "玩家查看背包 "+ OrderEnum.TIDY_BAG.getOrder() + "\r\n"
                + "玩家整理背包 "+ OrderEnum.SHOW_BAG.getOrder() + "\r\n"
                + "玩家查看装备 "+ OrderEnum.SHOW_EQUIP.getOrder() + "\r\n"
                + "玩家使用背包中非装备物品 "+ OrderEnum.USE_ITEM.getOrder() + " id=23432412323&count=1\r\n"
                + "玩家使用背包中装备 addEquip id=23432412323\r\n"
                + "玩家丢弃背包中装备 removeItem id=23432412323&count=1\r\n"
                + "玩家修复装备 repairEquip id=23432412323&value=100; value可以忽略，表示修满该装备耐力点\r\n"
                + "玩家脱去身上装备 removeEquip part=0; id代表: 头:0, 身体:1, 手:2, 手指:3, 腰:4, 脚:5, 背包:9\r\n\n"
                + "玩家查看副本列表 "+ OrderEnum.SHOW_COPY_SENCE.getOrder() + "\r\n"
                + "玩家进入新的副本 "+ OrderEnum.ENTER_NEW_COPY.getOrder() +" id=1;\r\n"
                + "玩家查看副本列表 "+ OrderEnum.SHOW_EXIST_COPY_SENCE.getOrder() + "\r\n"
                + "玩家进入存在的副本 "+ OrderEnum.ENTER_COPY.getOrder() +" senceId=1;\r\n\n"
                + "玩家查看商店 "+ OrderEnum.SHOW_STORE.getOrder() +" \r\n"
                + "玩家购买商店中的物品 "+ OrderEnum.BUY_SHOP_ITEM.getOrder() +" itemInfoId=1005&count=100\r\n\n"
                + "玩家发送邮件 "+ OrderEnum.SEND_MAIL.getOrder() +" title=送给我的小哥哥&content=送你100瓶血瓶&recevier=安妮&id=#{背包中血瓶的id}&count=100\r\n"
                + "玩家查看一封邮件 "+ OrderEnum.GET_MAIL.getOrder() +" id=#{邮箱列表中的邮件id}\r\n"
                + "玩家查看邮件列表 "+ OrderEnum.SHOW_ALL_MAIL.getOrder() +" \r\n"
                + "玩家收取一封邮件 "+ OrderEnum.RECEIVE_MAIL.getOrder() +" id=#{邮箱列表中的邮件id}\r\n"
                + "玩家一键收取邮件 "+ OrderEnum.RECEIVE_ALL_MAIL.getOrder() +" \r\n\n"

                + "玩家发起面对面交易 "+ OrderEnum.START_ONLINE_TRADE.getOrder() +
                        " accpeterId=#{目标玩家id}&auctionItemId=#{背包中交易物品的id}&autionCount=#{要交易的数量}&itemInfoId=#{期望获得的物品种类id}&accpetCount" +
                        "=#{期望获得的物品数量}\r\n"
                + "玩家接受面对面交易 "+ OrderEnum.ACCEPT_ONLINE_TRADE.getOrder() +" id=#{交易号}\r\n"
                + "玩家获取自己发起的正在进行中的面对面交易 "+ OrderEnum.GET_ONLINE_TRADE.getOrder() +" \r\n"
                + "玩家获取自己还未处理的面对面交易 "+ OrderEnum.GET_ONLINE_TRADE_CAN_RECEIVE.getOrder() +" \r\n\n"
                + "玩家获取历史面对面交易记录 "+ OrderEnum.GET_ONLINE_TRADE_HISTORY.getOrder() +" \r\n\n"
                + "查看交易栏上的拍卖品 "+ OrderEnum.GET_DEAL.getOrder() +" \r\n"
                + "玩家把物品放入交易栏拍卖 "+ OrderEnum.START_DEAL_TRADE.getOrder() +
                        " accpeterId=#{目标玩家id}&auctionItemId=#{背包中交易物品的id}&autionCount=#{要交易的数量}&price=#{竞拍价格}&\r\n"
                + "玩家参与交易栏竞拍 "+ OrderEnum.ACCEPT_DEAL_TRADE.getOrder() +" id=#{交易号}&price=#{玩家给出的竞拍价格}\r\n"
                + "玩家查看交易栏上可竞拍的拍卖品 "+ OrderEnum.GET_DEAL_CAN_BUY.getOrder() +" \r\n"
                + "玩家查看参与交易的交易栏记录 "+ OrderEnum.GET_DEAL_HISTORY.getOrder() +" \r\n\n"

                + "玩家创建公会 "+ OrderEnum.CREATE_GUILD.getOrder() +" name=#{玩家想创建的公会名称}\r\n"
                + "玩家查看自己创建所有公会 "+ OrderEnum.SHOW_CHAIRMAN_GUILD.getOrder() +"\r\n"
                + "玩家查看加入的公会 "+ OrderEnum.SHOW_GUILDLIST.getOrder() +"\r\n"
                + "玩家查看可以加入的公会 "+ OrderEnum.SHOW_GUILD_CAN_ATTEND.getOrder() +"\r\n"
                + "玩家发起参加公会的申请 "+ OrderEnum.APPLY_ATTEND_GUILD.getOrder() +" id=#{玩家想进入的公会id}\r\n"
                + "会长玩家查看公会的入会申请 "+ OrderEnum.SHOW_GUILD_REQUEST.getOrder() +" id=#{公会id}\r\n"
                + "会长玩家同意其他玩家入会申请 "+ OrderEnum.AGREE_ATTEND_REQUEST.getOrder() +" id=#{公会id}&reqId=#{申请号}\r\n"
                + "会长玩家获取公会物品 "+ OrderEnum.GET_GUILD_ITEM.getOrder() +" id=#{公会id}&itemInfoId=#{公会中仓库的物品种类}&count=#{想获得的数量}\r\n"
                + "公会成员捐献物品到公会中 "+ OrderEnum.DONATE_ITEM.getOrder() +" id=#{公会id}&itemId=#{背包中物品id}&count=#{想捐献的数量}\r\n"
                + "公会成员退出公会 "+ OrderEnum.EXIT_GUILD.getOrder() +" id=#{公会id}\r\n\n"


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
     * 展示背包
     * @param msgEntity
     * @throws Exception
     */
    public void tidyBag(MsgEntity msgEntity) throws Exception {

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
    public void enterNewCopy(MsgEntity msgEntity, String[] input)  throws Exception {
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
                    .setSenceId(Integer.parseInt((String) map.get("senceId")))
                    .build();
            msgEntity.setData(builder.build().toByteArray());
        }
    }


    /**
     * 购买商店物品
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void buyGood(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);

            StorePt.StoreRequestInfo.Builder builder = StorePt.StoreRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setItemInfoId(Integer.parseInt((String) map.get("itemInfoId")))
                    .setCount(Integer.parseInt((String) map.get("count")))
                    .setId(1)
                    .build();
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    /**
     * 发送邮件
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public MsgEntity sendMail(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            MailPt.Mail.Builder mailbuild = MailPt.Mail.newBuilder();
            MailPt.Mail mail = mailbuild.setTitle((String) map.get("title"))
                    .setContent((String) map.get("content"))
                    .setRecevierName((String) map.get("recevier"))
                    .build();


            MailPt.MailRequestInfo.Builder builder = MailPt.MailRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setMail(mail)
                    .addItem(ItemPt.Item.newBuilder()
                            .setId(Long.valueOf((String) map.get("id")))
                            .setCount(Integer.parseInt((String) map.get("count")))
                    )
                    .build();
            msgEntity.setData(builder.build().toByteArray());

        }
        return msgEntity;
    }

    /**
     * 获取邮件内容
     * @param msgEntity
     * @param input
     * @return
     * @throws Exception
     */
    public MsgEntity getMail(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            MailPt.MailRequestInfo.Builder builder = MailPt.MailRequestInfo.newBuilder();
            builder.setId(Integer.parseInt((String) map.get("id"))).build();
            msgEntity.setData(builder.build().toByteArray());
        }
        return msgEntity;
    }

    /**
     * 提取邮件
     * @param msgEntity
     * @param input
     * @return
     * @throws Exception
     */
    public MsgEntity receieveMail(MsgEntity msgEntity, String[] input)  throws Exception {
       return getMail(msgEntity, input);
    }


    // name=adfa&password=123456
    //map{name,adfhelpa},{password,123456}
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

    /**
     * 发起面对面交易
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void startOnlineTrade(MsgEntity msgEntity, String[] input)  throws Exception {

        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            TradePt.TradeRequestInfo.Builder builder = TradePt.TradeRequestInfo.newBuilder();
            builder.setAccpeterId(Long.valueOf((String) map.get("accpeterId")))
                    .setAuctionItemId(Long.valueOf((String)map.get("auctionItemId")))
                    .setAutionCount(Integer.valueOf((String)map.get("autionCount")))
                    .setItemInfoId(Integer.valueOf((String)map.get("itemInfoId")))
                    .setAccpetCount(Integer.valueOf((String)map.get("accpetCount")));
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    /**
     * 接受面对面交易
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void accpetOnlineTrade(MsgEntity msgEntity, String[] input) throws Exception{
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            TradePt.TradeRequestInfo.Builder builder = TradePt.TradeRequestInfo.newBuilder();
            builder.setId(Integer.valueOf((String) map.get("id")));
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    /**
     * 发起交易栏交易
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void startDealTrade(MsgEntity msgEntity, String[] input)  throws Exception {
///startdealTrade auctionItemId=6893390492571144192&auctionCount=1&price=1888&tradeModel=2
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            TradePt.TradeRequestInfo.Builder builder = TradePt.TradeRequestInfo.newBuilder();
            builder.setAuctionItemId(Long.valueOf((String)map.get("auctionItemId")))
                    .setAutionCount(Integer.valueOf((String)map.get("autionCount")))
                    .setPrice(Integer.valueOf((String)map.get("price")))
                    .setTradeModel(Integer.valueOf((String)map.get("tradeModel")));
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    /**
     * 竞拍交易栏的物品
     * @param msgEntity
     * @param input
     * @throws Exception
     */
    public void accpetDealTrade(MsgEntity msgEntity, String[] input) throws Exception{
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            TradePt.TradeRequestInfo.Builder builder = TradePt.TradeRequestInfo.newBuilder();
            builder.setId(Integer.valueOf((String) map.get("id")))
                    .setPrice(Integer.valueOf((String)map.get("price")));
            msgEntity.setData(builder.build().toByteArray());
        }
    }


    /**
     * + "玩家创建公会 "+ OrderEnum.CREATE_GUILD.getOrder() +" name=#{玩家想创建的公会名称}\r\n"
     *                 + "玩家查看自己创建所有公会 "+ OrderEnum.SHOW_CHAIRMAN_GUILD.getOrder() +"\r\n"
     *                 + "玩家查看加入的公会 "+ OrderEnum.SHOW_GUILDLIST.getOrder() +"\r\n"
     *                 + "玩家查看可以加入的公会 "+ OrderEnum.SHOW_GUILD_CAN_ATTEND.getOrder() +"\r\n"
     *                 + "玩家发起参加公会的申请 "+ OrderEnum.APPLY_ATTEND_GUILD.getOrder() +" id=#{玩家想进入的公会id}\r\n"
     *                 + "会长玩家查看公会的入会申请 "+ OrderEnum.SHOW_GUILD_REQUEST.getOrder() +" id=#{公会id}\r\n"
     *                 + "会长玩家同意其他玩家入会申请 "+ OrderEnum.AGREE_ATTEND_REQUEST.getOrder() +" id=#{公会id}&reqId=#{申请号}\r\n"

     *                 + "公会成员退出公会 "+ OrderEnum.EXIT_GUILD.getOrder() +" id=#{公会id}\r\n\n"
     */

    public void createGuild(MsgEntity msgEntity, String[] input) throws Exception{
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            GuildPt.GuildRequestInfo.Builder builder = GuildPt.GuildRequestInfo.newBuilder();
            builder.setName((String)map.get("name"));
            msgEntity.setData(builder.build().toByteArray());
        }
    }



    public void applyAttendGuild(MsgEntity msgEntity, String[] input) throws Exception{
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);
            GuildPt.GuildRequestInfo.Builder builder = GuildPt.GuildRequestInfo.newBuilder();
            builder.setGuildId(Integer.valueOf((String)map.get("id")));
            msgEntity.setData(builder.build().toByteArray());
        }
    }




    public void showGuildReq(MsgEntity msgEntity, String[] input) throws Exception{
        applyAttendGuild(msgEntity, input);
    }



    public void agreeGuildReq(MsgEntity msgEntity, String[] input) throws Exception{
        applyAttendGuild(msgEntity, input);
    }



    public void exitGuild(MsgEntity msgEntity, String[] input) throws Exception{
        applyAttendGuild(msgEntity, input);
    }

    /**
     *+ "会长玩家获取公会物品 "+ OrderEnum.GET_GUILD_ITEM.getOrder() +" id=#{公会id}&itemInfoId=#{公会中仓库的物品种类}&count=#{想获得的数量}\r\n"
     *+ "公会成员捐献物品到公会中 "+ OrderEnum.DONATE_ITEM.getOrder() +" id=#{公会id}&itemId=#{背包中物品id}&count=#{想捐献的数量}\r\n"
     **/
    public void getGuildItem(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);

            GuildPt.GuildRequestInfo.Builder builder = GuildPt.GuildRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setGuildId(Integer.valueOf((String) map.get("id")))
                    .setItemInfoId(Integer.parseInt((String) map.get("itemInfoId")))
                    .setCount(Integer.parseInt((String) map.get("count")))
                    .build();
            msgEntity.setData(builder.build().toByteArray());
        }
    }

    public void donateGuildItem(MsgEntity msgEntity, String[] input)  throws Exception {
        if(input.length >= 2) {
            Map<String, Object> map = splitCmdString(input);

            GuildPt.GuildRequestInfo.Builder builder = GuildPt.GuildRequestInfo.newBuilder();
            builder.setMsgId(UUID.randomUUID().hashCode())
                    .setTime(System.currentTimeMillis())
                    .setGuildId(Integer.valueOf((String) map.get("id")))
                    .setItemId(Long.valueOf((String) map.get("itemId")))
                    .setCount(Integer.valueOf((String) map.get("count")))
                    .build();
            msgEntity.setData(builder.build().toByteArray());
        }
    }

}
