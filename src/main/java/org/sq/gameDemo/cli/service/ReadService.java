package org.sq.gameDemo.cli.service;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.cli.GameCli;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.OrderMapping;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class ReadService {

    @Value("${token.filePath}")
    private String tokenFileName;

    @OrderMapping(OrderEnum.Login)
    public void loginReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        UserProto.ResponseUserInfo msg = UserProto.ResponseUserInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 404 || msg.getResult() == 222) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() != 500) {
            GameCli.setToken(msg.getToken());
            //写入文件
            writeToTokenFile(msg.getToken());
            System.out.println("\r\nlogin success, \r\nwelcome back to ->" + msg.getContent() + "!!");
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
            System.out.println(file.getAbsolutePath() + "创建成功");
            String tokeFilePath = filePath + File.separator + "token.txt";
            File file1 = new File(tokeFilePath);
            if(!file1.exists()) {
                file1.createNewFile();
            }
            fsOut = new FileOutputStream(filePath + File.separator + "token.txt");
            byte[] b = taken.getBytes();
            fsOut.write(b);
            fsOut.close();
            System.out.println(taken+"写入Token成功！");
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

    private void printSkills(List<SkillPt.Skill> skillList) {
        skillList.stream().forEach(skillPt -> {
            System.out.println("--技能id"
                    + skillPt.getId()
                    + ", 技能名"
                    + skillPt.getName()
                    + ", 技能cd"
                    + skillPt.getCd()
                    + ", 技能描述"
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


    @OrderMapping(OrderEnum.Aoi)
    public void aoi(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        SenceMsgProto.SenceMsgResponseInfo msg = SenceMsgProto.SenceMsgResponseInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 111 || msg.getResult() == 222) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() != 500) {
            SenceProto.Sence sence = msg.getSence();
            System.out.println("sence: " + sence.getName() + " .");
            printSenceMsg(msg.getPlayerList(), msg.getNpcList(), msg.getMonsterList(), msg.getEntityTypeList());
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
            );
        }
        System.out.println("monster in sence:");
        for (MonsterPt.Monster monster : monsterList) {
            System.out.println("--id: "
                    + monster.getId()
                    + ", name: "
                    + monster.getName()
                    + ", state:"
                    + (monster.getState()==1?"live":"dead")
            );
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
        if(msg.getResult() == 404) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() != 500) {
            GameCli.setToken(msg.getToken());
            writeToTokenFile(msg.getToken());
            System.out.println("\r\nlogin success, \r\nwelcome back to ->" + msg.getContent() + "!!");
        } else {
            System.out.println("服务端异常");
        }

    }

    @OrderMapping(OrderEnum.BroadCast)
    public void broadCast(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        MessageProto.Msg msg = MessageProto.Msg.parseFrom(msgEntity.getData());
        System.out.println("广播 -> " + msg.getContent());
    }

}
