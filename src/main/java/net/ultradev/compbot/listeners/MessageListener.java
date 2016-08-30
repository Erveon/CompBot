package net.ultradev.compbot.listeners;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.ultradev.compbot.CompBot;
import net.ultradev.compbot.commands.CommandHandler;
import net.ultradev.compbot.commands.CommandHandler.CommandType;

public class MessageListener extends ListenerAdapter {
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
        if (event.isPrivate()) {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getUsername(),
                                    event.getMessage().getContent());
            if(event.getMessage().getContent().startsWith(CommandHandler.COMMAND_PREFIX)) {
            	CompBot.getCommandHandler().handleCommand(event.getMessage(), event.getAuthor(), event.getTextChannel(), event.getGuild(), CommandType.PRIVATE);
            }
        } else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getAuthor().getUsername(),
                        event.getMessage().getContent());
            if(event.getMessage().getContent().startsWith(CommandHandler.COMMAND_PREFIX)) {
            	CompBot.getCommandHandler().handleCommand(event.getMessage(), event.getAuthor(), event.getTextChannel(), event.getGuild(), CommandType.PUBLIC);
            }
            if(event.getTextChannel().getName().equalsIgnoreCase("register")) {
            	event.getMessage().deleteMessage();
            }
        }
    }

}
