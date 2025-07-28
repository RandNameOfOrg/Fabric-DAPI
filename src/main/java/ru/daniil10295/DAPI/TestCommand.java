package ru.daniil10295.DAPI;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import ru.daniil10295.DAPI.client.CommandArgument;
import ru.daniil10295.DAPI.client.CommandBase;

public class TestCommand extends CommandBase {
    public TestCommand() {
        super(
            "testcmd",  // Command name
            "A test command with various arguments",  // Description
            new String[]{"t", "testcmd"},  // Aliases
            new CommandArgument[]{
                new CommandArgument("player", "defaultPlayer", true),
                new CommandArgument("message", "defaultMessage", false),
                new CommandArgument("number", "123", false)
            },
            2  // Permission level (op level 2)
        );
    }

    @Override
    public int run(CommandArgument[] args, CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (args != null) {
            for (CommandArgument arg : args) {
                sendToChat(source, "Argument '" + arg.name + "': " + arg.value);
            }
        }

        sendToChat(source, "Command executed successfully");
        return 1;
    }
}