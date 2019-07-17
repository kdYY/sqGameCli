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
import java.util.List;
import java.util.Scanner;

@Component
public class ReadService {

    @Value("${token.filePath}")
    private String tokenFileName;

    @OrderMapping(OrderEnum.Login)
    public void loginReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        UserProto.ResponseUserInfo msg = UserProto.ResponseUserInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 404) {
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
        File file = new File(SendOrderService.class.getClassLoader().getResource(tokenFileName).getFile());
        //
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.write(taken);
            fileWriter.flush();
            fileWriter.close();
            System.out.println("写入文件成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入token文件失败");
        } finally {
            try {
                fileWriter.close();
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
                System.out.println("id=" + entityTypes.getId() + ",name=" +entityTypes.getName());
            }
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
            List<UserEntityProto.UserEntity> userEntityList = msg.getUserEntityList();
            List<SenceEntityProto.SenceEntity> senceEntityList = msg.getSenceEntityList();
            System.out.println("welcome to " + sence.getName() + " !");

            printSenceMsg(userEntityList, senceEntityList, msg.getEntityTypeList());
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
            printSenceMsg(msg.getUserEntityList(), msg.getSenceEntityList(), msg.getEntityTypeList());
        } else {
            System.out.println("服务端异常");
        }
    }


    private void printSenceMsg(List<UserEntityProto.UserEntity> userEntityList, List<SenceEntityProto.SenceEntity> senceEntityList, List<EntityTypeProto.EntityType> entityTypeList) {
        System.out.println("player count in sence:" + userEntityList.size());
        for (UserEntityProto.UserEntity userEntity : userEntityList) {
            System.out.println("-name: "
                    + userEntity.getNick()
                    + "("
                    + getTypeName(userEntity.getTypeId(), entityTypeList)
                    + "), state:"
                    + (userEntity.getState()==1?"live":"dead"));
        }
        System.out.println("monster in sence:");
        for (SenceEntityProto.SenceEntity senceEntity : senceEntityList) {
            System.out.println("-id: "
                    + senceEntity.getId()
                    + ", name: "
                    + getTypeName(senceEntity.getTypeId(), entityTypeList)
                    + ", state:"
                    + (senceEntity.getState()==1?"存活":"死亡") + (senceEntity.getTypeId() == 1?getNpcWord(senceEntity):""));
        }
    }

    private String getNpcWord(SenceEntityProto.SenceEntity senceEntity) {
        StringBuilder word = new StringBuilder(", npc语录:");
        for (String s : senceEntity.getNpcWordList()) {
            word.append(s + "|");
        }
        return word.toString();
    }

    private String getTypeName(int typeId, List<EntityTypeProto.EntityType> entityTypeList) {
        for (EntityTypeProto.EntityType entityType : entityTypeList) {
            if(entityType.getId() == typeId) {
                return entityType.getName();
            }
        }
        return "默认";
    }

    @OrderMapping(OrderEnum.Move)
    public void moveReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        aoi(msgEntity);
    }

    @OrderMapping(OrderEnum.TalkWithNpc)
    public void talkNpcReturn(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        SenceEntityProto.ResponseInfo responseInfo = SenceEntityProto.ResponseInfo.parseFrom(msgEntity.getData());
        SenceEntityProto.SenceEntity senceEntity = responseInfo.getSenceEntity(0);
        for (String word : senceEntity.getNpcWordList()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(word);
        }
       // aoi(msgEntity);
    }

    @OrderMapping(OrderEnum.CheckToken)
    public void checkToken(MsgEntity msgEntity) throws InvalidProtocolBufferException {
        UserProto.ResponseUserInfo msg = UserProto.ResponseUserInfo.parseFrom(msgEntity.getData());
        if(msg.getResult() == 404) {
            System.out.println(msg.getContent());
        } else if(msg.getResult() != 500) {
            GameCli.setToken(msg.getToken());
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
