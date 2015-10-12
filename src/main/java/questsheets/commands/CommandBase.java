package questsheets.commands;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hardcorequesting.quests.Quest;
import hardcorequesting.quests.QuestAdapters;
import hardcorequesting.quests.QuestSet;
import net.minecraft.command.ICommandSender;
import questsheets.QuestSheets;

import java.io.File;
import java.util.List;

public abstract class CommandBase implements ISubCommand
{
    protected static Gson GSON;

    static
    {
        GSON = new GsonBuilder().registerTypeAdapter(QuestSet.class, QuestAdapters.QUEST_SET_ADAPTER).setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES).create();
    }

    @Override
    public int getPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return null;
    }

    @Override
    public boolean isVisible(ICommandSender sender)
    {
        return Quest.isEditing;
    }

    public static File getFile(String name)
    {
        return new File(QuestSheets.configDir, name + ".json");
    }
}
