package net.ultradev.compbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.ultradev.compbot.commands.CommandHandler;
import net.ultradev.compbot.commands.CustomCommands;
import net.ultradev.compbot.commands.UserCommands;
import net.ultradev.compbot.listeners.MessageListener;

public class CompBot {
	
	private static CommandHandler commandHandler;
	
	public static void main(String[] args) {
		new CompBot();
	}
	
	public CompBot() {
		CustomCommands customCommands = new CustomCommands();
		try {
			JDA jda = new JDABuilder().setBotToken("bottokenhere").buildBlocking();
			jda.addEventListener(new MessageListener());
			jda.addEventListener(customCommands);
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			e.printStackTrace();
		}
		
		commandHandler = new CommandHandler();
		getCommandHandler().addListener(new UserCommands());
		getCommandHandler().addListener(customCommands);
	}
	
	public static CommandHandler getCommandHandler() {
		return commandHandler;
	}

}