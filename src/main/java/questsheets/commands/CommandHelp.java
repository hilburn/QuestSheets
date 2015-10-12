package questsheets.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp implements ISubCommand
{

    public static final String PREFIX = "\u00A7";//§
    public static final String YELLOW = PREFIX + "e";
    public static final String WHITE = PREFIX + "f";

    @Override
    public int getPermissionLevel()
    {
        return -1;
    }
    
    @Override
    public String getCommandName()
    {

        return "help";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        switch (arguments.length)
        {
            case 0:
                StringBuilder output = new StringBuilder(StatCollector.translateToLocal(LangHelper.HELP_START) + " ");
                List<String> commands = new ArrayList<>();
                for (ISubCommand command : ParentCommand.commands.values())
                {
                    if (command.isVisible(sender)) commands.add(command.getCommandName());
                }

                for (int i = 0; i < commands.size() - 1; i++)
                {
                    output.append("/qs " + YELLOW).append(commands.get(i)).append(WHITE).append(", ");
                }
                output.delete(output.length() - 2, output.length());
                output.append(" and /qs " + YELLOW).append(commands.get(commands.size() - 1)).append(WHITE).append(".");
                sender.addChatMessage(new ChatComponentText(output.toString()));
                break;
            case 1:
                String commandName = arguments[0];
                if (!ParentCommand.commandExists(commandName))
                {
                    throw new CommandNotFoundException(LangHelper.NOT_FOUND);
                }
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(LangHelper.PREFIX + commandName + LangHelper.INFO_SUFFIX)));
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(LangHelper.PREFIX + commandName + LangHelper.SYNTAX_SUFFIX)));
                break;
            default:
                throw new WrongUsageException(LangHelper.PREFIX + getCommandName() + LangHelper.SYNTAX_SUFFIX);
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 2)
        {
            return ParentCommand.instance.addTabCompletionOptions(sender, new String[]{args[1]});
        }
        return null;
    }

    @Override
    public boolean isVisible(ICommandSender sender)
    {
        return true;
    }

}
