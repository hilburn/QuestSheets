package questsheets.commands;

import com.google.gson.stream.JsonReader;
import hardcorequesting.QuestingData;
import hardcorequesting.commands.CommandHandler;
import hardcorequesting.quests.Quest;
import hardcorequesting.quests.QuestSet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import questsheets.QuestSheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CommandLoad extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "load";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        if (arguments.length == 1 && arguments[0].equals("all"))
        {
            for (File file : getPossibleFiles())
            {
                load(sender, file);
            }
        } else if (arguments.length > 0)
        {
            String file = "";
            for (String arg : arguments)
            {
                file += arg + " ";
            }
            file = file.substring(0, file.length() - 1);
            load(sender, getFile(file));
        }
    }

    private File[] getPossibleFiles()
    {
        return QuestSheets.configDir.listFiles();
    }

    private void load(ICommandSender sender, File file)
    {
        if (!file.exists())
        {
            throw new CommandException(LangHelper.FILE_NOT_FOUND);
        }
        try
        {
            JsonReader reader = new JsonReader(new FileReader(file));
            QuestSet set = GSON.fromJson(reader, QuestSet.class);
            reader.close();
            if (set != null)
            {
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(LangHelper.LOAD_SUCCESS, set.getName())));
            } else
            {
                throw new CommandException(LangHelper.LOAD_FAILED);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new CommandException(LangHelper.LOAD_FAILED);
        }

    }

    @Override
    public boolean isVisible(ICommandSender sender)
    {
        return Quest.isEditing && QuestingData.hasData(sender.getCommandSenderName()) && CommandHandler.isOwnerOrOp(sender);
    }
}
