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
		System.out.println("启动netty中");

		gameCli.init();
		ChannelFuture f = gameCli.getFuture();
		Scanner scanner = new Scanner(System.in);
		String line = "";
		while (scanner.hasNext()) {
			try {
				line = scanner.nextLine();
				gameCli.sendMsg(line);
				// line = "";
			} catch (Exception e) {
				e.printStackTrace();
				//关闭连接
				//f.channel().close().sync();
			}
		}
		System.out.println("客户端结束");
	}
}
