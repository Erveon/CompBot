package net.ultradev.compbot.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class CommandHandler {
	
	public static String COMMAND_PREFIX = "!";
	public enum CommandType { PRIVATE, PUBLIC, BOTH }
	
	private List<CommandListener> listeners;
	private Map<String, BotCommand> commands;
	
	public CommandHandler() {
		commands = new HashMap<>();
		listeners = new ArrayList<>();
	}
	
	public void addListener(CommandListener listener) {
		listeners.add(listener);
		for(Method method : listener.getClass().getMethods()) {
			for(Annotation annotation : method.getAnnotations()) {
				if(annotation instanceof Command) {
					Command command = (Command) annotation;
					commands.put(command.name(), new BotCommand(listener, method, command.type()));
				}
			}
		}
	}
	
	public void handleCommand(Message message, User user, TextChannel channel, Guild server, CommandType type) {
		String command = getCommandFromMessage(message);
		if(commands.containsKey(command)) {
			CommandReceivedEvent event = new CommandReceivedEvent(message, user, channel, server);
			BotCommand botCommand = commands.get(getCommandFromMessage(message));
			if(!type.equals(CommandType.BOTH) && !type.equals(botCommand.getType()))
				return;
			try {
				botCommand.getMethod().invoke(botCommand.getListener(), event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasCommand(String command) {
		return commands.containsKey(command);
	}
	
	private String getCommandFromMessage(Message message) {
		String[] params = message.getContent().split(" ");
		return params[0].substring(1, params[0].length());
	}
	

	private class BotCommand {
		
		private CommandListener listener;
		private Method method;
		private CommandType type;
		
		public BotCommand(CommandListener listener, Method method, CommandType type) {
			this.listener = listener;
			this.method = method;
			this.type = type;
		}
		
		public CommandListener getListener() {
			return listener;
		}
		
		public Method getMethod() {
			return method;
		}
		
		public CommandType getType() {
			return type;
		}
		
	}
	
}




