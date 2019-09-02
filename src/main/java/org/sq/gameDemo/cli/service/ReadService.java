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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    private void printPlayer(PlayerPt.Player player) {
        System.out.println("|id: " + player.getId());
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
        } else {
            System.out.println("服务端异常");
        }
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        } else if(msg.getResult() != 500 && (token != null || token.equals(""))) {
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

    private void printItem(ItemPt.Item item) {
        ItemInfoPt.ItemInfo itemInfo = item.getItemInfo();

        if(itemInfo.getType() == 1) {
            System.out.println("-|id:" + item.getId() + ",穿戴部位:" + EquitmentPart.getPartByCode(itemInfo.getPart().getNumber()) + "| " +
                    "装备名称: " +
                    itemInfo.getName() + "| 等级: " + item.getLevel() + "| 耐力点: " + item.getDurable());
        } else {
            System.out.println("-|id: " + item.getId() + ", name: " + itemInfo.getName() + "  * " + item.getCount());
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
        mailList.stream().filter(mail -> mail.getIsRead()).forEach(mail -> {
            showMailMsg(mail);
        });
        mailList.stream().filter(mail -> !mail.getIsRead()).forEach(mail -> {
            showMailMsg(mail);
        });
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

    private void printItemInfo(ItemInfoPt.ItemInfo itemInfo) {
        System.out.print("-|id:" + itemInfo.getId() + ", name:" + itemInfo.getName());
        if(itemInfo.getType() == 1) {
            System.out.print(", 可穿戴部位: " + EquitmentPart.getPartByCode(itemInfo.getPartValue()) + " , durable: " + itemInfo.getDurable());
        }
        System.out.println(", describe: " + itemInfo.getDescribe());
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
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -");
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

    }

    private String printDate(long startTime) {
        SimpleDateFormat format =  new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
        return format.format(startTime);
    }

    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_CAN_RECEIVE)
    public void getAcceptOnlineTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        getOnlineTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_HISTORY)
    public void getAttentedOnlineTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        getOnlineTrade(msgEntity);
    }

    @OrderMapping(OrderEnum.GET_DEAL)
    public void getDealTrade(MsgEntity msgEntity ) throws InvalidProtocolBufferException {
        TradePt.TradeResponseInfo tradeResponseInfo = TradePt.TradeResponseInfo.parseFrom(msgEntity.getData());
        List<TradePt.Trade> tradeList = tradeResponseInfo.getTradeList();
        System.out.println("交易栏");
        if(tradeList == null || tradeList.size() == 0) {
            System.out.println("(空)");
        }
        for (TradePt.Trade trade : tradeList) {
            printTrade(trade);
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -");
        }
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
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -");
        }
    }

    private void printGuild(GuildPt.Guild guild) {
        System.out.println("公会ID: "+ guild.getId());
        System.out.println("公会名称: "+ guild.getName());
        System.out.println("公会等级: "+ guild.getLevel());
        System.out.println("公会人数: "+ guild.getMemberList().size() + "/" + guild.getMemberSize());


    }

}
