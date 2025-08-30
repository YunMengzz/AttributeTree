package cn.attribute.tang;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("attropen").setExecutor(new MyCommandExecutor());
        this.saveResource("config.yml",false);
        this.getLogger().info(ChatColor.GREEN + "插件已加载！");
    }

    @Override
    public void onDisable() {
        this.getLogger().info(ChatColor.GREEN + "插件已卸载");
    }
}
