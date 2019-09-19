package org.sq.gameDemo.cli.service;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.cli.GameCli;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.EquitmentPart;
import org.sq.gameDemo.svr.common.OrderMapping;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReadService {

    @Value("${token.filePath}")
    private String tokenFileName;

    @OrderMapping(OrderEnum.Login)
    public void loginReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        UserProto.ResponseUserInfo msg = UserProto.ResponseUserInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 222) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() == 404) {
            GameCli.setToken(msg.getToken());
            //写入文件
            writeToTokenFile(msg.getToken());
            System.out.println(msg.getContent() + "!!");
        } else if(msg.getResult() != 500) {
            GameCli.setToken(msg.getToken());
            //写入文件
            writeToTokenFile(msg.getToken());
            System.out.println("welcome back to ->" + msg.getContent() + "!!");
        } else {
            System.out.println("服务端异常");
        }

    }

    private void writeToTokenFile(String taken) {
        //清空文件，写入文件
        FileOutputStream fsOut = null;
        String filePath = System.getProperty("user.dir")
                + "/conf";
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                file.mkdirs();
            }
            String tokeFilePath = filePath + File.separator + "token.txt";
            File file1 = new File(tokeFilePath);
            if(!file1.exists()) {
                file1.createNewFile();
            }
            fsOut = new FileOutputStream(filePath + File.separator + "token.txt");
            byte[] b = taken.getBytes();
            fsOut.write(b);
            fsOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("写入token文件失败");
        } finally {
            try {
                fsOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //
    @OrderMapping(OrderEnum.GetRole)
    public void getRoleReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {

        EntityTypeProto.EntityTypeResponseInfo msg = EntityTypeProto.EntityTypeResponseInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() != 500) {
            for (int i = 0; i < msg.getEntityTypeCount(); i++) {
                EntityTypeProto.EntityType entityTypes = msg.getEntityType(i);
                System.out.println("id=" + entityTypes.getId() + ",name=" +entityTypes.getName() + ", 类型" + entityTypes.getType().name());
                System.out.println("--拥有的技能:");
                printSkills(entityTypes.getSkillList());
            }
            System.out.println("+----------------------+");
            System.out.println("指令小助手:(使用"+ OrderEnum.BindRole.getOrder() +" id=#{角色id} 进行绑定角色吧!)");
        } else {
            System.out.println("服务端异常");
        }
    }

    @OrderMapping(OrderEnum.ShowPlayer)
    public void showPlayerSkill(MsgEntity msgEntity) throws InvalidProtocolBufferException {

        PlayerPt.PlayerRespInfo msg = PlayerPt.PlayerRespInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() != 500) {
            msg.getPlayerList().forEach(player -> {
                printPlayer(player);
                printSkills(msg.getSkillList());
            });
        } else {
            System.out.println("服务端异常");
        }
    }


    //
    @OrderMapping(OrderEnum.BindRole)
    public void bindRoleReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        SenceMsgProto.SenceMsgResponseInfo msg = SenceMsgProto.SenceMsgResponseInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 500) {
            System.out.println("服务端异常");
        } else if(msg.getResult() == 111) {
            System.out.println(msg.getContent());
        } else {
            System.out.println("bind success！");
            SenceProto.Sence sence = msg.getSence();

            System.out.println("welcome to " + sence.getName() + " !");

            printSenceMsg(msg.getPlayerList(), msg.getNpcList(), msg.getMonsterList(), msg.getEntityTypeList());
        }
    }

    @OrderMapping(OrderEnum.ENTER_NEW_COPY)
    public void enternewcopy(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        aoi(msgEntity);
    }

    @OrderMapping(OrderEnum.ENTER_COPY)
    public void entercopy(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        aoi(msgEntity);
    }
    @OrderMapping(OrderEnum.Aoi)
    public void aoi(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        SenceMsgProto.SenceMsgResponseInfo msg = SenceMsgProto.SenceMsgResponseInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 111 || msg.getResult() == 222) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() != 500) {
            SenceProto.Sence sence = msg.getSence();
            if(sence != null) {
                System.out.println("sence: " + sence.getName() + " .");
            }
            printSenceMsg(msg.getPlayerList(), msg.getNpcList(), msg.getMonsterList(), msg.getEntityTypeList());
            if(msg.getBoss() != null) {
                printMonster(msg.getBoss());
            }
            System.out.println("+-------------------+");
            System.out.println("指令小助手:1. 使用"+ OrderEnum.Aoi.getOrder() +" 可以查看跟你相同场景内的信息哦，包括玩家、怪物、NPC");
            System.out.println("指令小助手:2. 使用"+ OrderEnum.Move.getOrder() +" id=1:起源之地, 2:村子, 3:城堡, 4:森林 可以移动到不同的场景~ " +
                    "(偷偷告诉你，打不过怪物的时候赶紧使用该指令逃之夭夭吧~ ^_^ ~) ");
            System.out.println("指令小助手:3. 使用"+ OrderEnum.TalkWithNpc.getOrder() +" id={场景内NPC的id} 就可以跟场景内的NPC对话, 说不定会有惊喜~");
            System.out.println("指令小助手:4. 使用"+ OrderEnum.SkillAttack.getOrder() +" skillId={选择一个你拥有的技能id(可以使用"+ OrderEnum.ShowPlayer.getOrder()
                    +"查看自身技能)" +
                    "}&targetId=#{怪物id, 玩家id}, eg: skillAttack skillId=7&targetId=6900538778620923909" );
            System.out.println("指令小助手:5. 使用 help 指令可以获取各种指令帮助信息");
        } else {
            System.out.println("服务端异常");
        }
    }





    @OrderMapping(OrderEnum.Move)
    public void moveReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        aoi(msgEntity);
    }

    @OrderMapping(OrderEnum.TalkWithNpc)
    public void talkNpcReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        NpcPt.NpcRespInfo responseInfo = NpcPt.NpcRespInfo.parseFrom(msgEntity.getData());
        NpcPt.Npc npc = responseInfo.getNpc(0);
        Arrays.stream(npc.getNpcWord().split(","))
                .forEach(word -> {
                    System.out.println(word);
        });
       // aoi(msgEntity);
    }

    @OrderMapping(OrderEnum.CheckToken)
    public void checkToken(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        UserProto.ResponseUserInfo msg = UserProto.ResponseUserInfo.parseFrom(msgEntity.getData());
        String token = msg.getToken();
        if(msg.getResult() == 404) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() == 505) {
            GameCli.setToken(token);
            writeToTokenFile(token);
            System.out.println(msg.getContent());
        } else if(msg.getResult() == 200 && (token != null || token.equals(""))) {
            GameCli.setToken(token);
            writeToTokenFile(token);
            System.out.println("login success,welcome back to ->" + msg.getContent() + "!!");
        } else {
            System.out.println("服务端异常");
        }

    }

    @OrderMapping(OrderEnum.BroadCast)
    public void broadCast(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        MessageProto.Msg msg = MessageProto.Msg.parseFrom(msgEntity.getData());
        System.out.println("广播 -> " + msg.getContent());
    }


    @OrderMapping(OrderEnum.SHOW_BAG)
    public void showBag(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        BagPt.BagRespInfo msg = BagPt.BagRespInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 500) {
            System.out.println(msg.getContent());
        } else {
            List<ItemPt.Item> itemList = msg.getBag().getItemList();
            itemList.forEach(item -> {
                printItem(item);
            });
        }
    }



    @OrderMapping(OrderEnum.TIDY_BAG)
    public void tidyBag(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        showBag(msgEntity);
    }

    @OrderMapping(OrderEnum.SHOW_EQUIP)
    public void showEquip(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        byte[] data = msgEntity.getData();
        ItemPt.ItemResponseInfo itemResponseInfo = ItemPt.ItemResponseInfo.parseFrom(data);
        List<ItemPt.Item> itemList = itemResponseInfo.getItemList();
        if(itemList.size() == 0) {
            System.out.println("身上暂时没有装备");
        } else {
            for (ItemPt.Item item : itemList) {
                printItem(item);
            }
        }
    }

    @OrderMapping(OrderEnum.SHOW_STORE)
    public void showStore(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        StorePt.StoreResponseInfo msg = StorePt.StoreResponseInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 500) {
            System.out.println("svr_err");
        } else {
            StorePt.Store store = msg.getStore();
            Optional.ofNullable(store).ifPresent(st -> {
                System.out.println("欢迎来到" + st.getName() + ", 买买买吧！！！！");
                st.getItemInfoList().forEach(itemInfo -> {
                    System.out.println("id: " + itemInfo.getId()
                            + ", name: " + itemInfo.getName()
                            + ", price: 元宝* " + itemInfo.getPrice()
                            + ", description:" + itemInfo.getDescribe()
                    );
                });
            });
        }
    }

    @OrderMapping(OrderEnum.SHOW_ALL_MAIL)
    public void showAllMail(MsgEntity msgEntity) throws InvalidProtocolBufferException {

        MailPt.MailResponseInfo mailResponseInfo = MailPt.MailResponseInfo.parseFrom(msgEntity.getData());
        List<MailPt.Mail> mailList = mailResponseInfo.getMailList();
        mailList.stream().filter(m -> !m.getIsRead()).forEach(mail -> {
            showMailMsg(mail);
        });
        System.out.println("指令小助手:(使用"+ OrderEnum.RECEIVE_MAIL.getOrder() +" id=#{邮件id} 接收邮件吧!)");
        System.out.println("指令小助手:(使用"+ OrderEnum.RECEIVE_ALL_MAIL.getOrder() +" 一键接收所有未读邮件吧!)");
    }

    @OrderMapping(OrderEnum.GET_MAIL)
    public void showMail(MsgEntity msgEntity) throws InvalidProtocolBufferException {

        showAllMail(msgEntity);
    }

    private void showMailMsg(MailPt.Mail mail) {
        System.out.print("邮件: " + " |id:" + mail.getId());
        if(mail.getIsRead()) {
            System.out.println(" -> 已读");
        } else {
            System.out.println("-> 未读");
        }
        System.out.println(" |发送者:" + mail.getSenderName());
        System.out.println(" |title:" + mail.getTitle() + "\r\n |content:" + mail.getContent());
        if(mail.getItemList().size() != 0) {
            System.out.println(" |道具附件:");
            mail.getItemList().forEach(item -> {
                System.out.println("   |name:" + item.getItemInfo().getName() + ", level:" + item.getLevel() + ", count:" + item.getCount
                        ());
            });
        }
        System.out.println("+----------------------+");

    }

    @OrderMapping(OrderEnum.SHOW_ITEMINFO)
    public void showAllItemInfo(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        ItemInfoPt.ItemInfoResponseInfo responseInfo = ItemInfoPt.ItemInfoResponseInfo.parseFrom(msgEntity.getData());
        List<ItemInfoPt.ItemInfo> itemInfoList = responseInfo.getItemInfoList();
        for (ItemInfoPt.ItemInfo itemInfo : itemInfoList) {
            printItemInfo(itemInfo);
        }
    }



    @OrderMapping(OrderEnum.GET_ONLINE_TRADE)
    public void getOnlineTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        TradePt.TradeResponseInfo tradeResponseInfo = TradePt.TradeResponseInfo.parseFrom(msgEntity.getData());
        List<TradePt.Trade> tradeList = tradeResponseInfo.getTradeList();
        System.out.println("面对面交易:");
        if(tradeList == null || tradeList.size() == 0) {
            System.out.println("(空)");
        }
        for (TradePt.Trade trade : tradeList) {
            printTrade(trade);
        }
        printTradeOrder();
    }



    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_CAN_RECEIVE)
    public void getAcceptOnlineTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        getOnlineTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_HISTORY)
    public void getAttentedOnlineTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        getOnlineTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.SHOW_DEAL)
    public void getDealTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        TradePt.TradeResponseInfo tradeResponseInfo = TradePt.TradeResponseInfo.parseFrom(msgEntity.getData());
        List<TradePt.Trade> tradeList = tradeResponseInfo.getTradeList();
        System.out.println("交易栏");
        if(tradeList == null || tradeList.size() == 0) {
            System.out.println("(空)");
        }
        for (TradePt.Trade trade : tradeList) {
            printTrade(trade);
        }
        printTradeOrder();
    }

    private void printTradeOrder() {
        System.out.println("下面是交易栏的指令帮助");
        System.out.println("指令小助手:1. 使用"+ OrderEnum.ACCEPT_DEAL_TRADE.getOrder() +" id=#{交易号}&price=#{玩家给出的竞拍元宝数目} 参与交易栏竞拍吧!");
        System.out.println("指令小助手:2. 使用"+ OrderEnum.GET_DEAL_CAN_BUY.getOrder() +" 查看可参与的交易吧!");
        System.out.println("指令小助手:3. 使用"+ OrderEnum.GET_DEAL_HISTORY.getOrder() +" 查看历史交易记录吧!");
        System.out.println("指令小助手:4. 使用"+ OrderEnum.START_DEAL_TRADE.getOrder()  +
                " auctionItemId=#{背包中交易物品的id}&autionCount=#{要交易的数量}&price=#{竞拍价格}&tradeModel=#{竞拍模式(2为一口价 3为竞拍)} 把物品放入交易栏进行拍卖!");
        System.out.println("+-------------------+");
        System.out.println("如果你只想跟你的好友进行面对面交易，以下指令会帮助到你哦");
        System.out.println("下面是交易栏的指令帮助");
        System.out.println("指令小助手:1. 使用"+ OrderEnum.ACCEPT_ONLINE_TRADE.getOrder() +" id=#{交易号} 跟好友进行面对面交易吧!");
        System.out.println("指令小助手:2. 使用"+ OrderEnum.GET_ONLINE_TRADE_CAN_RECEIVE.getOrder() +" 查看正在进行中的面对面交易");
        System.out.println("指令小助手:3. 使用"+ OrderEnum.GET_ONLINE_TRADE_HISTORY.getOrder() +" 查看历史面对面交易记录吧!");
        System.out.println("指令小助手:4. 使用"+ OrderEnum.START_ONLINE_TRADE.getOrder()  + " accpeterId=#{目标玩家id(可以通过查看好友列表获取)}" +
                "&auctionItemId=#{背包中交易物品的id}" +
                "&autionCount=#{要交易的数量}" +
                "&itemInfoId=#{期望获得的物品种类id(可以通过"+OrderEnum.SHOW_ITEMINFO.getOrder()+" 指令来获取 )}" +
                "&accpetCount=#{期望获得的物品数量}");
    }

    @OrderMapping(OrderEnum.GET_DEAL_CAN_BUY)
    public void getDealTradeCanBuy(MsgEntity msgEntity ) throws InvalidProtocolBufferException {

        getDealTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.GET_DEAL_HISTORY)
    public void getAttentedDealTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        getDealTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.CREATE_GUILD)
    public void createGuild(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        GuildPt.GuildResponseInfo resp = GuildPt.GuildResponseInfo.parseFrom(msgEntity.getData());
        List<GuildPt.Guild> guildList = resp.getGuildList();
        if(guildList == null || guildList.size() == 0) {
            System.out.println("(空)");
        }
        for (GuildPt.Guild guild : guildList) {
            printGuild(guild);
        }
        printGuildOrder();
    }



    @OrderMapping(OrderEnum.SHOW_GUILD_CAN_ATTEND)
    public void showGuildCanAttend(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        createGuild(msgEntity);
    }


    @OrderMapping(OrderEnum.SHOW_GUILD_REQUEST)
    public void showGuildRequest(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        GuildPt.GuildResponseInfo resp = GuildPt.GuildResponseInfo.parseFrom(msgEntity.getData());
        List<GuildPt.AttendGuildReq> attendReqList = resp.getAttendReqList();
        if(attendReqList == null || attendReqList.size() == 0) {
            System.out.println("(空)");
        }
        for (GuildPt.AttendGuildReq attendGuildReq : attendReqList) {
            printAttendGuildReq(attendGuildReq);
        }
        printGuildOrder();
    }


    public void printGuildOrder() {
        System.out.println("+----------------------+");
        System.out.println("指令小助手:1. 使用"+ OrderEnum.AGREE_ATTEND_REQUEST.getOrder() +" id=#{公会id}&reqId=#{申请号}&agree=#{同意为true，不同意为false}" +
                " 处理入会申请");
        System.out.println("指令小助手:2. 使用"+ OrderEnum.CREATE_GUILD.getOrder() +" name=#{公会名称} 创建公会");
        System.out.println("指令小助手:3. 使用"+ OrderEnum.APPLY_ATTEND_GUILD.getOrder() +" id=#{玩家想进入的公会id} 发起入会申请");
        System.out.println("指令小助手:4. 使用"+ OrderEnum.EXIT_GUILD.getOrder() +" id=#{公会id} 退出公会");
        System.out.println("指令小助手:5. 使用"+ OrderEnum.GET_GUILD_ITEM.getOrder() +" id=#{公会id}&itemInfoId=#{公会中仓库的物品种类}&count=#{想获得的数量} " +
                "会长玩家获取公会物品");
        System.out.println("+----------------------+");
        System.out.println("指令小助手:1. 使用"+ OrderEnum.SHOW_CHAIRMAN_GUILD.getOrder() +" 查看自己创建的公会");
        System.out.println("指令小助手:2. 使用"+ OrderEnum.SHOW_GUILDLIST.getOrder() +" 查看加入的公会");
        System.out.println("指令小助手:3. 使用"+ OrderEnum.SHOW_GUILD_CAN_ATTEND.getOrder() +" 查看可以加入的公会");
        System.out.println("指令小助手:4. 使用"+ OrderEnum.SHOW_GUILD_REQUEST.getOrder() +" id=#{公会id} 查看公会的入会申请");
        System.out.println("+----------------------+");
        printGuildBag();
    }

    public void printGuildBag() {
        System.out.println("指令小助手:1. 使用"+ OrderEnum.DONATE_ITEM.getOrder() +" id=#{公会id}&itemId=#{背包中物品id}&count=#{想捐献的数量} 捐献物品，提升职位");
        System.out.println("指令小助手:2. 使用"+ OrderEnum.SHOW_GUILD_BAG.getOrder() +" id=#{公会id} 查看公会仓库");

    }
    @OrderMapping(OrderEnum.SHOW_CHAIRMAN_GUILD)
    public void showChairManGuild(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        createGuild(msgEntity);
    }
    @OrderMapping(OrderEnum.SHOW_GUILDLIST)
    public void showGuildList(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        createGuild(msgEntity);
    }

    @OrderMapping(OrderEnum.SHOW_GUILD_BAG)
    public void showGuildBag(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        GuildPt.GuildResponseInfo resp = GuildPt.GuildResponseInfo.parseFrom(msgEntity.getData());
        List<ItemPt.Item> itemList = resp.getItemList();
        if(itemList.size() == 0) {
            System.out.println("(空)");
        } else {
            itemList.forEach(item -> {
                printItem(item);
            });
        }
        printGuildBag();
    }

    @OrderMapping(OrderEnum.SHOW_TASK_CAN_ACCEPT)
    public void showTaskCanAccept(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        TaskPt.TaskResponseInfo resp = TaskPt.TaskResponseInfo.parseFrom(msgEntity.getData());
        List<TaskPt.Task> taskList = resp.getTaskList();
        if(taskList.size() == 0) {
            System.out.println("(空)");
        } else {
            for (TaskPt.Task task : taskList) {
                printTask(task);
            }
            System.out.println("指令小助手:1. 使用"+ OrderEnum.SHOW_TASK_CAN_ACCEPT.getOrder() +" 查看可接受的任务吧!");
            System.out.println("指令小助手:2. 使用"+ OrderEnum.ACCEPT_TASK.getOrder() +" id=#{任务id} 进行任务吧!");
        }
    }

    @OrderMapping(OrderEnum.SHOW_TASK)
    public void showTask(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        showTaskCanAccept(msgEntity);
    }
    @OrderMapping(OrderEnum.SHOW_TEAM)
    public void showTeam(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        creatTeam(msgEntity);
    }

    @OrderMapping(OrderEnum.CREATE_TEAM)
    public void creatTeam(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        TeamPt.TeamResponseInfo resp = TeamPt.TeamResponseInfo.parseFrom(msgEntity.getData());
        for (TeamPt.Team team : resp.getTeamList()) {
            printTeam(team);
        }
    }

    @OrderMapping(OrderEnum.SHOW_FRIEND)
    public void showFriend(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        FriendPt.FriendResponseInfo resp = FriendPt.FriendResponseInfo.parseFrom(msgEntity.getData());
        for (FriendPt.Friend friend : resp.getFriendList()) {
            printFriend(friend);
        }
    }

    private void printFriend(FriendPt.Friend friend) {
        System.out.println("|unId: " + friend.getUnId());
        System.out.println("|name: " + friend.getName());
        System.out.println("|type: " + friend.getTypeName());
        System.out.println("|state: " + (friend.getIsOnline()?"在线":"离线"));
        System.out.println("+----------------------+");
    }

    private void printTeam(TeamPt.Team team) {
        System.out.println("|id:" + team.getId());
        System.out.println("|name:" + team.getName());
        System.out.println("|member: (" + team.getMemberList().size() + "/" + team.getLimitedSize() + ")");
        System.out.print("|成员如下:");
        for (TeamPt.Member member : team.getMemberList()) {
            System.out.println("-|id:" + member.getId() + ", name=" + member.getName());
        }
    }


    private void printTask(TaskPt.Task task) {
        System.out.println("|id:" + task.getTaskId());
        System.out.println("|name:" + task.getName());
        System.out.println("|describ: " + task.getDescription());
        System.out.print("|require:" + task.getTaskProgress());
        System.out.println("|reward:");
        for (ItemInfoPt.ItemInfo itemInfo : task.getRewardList()) {
            printItemInfo(itemInfo);
        }
        System.out.println("+----------------------+");
    }

    private void printItem(ItemPt.Item item) {
        ItemInfoPt.ItemInfo itemInfo = item.getItemInfo();

        if(itemInfo.getType() == 1) {
            System.out.println("-|id:" + item.getId() + ",穿戴部位:" + EquitmentPart.getPartByCode(itemInfo.getPart().getNumber()) + "| " +
                    "装备名称: " +
                    itemInfo.getName() + "| 等级: " + item.getLevel() + "| 耐力点: " + item.getDurable());
        } else {
            System.out.println("-|id: " + item.getId() + ", itemInfoId: " + itemInfo.getId() + ", name: " + itemInfo.getName() + " * " +
                    item.getCount());
        }

    }
    private void printItemInfo(ItemInfoPt.ItemInfo itemInfo) {
        System.out.print("-|id:" + itemInfo.getId() + ", name:" + itemInfo.getName() +( itemInfo.getCount() > 0? "* " + itemInfo.getCount():""));
        if(itemInfo.getType() == 1) {
            System.out.print(", 可穿戴部位: " + EquitmentPart.getPartByCode(itemInfo.getPartValue()) + " , durable: " + itemInfo.getDurable());
        }
        System.out.println(", describe: " + itemInfo.getDescribe());
    }
    private void printMonster(MonsterPt.Monster monster) {
        System.out.println("--id: "
                + monster.getId()
                + ", name: "
                + monster.getName()
                + ", state:"
                + (monster.getState()==1?"live":monster.getState()==2?"attacking":"dead")
                + ", hp:"
                + monster.getHp()
                + ", mp:"
                + monster.getMp()
                + ", level:"
                + monster.getLevel()
                + ", exp:"
                + monster.getLevel() * 10
                + ", npcWord:"
                + monster.getNpcWord()
        );
    }

    private void printSenceMsg(List<PlayerPt.Player> playerList, List<NpcPt.Npc> npcList, List<MonsterPt.Monster> monsterList, List<EntityTypeProto.EntityType> entityTypeList) {
        System.out.println("player count in sence:" + playerList.size());
        for (PlayerPt.Player player : playerList) {
            System.out.println("---id: "
                    + player.getId()
                    +",name: "
                    + player.getName()
                    + ", state:"
                    + (player.getState()==1?"live":"dead")
                    + ", exp:"
                    + player.getExp()
                    + ", hp:"
                    + player.getHp()
                    + ", mp:"
                    + player.getMp()
            );
        }
        System.out.println("monster in sence:");
        for (MonsterPt.Monster monster : monsterList) {
            printMonster(monster);
        }
        if(npcList == null || npcList.size() == 0) {
            return;
        }
        System.out.println("npc in sence:");
        for (NpcPt.Npc npc : npcList) {
            System.out.println("-id: "
                    + npc.getId()
                    + ", name: "
                    + npc.getName()
            );
        }
    }

    private void printTrade(TradePt.Trade trade) {
        List<ItemPt.Item> itemList = trade.getItemList();
        ItemInfoPt.ItemInfo accpertItemInfo = trade.getAccpertItemInfo();
        System.out.println("|交易号:" + trade.getId());

        System.out.println("|交易时间:" + printDate(trade.getStartTime()));

        for (ItemPt.Item item : itemList) {
            if(item.getItemInfo() != null && item.getItemInfo().getId() == trade.getItemInfoId()) {
                System.out.println("|此次成功交易获取的物品: ");
            } else if(item.getId() == trade.getAuctionItemId()) {
                System.out.println("|此次发起交易的物品");
            } else {
                System.out.println("|参与竞拍有以下的出价:");
            }
            printItem(item);
        }
        System.out.println(trade.getTradeModel() == 1? "|希望的收获品:": ("| 当前竞拍价格最高价:" + trade.getPrice() ));
        if(trade.getTradeModel() == 1) {
            printItemInfo(accpertItemInfo);
            System.out.println("|希望获得的数量: " + trade.getCount()

            );
        }

        System.out.println("|交易结果: " + (trade.getFinish() ? (trade.getSuccess()?"交易成功":"交易失败") : "进行中" ));
        System.out.println("|交易进度:"+(trade.getFinish()?"完成":"进行中"));
        System.out.println("+----------------------+");
    }

    private String printDate(long startTime) {
        SimpleDateFormat format =  new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
        return format.format(startTime);
    }

    private void printGuild(GuildPt.Guild guild) {
        System.out.println("|公会ID: "+ guild.getId());
        System.out.println("|公会名称: "+ guild.getName());
        System.out.println("|公会等级: "+ guild.getLevel());
        System.out.println("|公会人数: "+ guild.getMemberList().size() + "/" + guild.getMemberSize());
        System.out.println("|公会成员:");
        for (GuildPt.Member member : guild.getMemberList()) {
            System.out.println("-| name: " + member.getName() + ", level: " + member.getLevel() + ", position: " + member.getRight());
        }
        System.out.println("|贡献值排行");
        printGuildDonate(guild.getDonateList());
        System.out.println("+----------------------+");
    }

    private void printGuildDonate(List<GuildPt.Donate> donateList) {
        List<GuildPt.Donate> collect = donateList.stream().sorted(Comparator.comparingLong(GuildPt.Donate::getDonateNum).reversed()).collect
                (Collectors.toList());
        int i=1;
        for (GuildPt.Donate donate : collect) {
            System.out.println("-| No." + (i++)  + " name: " + donate.getName() + ", 贡献值: " + donate.getDonateNum());
        }
    }
    private void printAttendGuildReq(GuildPt.AttendGuildReq attendGuildReq) {
        System.out.println("|申请号: " + attendGuildReq.getUnId());
        System.out.println("|申请人:" + attendGuildReq.getName());
        System.out.println("|申请时间:" + printDate(attendGuildReq.getRequestTime()));
        System.out.println("|是否已经同意:" + (attendGuildReq.getAgree()?"已同意":"待确认"));
        System.out.println("+----------------------+");
    }

    private void printPlayer(PlayerPt.Player player) {
        System.out.println("|id: " + player.getId());
        System.out.println("|unId: " + player.getUnId());
        System.out.println("|name: " + player.getName());
        System.out.println("|hp: " + player.getHp() + "/" + player.getBHp());
        System.out.println("|mp: " + player.getMp() + "/" + player.getBMp());
        System.out.println("|level: " + player.getLevel());
        System.out.println("|exp:" + player.getExp());
        System.out.println("|战力:" + player.getAttack());

    }

    private void printSkills(List<SkillPt.Skill> skillList) {
        skillList.stream().forEach(skillPt -> {
            System.out.println("--技能id:"
                    + skillPt.getId()
                    + ", 技能名:"
                    + skillPt.getName()
                    + (skillPt.getHurt() > 0 ? (",技能伤害:"+ skillPt.getHurt()):(skillPt.getHeal() > 0 ?(",技能治疗:"+ skillPt.getHeal()) : ""))
                    + ", 技能cd:"
                    + skillPt.getCd()/1000
                    + "秒, 技能描述"
                    + skillPt.getDescription()
            );

        });
    }
}
