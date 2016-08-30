package net.ultradev.compbot.commands;

import java.util.List;

import net.dv8tion.jda.entities.Role;
import net.ultradev.compbot.commands.CommandHandler.CommandType;

public class UserCommands implements CommandListener {
	
	@Command(name = "info", type = CommandType.BOTH)
	public void handleInfoCommand(CommandReceivedEvent event) {
		event.getChannel().sendMessage("Hi! I'm Competitive Bot, at your service!"
				+ "\nErveon/Gilles has created me to make sure you have a good experience around here."
				+ "\nIf I ever try to take over the world, or have a bug, feel free to message him!");
	}

	@Command(name = "rocketleague", type = CommandType.PUBLIC)
	public void handleRlRankup(CommandReceivedEvent event) {
		addRole(event, "RL Player", "Rocket League Player");
	}
	
	@Command(name = "rainbowsix", type = CommandType.PUBLIC)
	public void handleR6Rankup(CommandReceivedEvent event) {
		addRole(event, "R6 Player", "Rainbow Six Player");
	}
	
	@Command(name = "overwatch", type = CommandType.PUBLIC)
	public void handleOverwatchRankup(CommandReceivedEvent event) {
		addRole(event, "OW Player", "Overwatch Player");
	}
	
	public void addRole(CommandReceivedEvent event, String roleName, String formattedRole) {
		if(event.getChannel().getName().equalsIgnoreCase("register")) {
			List<Role> roles = event.getServer().getRolesByName(roleName);
			for(Role role : roles)
				event.getServer().getManager().addRoleToUser(event.getUser(), role);
			event.getServer().getManager().update();
			event.getUser().getPrivateChannel().sendMessage("You now have the role "+ formattedRole +"."
					+ "\nYou now have access to all the voice channels and text channels that have to do with your selected game."
					+ "\nYou may have all of the roles from register if you would like, just do the commands for the other games.");
		}
	}
	
}
