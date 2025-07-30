package ru.daniil10295.DAPI;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
            0  // Permission level
        );
    }

    @Override
    public int run(CommandArgument[] args, CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        // Get arguments
        String player = "";
        String message = "";
        String number = "";

        for (CommandArgument arg : args) {
            switch (arg.name) {
                case "player":
                    player = arg.value;
                    break;
                case "message":
                    message = arg.value;
                    break;
                case "number":
                    number = arg.value;
                    break;
            }
        }

        // Send feedback
        String finalPlayer = player;
        String finalMessage = message;
        String finalNumber = number;
        source.sendFeedback(() -> Text.literal(
            "Player: " + finalPlayer + ", Message: " + finalMessage + ", Number: " + finalNumber
        ).formatted(Formatting.GREEN), false);

        return 1;
    }
}