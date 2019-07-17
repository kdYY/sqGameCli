package org.sq.gameDemo;

import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.sq.gameDemo.cli.GameCli;
import org.sq.gameDemo.svr.common.SpringUtil;
import org.sq.gameDemo.svr.common.dispatch.DispatchRequest;

import java.util.Scanner;

@SpringBootApplication
public class StartApplication implements CommandLineRunner {
	@Value("${token.filePath}")
	private String tokenFileName;
	@Autowired
	private GameCli gameCli;

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

	@Override
	public void run(String... strings) throws InterruptedException {
		DispatchRequest dispatchRequest = (DispatchRequest) SpringUtil.getBean("dispatchRequest");
		try {
			//初始化指令映射
			dispatchRequest.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("启动netty客户端中");

		gameCli.init(tokenFileName);
		GameCli.input();
		System.out.println("客户端结束");
	}
}
