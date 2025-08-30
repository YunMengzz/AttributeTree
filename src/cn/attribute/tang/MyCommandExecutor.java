package cn.attribute.tang;

import cn.attribute.tang.utils.VexGuiUtils;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MyCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        // reload在前 防止NullPointException
        if (strings.length != 0 && "reload".equalsIgnoreCase(strings[0])) {
            if (sender.isOp()||sender.hasPermission("attrtree.reload")) {
                Player p = null;
                if (sender instanceof Player){
                    p = (Player) sender;
                }
                Main main = JavaPlugin.getPlugin(Main.class);
                main.getLogger().info(ChatColor.GREEN + "AttributeTree加点系统正在重新加载");
                if (p != null) {
                    p.sendMessage(ChatColor.GREEN + "AttributeTree加点系统正在重新加载");
                }
                VexGuiUtils.reload();
                main.getLogger().info(ChatColor.GREEN + "AttributeTree加点系统加载完成");
                if (p != null) {
                    p.sendMessage(ChatColor.GREEN + "AttributeTree加点系统加载完成");
                }
            } else{
                System.out.println("您没有权限使用此命令！");
            }
            return true;
        }
        if (!(sender instanceof Player)) {
            System.out.println(ChatColor.RED + "您不是玩家，不能使用此命令！");
            return true;
        }
        Player p = (Player) sender;

        VexGui gui = VexGuiUtils.getVexGui(p);
        if (gui != null) {
            VexViewAPI.openGui(p,gui);
        }
        return true;
    }
}
