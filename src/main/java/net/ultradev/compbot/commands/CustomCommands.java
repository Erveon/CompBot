package net.ultradev.compbot.commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.ultradev.compbot.commands.CommandHandler.CommandType;

public class CustomCommands extends ListenerAdapter implements CommandListener {
	
	private Map<String, String> customcommands;
	File file;
	
	public CustomCommands() {
		try {
			file = new File(Paths.get(".").toAbsolutePath().normalize().toString() + "\\commands.json");
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		customcommands = new HashMap<>();
		load();
	}
	
	public boolean hasRole(User user, Guild server, String roleName) {
		List<Role> roles = server.getRolesByName(roleName);
		if(roles == null) return false;
		for(Role role : roles) {
			if(server.getRolesForUser(user).contains(role))
				return true;
		}
		return false;
	}
	
	@Command(name = "addcommand", type = CommandType.BOTH)
	public void handleAddCommand(CommandReceivedEvent event) {
		if(!event.getChannel().getName().equals("bot")) {
			return;
		}
		String message = event.getMessage().getContent().replaceFirst(CommandHandler.COMMAND_PREFIX + "addcommand", "").trim();
		message = message.trim();
		if(!message.contains("|")) {
			event.getChannel().sendMessage(event.getUser().getAsMention() + " - usage > " + CommandHandler.COMMAND_PREFIX + "addcommand command name | bot response to command");
		} else {
			String[] messages = message.split("\\|");
			if(messages.length != 2) {
				event.getChannel().sendMessage(event.getUser().getAsMention() + " - usage > " + CommandHandler.COMMAND_PREFIX + "addcommand command name | bot response to command"
						+ "\nKeep in mind that you can only have 1 pipe");
			} else {
				String command = messages[0].trim();
				String output = messages[1].trim();
				if(customcommands.containsKey(command)) {
					event.getChannel().sendMessage(event.getUser().getAsMention() + " - That command already exists. Delete it first before making another. (!deletecommand command)");
				} else {
					customcommands.put(command.toLowerCase(), output);
					save();
					event.getChannel().sendMessage(event.getUser().getAsMention() + " - Successfully registered the command");
				}
			}
		}
	}
	
	@Command(name = "deletecommand", type = CommandType.BOTH)
	public void handleDeleteCommand(CommandReceivedEvent event) {
		if(!event.getChannel().getName().equals("bot")) {
			return;
		}
		String message = event.getMessage().getContent().replaceFirst(CommandHandler.COMMAND_PREFIX + "deletecommand", "").trim().toLowerCase();
		if(customcommands.containsKey(message)) {
			customcommands.remove(message);
			event.getChannel().sendMessage(event.getUser().getAsMention() + " - Successfully removed the command");
			save();
		} else {
			event.getChannel().sendMessage(event.getUser().getAsMention() + " - Command does not exist");
		}
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
		String message = event.getMessage().getContent().trim();
		if(customcommands.containsKey(message.toLowerCase())) {
			event.getChannel().sendMessage(customcommands.get(message));
		}
	}
	
	public void load() {
		JSONObject persistence = new JSONObject();
		try {
			List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
			if(lines != null && lines.size() > 0) {
				persistence = new JSONObject(lines.get(0));
				for(String command : persistence.keySet()) {
					customcommands.put(command, persistence.getString(command));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		JSONObject persistence = new JSONObject();
		for(Entry<String, String> command : customcommands.entrySet())
			persistence.accumulate(command.getKey(), command.getValue());
		try {
			Files.write(Paths.get(file.toURI()), Arrays.asList(persistence.toString()), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
