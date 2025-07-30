package ru.daniil10295.DAPI.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandBase {
    public String name;
    public String description;
    public String[] aliases;
    public int permissionLevel = 0;

    public CommandArgument[] args;

    public CommandBase(String name, String description, String[] aliases, CommandArgument[] args) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        CommandRegistrationCallback.EVENT.register(this::register);
    }

    public CommandBase(String name, String description, String[] aliases, int permissionLevel) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permissionLevel = permissionLevel;
        CommandRegistrationCallback.EVENT.register(this::register);
    }

    public CommandBase(String name, String description, String[] aliases, CommandArgument[] args, int permissionLevel) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.args = args;
        this.permissionLevel = permissionLevel;
        CommandRegistrationCallback.EVENT.register(this::register);
    }

    public int run(CommandArgument[] args, CommandContext<ServerCommandSource> context) {

        return 0;
    }

    protected void parseArgs(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Get required arguments
        if (args != null) {
            for (CommandArgument arg : args) {
                try {
                    String value = context.getArgument(arg.name, String.class);
                    arg.value = value;
                } catch (IllegalArgumentException e) {
                    if (arg.required) {
                        source.sendError(Text.literal("Required argument '" + arg.name + "' is missing").formatted(Formatting.RED));
                        return;
                    }
                    // Use default value for optional arguments
                    arg.value = arg.defaultValue;
                }
            }
        }
        
        source.sendFeedback(() -> Text.literal("Command executed successfully").formatted(Formatting.GREEN), false);
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        _register(dispatcher, name);
        for (var alias: aliases) {
            if (alias == null || alias.equals(name)) {
                continue;
            }
            _register(dispatcher, alias);
        }
    }

    protected void _register(CommandDispatcher<ServerCommandSource> dispatcher, String name) {
        var command = literal(name)
                .requires(source -> source.hasPermissionLevel(permissionLevel));

        if (args != null && args.length > 0) {
            // Start with the first argument
            var argBuilder = argument(args[0].name, StringArgumentType.string());
            
            // Add remaining arguments
            for (int i = 1; i < args.length; i++) {
                argBuilder = argBuilder.then(
                    argument(args[i].name, StringArgumentType.string())
                );
            }
            
            command = command.then(argBuilder.executes(context -> {
                parseArgs(context);
                return run(args, context);
            }));
        } else {
            command = command.executes(context -> {
                parseArgs(context);
                return run(args, context);
            });
        }

        dispatcher.register(command);
    }

    public void sendToChat(ServerCommandSource source, String message) {
        sendToChat(source, message, Formatting.GREEN);
    }

    public void sendToChat(ServerCommandSource source, String message, Formatting color) {
        source.sendFeedback(() -> Text.literal(message).formatted(color), false);
    }

    public void sendError(ServerCommandSource source, String message) {
        source.sendError(Text.literal(message).formatted(Formatting.RED));
    }
}
