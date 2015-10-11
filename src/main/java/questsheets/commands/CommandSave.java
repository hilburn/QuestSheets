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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
                try
                {
                    save(sender, set, set.getName());
                }catch (CommandException ignored)
                {
                }
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
                    return;
                } else if (name.length == arguments.length && stringsMatch(name, arguments))
                {
                    save(sender, set, set.getName());
                    return;
                }
            }
            String arg = "";
            for (String subName : arguments)
            {
                arg += subName + " ";
            }
            arg = arg.substring(0, arg.length() - 1);
            throw new CommandException(LangHelper.QUEST_NOT_FOUND, arg);
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        String text = "";
        for (int i = 1; i < args.length; i++)
        {
            text += args[i] + " ";
        }
        text = text.substring(0, text.length()-1);
        Pattern pattern = Pattern.compile("^" + Pattern.quote(text), Pattern.CASE_INSENSITIVE);
        List<String> results = new ArrayList<>();
        for (QuestSet set : Quest.getQuestSets())
        {
            if (pattern.matcher(set.getName()).find()) results.add(set.getName());
        }
        return results;
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
            throw new CommandException(LangHelper.SAVE_FAILED, name);
        }
    }
}
