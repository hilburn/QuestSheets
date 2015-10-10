package questsheets.commands;

import hardcorequesting.quests.Quest;
import hardcorequesting.quests.QuestSet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import questsheets.QuestSheets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CommandSave extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "save";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        if (arguments.length == 1 && arguments[0].equals("all"))
        {
            for (QuestSet set : Quest.getQuestSets())
            {
                save(sender, set, set.getName());
            }
        } else if (arguments.length > 0)
        {
            for (QuestSet set : Quest.getQuestSets())
            {
                String[] name = set.getName().split(" ");
                if (name.length < arguments.length && stringsMatch(name, arguments))
                {
                    String fileName = "";
                    for (String subName : Arrays.copyOfRange(arguments, name.length, arguments.length))
                    {
                        fileName += subName + " ";
                    }
                    fileName = fileName.substring(0, fileName.length() - 1);
                    save(sender, set, fileName);
                } else if (name.length == arguments.length && stringsMatch(name, arguments))
                {
                    save(sender, set, set.getName());
                }
            }
        }
    }

    private static boolean stringsMatch(String[] sub, String[] search)
    {
        for (int i = 0; i < sub.length; i++)
        {
            if (!sub[i].equalsIgnoreCase(search[i])) return false;
        }
        return true;
    }

    private static void save(ICommandSender sender, QuestSet set, String name)
    {
        try
        {
            File file = getFile(name);
            if (!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            GSON.toJson(set, fileWriter);
            fileWriter.close();
            sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(LangHelper.SAVE_SUCCESS, file.getPath().substring(QuestSheets.configDir.getParentFile().getParent().length()))));
        } catch (IOException e)
        {
            throw new CommandException(LangHelper.SAVE_FAILED);
        }
    }
}
