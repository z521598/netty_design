package com.baidu.netty.client;

import io.netty.channel.ChannelDuplexHandler;

public class TestCodecChannelHandler extends ChannelDuplexHandler {
	private String s;

	public TestCodecChannelHandler(String s) {
		this.s = s;
	}


}
