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
                    source.sendFeedback(() -> Text.literal("Argument '" + arg.name + "': " + value).formatted(Formatting.GREEN), false);
                } catch (IllegalArgumentException e) {
                    if (arg.required) {
                        source.sendError(Text.literal("Required argument '" + arg.name + "' is missing").formatted(Formatting.RED));
                        return;
                    }
                    // Use default value for optional arguments
                    arg.value = arg.defualtValue;
                }
            }
        }
        
        source.sendFeedback(() -> Text.literal("Command executed successfully").formatted(Formatting.GREEN), false);
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        var command = literal(name)
            .requires(source -> source.hasPermissionLevel(permissionLevel));

        // Add arguments if any
        if (args != null) {
            var argBuilder = command;
            for (CommandArgument arg : args) {
                if (arg.required) {
                    argBuilder = argBuilder.then(argument(arg.name, StringArgumentType.string()));
                } else {
                    argBuilder = argBuilder.then(
                        argument(arg.name, StringArgumentType.string())
                            .executes(context -> {
                                // Handle optional argument
                                return 1;
                            })
                    );
                }
            }
            command = argBuilder.executes(context -> {
                parseArgs(context);
                return run(args, context);
            });
        } else {
            command = command.executes(context -> {
                parseArgs(context);
                return run(args, context);
            });
        }

        // Add aliases if any
        if (aliases != null) {
            for (String alias : aliases) {
                command = command.then(literal(alias));
            }
        }

        dispatcher.register(command);
    }

    public void sendToChat(ServerCommandSource source, String message) {
        source.sendFeedback(() -> Text.literal(message).formatted(Formatting.GREEN), false);
    }

    public void sendError(ServerCommandSource source, String message) {
        source.sendError(Text.literal(message).formatted(Formatting.RED));
    }
}

