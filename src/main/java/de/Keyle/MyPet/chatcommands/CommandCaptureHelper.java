package de.Keyle.MyPet.chatcommands;

import de.Keyle.MyPet.util.Colorizer;
import de.Keyle.MyPet.util.MyPetPermissions;
import de.Keyle.MyPet.util.MyPetPlayer;
import de.Keyle.MyPet.util.locale.MyPetLocales;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCaptureHelper implements CommandExecutor, TabCompleter
{
    private static List<String> emptyList = new ArrayList<String>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            MyPetPlayer myPetPlayer = MyPetPlayer.getMyPetPlayer(player);

            if (MyPetPermissions.has(player, "MyPet.user.capturehelper"))
            {
                myPetPlayer.setCaptureHelperActive(!myPetPlayer.isCaptureHelperActive());
                String mode = myPetPlayer.isCaptureHelperActive() ? MyPetLocales.getString("Name.Enabled", player) : MyPetLocales.getString("Name.Disabled", player);
                player.sendMessage(Colorizer.setColors(MyPetLocales.getString("Message.CaptureHelperMode", player)).replace("%mode%", "" + mode));
                return true;
            }
            player.sendMessage(Colorizer.setColors(MyPetLocales.getString("Message.NotAllowed", player)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings)
    {
        return emptyList;
    }
}