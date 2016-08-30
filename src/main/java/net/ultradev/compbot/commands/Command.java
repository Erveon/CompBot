package net.ultradev.compbot.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.ultradev.compbot.commands.CommandHandler.CommandType;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	
	String name();
	CommandType type();
	
}
