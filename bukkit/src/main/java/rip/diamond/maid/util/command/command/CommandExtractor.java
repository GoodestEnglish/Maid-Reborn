package rip.diamond.maid.util.command.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.exception.CommandRegistrationException;
import rip.diamond.maid.util.command.exception.CommandStructureException;
import rip.diamond.maid.util.command.exception.MissingProviderException;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandExtractor {

	private final DrinkCommandService commandService;

	public CommandExtractor(DrinkCommandService commandService) {
		this.commandService = commandService;
	}

	public Map<String, DrinkCommand> extractCommands(@Nonnull Object handler) throws MissingProviderException, CommandStructureException {
		Preconditions.checkNotNull(handler, "Handler object cannot be null");
		final Map<String, DrinkCommand> commands = new HashMap<>();
		for (Method method : handler.getClass().getDeclaredMethods()) {
			Optional<DrinkCommand> o = extractCommand(handler, method);
			if (o.isPresent()) {
				DrinkCommand drinkCommand = o.get();
				commands.put(commandService.getCommandKey(drinkCommand.getName()), drinkCommand);
			}
		}
		return commands;
	}

	private Optional<DrinkCommand> extractCommand(@Nonnull Object handler, @Nonnull Method method) throws MissingProviderException, CommandStructureException {
		Preconditions.checkNotNull(handler, "Handler object cannot be null");
		Preconditions.checkNotNull(method, "Method cannot be null");
		if (method.isAnnotationPresent(Command.class)) {
			try {
				method.setAccessible(true);
			} catch (SecurityException ex) {
				throw new CommandRegistrationException("Couldn't access method " + method.getName());
			}
			Command command = method.getAnnotation(Command.class);
			String perm = "";
			if (method.isAnnotationPresent(Require.class)) {
				Require require = method.getAnnotation(Require.class);
				perm = require.value();
			} else if (method.getDeclaringClass().isAnnotationPresent(Require.class)) {
				Require require = method.getDeclaringClass().getAnnotation(Require.class);
				perm = require.value() + (command.name().isEmpty() ? "" : ".") + command.name();
			}
			DrinkCommand drinkCommand = new DrinkCommand(
					commandService, command.name(), Sets.newHashSet(command.aliases()), command.desc(), command.usage(), command.async(),
					perm, handler, method
			);
			return Optional.of(drinkCommand);
		}
		return Optional.empty();
	}

}
