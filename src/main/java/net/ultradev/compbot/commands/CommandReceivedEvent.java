package net.ultradev.compbot.commands;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class CommandReceivedEvent {

	private Message message;
	private User user;
	private TextChannel channel;
	private Guild server;
	
	public CommandReceivedEvent(Message message, User user, TextChannel channel, Guild server) {
		this.message = message;
		this.user = user;
		this.channel = channel;
		this.server = server;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public User getUser() {
		return user;
	}
	
	public TextChannel getChannel() {
		return channel;
	}
	
	public Guild getServer() {
		return server;
	}
	
}
