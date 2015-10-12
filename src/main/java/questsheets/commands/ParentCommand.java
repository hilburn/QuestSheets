package questsheets.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import questsheets.QuestSheets;

import java.util.*;

public class ParentCommand extends CommandBase
{
    public static Map<String, ISubCommand> commands = new LinkedHashMap<String, ISubCommand>();
    public static ParentCommand instance = new ParentCommand();

    static
    {
        register(new CommandHelp());
        register(new CommandSave());
    }

    public static void register(ISubCommand command)
    {
        commands.put(command.getCommandName(), command);
    }

    public static boolean commandExists(String name)
    {
        return commands.containsKey(name);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "qs";
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {

        if (args.length == 1)
        {
            String subCommand = args[0];
            List result = new ArrayList();
            for (ISubCommand command : commands.values())
            {
                if (command.isVisible(sender) && command.getCommandName().startsWith(subCommand))
                    result.add(command.getCommandName());
            }
            return result;
        } else if (commands.containsKey(args[0]) && commands.get(args[0]).isVisible(sender))
        {
            return commands.get(args[0]).addTabCompletionOptions(sender, args);
        }
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            args = new String[]{"help"};
        }
        ISubCommand command = commands.get(args[0]);
        if (command != null)
        {
            if (sender instanceof EntityPlayer)
            {
                QuestSheets.setPlayer((EntityPlayer) sender);
            }
            if (sender.canCommandSenderUseCommand(command.getPermissionLevel(), getCommandName() + " " + command.getCommandName()) ||
                    (sender instanceof EntityPlayerMP && command.getPermissionLevel() <= 0))
            {
                command.handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                return;
            }
            throw new CommandException(LangHelper.NO_PERMISSION);
        }
        throw new CommandNotFoundException(LangHelper.NOT_FOUND);
    }
}
