package cn.attribute.tang.utils;

import cn.attribute.tang.Main;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.manager.AttributeManager;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexText;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VexGuiUtils {

    // 创建VexGui的基础变量 在静态代码块中赋值节约资源
    private static Main plugin;
    private static String pUrl;
    private static int pX;
    private static int pY;
    private static int pWidth;
    private static int pHeight;
    private static List<VexComponents> lvc;
    private static int attrX;
    private static int attrY;
    private static final List<String> buttonNames;

    static{
        // 存储好按钮名字  节约资源
        buttonNames = new ArrayList<>();
        buttonNames.add("goldFox");
        buttonNames.add("star");
        buttonNames.add("starLight");
        buttonNames.add("sunLight");
        buttonNames.add("dragonSpirit");

        try {
            plugin = JavaPlugin.getPlugin(Main.class);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/config.yml"));
            pUrl = config.getString("picture.url");
            pX = config.getInt("picture.x");
            pY = config.getInt("picture.y");
            pWidth = config.getInt("picture.width");
            pHeight = config.getInt("picture.height");
            attrX = config.getInt("attributePoints.X");
            attrY = config.getInt("attributePoints.Y");
            lvc = getButtons(config);
            // 判断是否非空
            if (plugin == null || pUrl == null){
                throw new Exception();
            }
        } catch (Exception e){
            JavaPlugin.getPlugin(Main.class).getLogger().info(ChatColor.RED + "AttrTree加载失败，加载时发现问题！");
            e.printStackTrace();
        }

    }

    public static VexGui getVexGui(OfflinePlayer p){
        // 创建属性点对象 加到lvc中
        if (lvc == null){
            JavaPlugin.getPlugin(Main.class).getLogger().info(ChatColor.RED + "按钮组件发生问题！");
            if (p instanceof Player) {
                Player player = (Player) p;
                player.sendMessage(ChatColor.RED + "加点系统配置发生问题，请联系管理员！");
                return null;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        PlayerData playerData = SkillAPI.getPlayerData(p);
        int attributePoints = playerData.getAttributePoints();
        list.add(attributePoints+"");
        VexText vexText = new VexText(attrX, attrY, list);
        List<VexComponents> vcs = lvc;
        vcs.add(vexText);
        // 创建gui对象，并返回
        return new VexGui(pUrl,pX,pY,pWidth,pHeight,vcs);
    }

    /**
     * 获取按钮方法
     * # 金狐（加大量血量和物理防御）（增加当前0.3的血量，0.2的防御），
     * # 星辰（技能强度和技能抗性）（当前0.2的技能强度，和0.2的技能抗性），
     * # 星辉（冷缩+魔力恢复）（减少百分之1的冷缩，0.2的魔力恢复），
     * # 日曜（加少量血量和物理攻击）（0.1的血量和0.2的攻击力），
     * # 龙魂（魔力值+少量防御）（增加20+0.1当前魔力值的蓝，增加0.1的防御）
     * # COOLDOWN
     * # HEALTH
     * # MANA
     * # MANA_REGEN
     * # MOVE_SPEED
     * # PHYSICAL_DAMAGE
     * # PHYSICAL_DEFENSE
     * # SKILL_DAMAGE
     * # SKILL_DEFENSE
     * @param config
     * @return
     */
    private static List<VexComponents> getButtons(FileConfiguration config) throws Exception {
        /*String pointsX = config.getString("attributePoints.x");
        String pointsY = config.getString("attributePoints.y");*/
        // 获取通用设置
        String url = config.getString("buttonSettings.currency.url");
        String url2 = config.getString("buttonSettings.currency.url2");

        if (url == null || url2 == null) {
            throw new Exception("按钮贴图配置未找到！");
        }

        List<VexComponents> lvc = new ArrayList<>();
        int i = 0;
        // 简化按钮初始化 for循环遍历
        // 节省栈内存 效率高效
        int btnX;
        int btnY;
        int btnW;
        int btnH;
        String key;
        for (String buttonName : buttonNames) {
            i++;
            key = "buttonSettings." + buttonName + ".";
            btnX = Integer.parseInt(config.getString(key + "x"));
            btnY = Integer.parseInt(config.getString(key + "y"));
            btnW = Integer.parseInt(config.getString(key + "w"));
            btnH = Integer.parseInt(config.getString(key + "h"));

            /*
            金狐（加大量血量和物理防御）（增加当前0.3的血量，0.2的防御），
            星辰（技能强度和技能抗性）（当前0.2的技能强度，和0.2的技能抗性），
            星辉（冷缩+魔力恢复）（增加百分之1的冷缩，0.2的魔力恢复），
            日曜（加少量血量和物理攻击）（0.1的血量和0.2的攻击力），
            龙魂（魔力值+少量防御）（增加0.3当前魔力值的蓝，增加0.1的防御）
             */
            if (buttonName.equals("goldFox")) {
                lvc.add(new VexButton(i,"      ",url,url2,btnX,btnY,btnW,btnH,player -> {
                    // 金狐service Code
                    int[] attrs = giveAttribute("烈魂", "战斗本能", player);
                    if (attrs.length == 0) {
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "加点成功！ 当前最大血量: " + attrs[0] + "，当前防御: " + attrs[1] + "   消耗属性点：2");

                }));
            } else if (buttonName.equals("star")) {
                lvc.add(new VexButton(i,"      ",url,url2,btnX,btnY,btnW,btnH,player -> {
                    // 星辰service Code
                    // 星辰（技能强度和技能抗性）（当前0.2的技能强度，和0.2的技能抗性），
                    int[] attrs = giveAttribute("物语", "海语", player);
                    if (attrs.length == 0) {
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "加点成功！ 当前技能强度: " + attrs[0] + "，当前技能抗性: " + attrs[1] + "   消耗属性点：2");

                }));
            } else if (buttonName.equals("starLight")) {
                lvc.add(new VexButton(i,"      ",url,url2,btnX,btnY,btnW,btnH,player -> {
                    // 星辉service Code
                    // 星辉（冷缩+魔力恢复）（增加百分之1的冷缩，0.2的魔力恢复），
                    int[] attrs = giveAttribute("物语","能源虹吸", player);
                    if (attrs.length == 0) {
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "加点成功！ 当前冷却缩减: " + attrs[0] + "，当前魔力回复: " + attrs[1] + "   消耗属性点：2");
                }));
            } else if (buttonName.equals("sunLight")) {
                lvc.add(new VexButton(i,"      ",url,url2,btnX,btnY,btnW,btnH,player -> {
                    // 日曜service Code
                    // 日曜（加少量血量和物理攻击）（0.1的血量和0.2的攻击力），
                    int[] attrs = giveAttribute("烈魂","战斗本能", player);
                    if (attrs.length == 0) {
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "加点成功！ 当前最大血量: " + attrs[0] + "，当前物理攻击: " + attrs[1] + "   消耗属性点：2");
                }));
            } else {
                lvc.add(new VexButton(i,"      ",url,url2,btnX,btnY,btnW,btnH,player -> {
                    // 龙魂service Code
                    // 龙魂（魔力值+少量防御）（增加0.3当前魔力值的蓝，增加0.1的防御）
                    int[] attrs = giveAttribute("烈魂", "战斗本能", player);
                    if (attrs.length == 0) {
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "加点成功！ 当前血量: " + attrs[0] + "，当前魔力回复: " + attrs[1] + "   消耗属性点：2");
                }));
            }
        }

        return lvc;

    }

    /**
     * 加点方法
     * @param attr1
     * @param attr2
     * @return
     */
    public static int[] giveAttribute(String attr1, String attr2, Player player){
        PlayerData playerData = SkillAPI.getPlayerData(player);
        if (playerData.getAttributePoints() < 2) {
            // 关闭界面
            player.closeInventory();
            // 提示信息
            player.sendMessage(ChatColor.RED + "您所拥有的属性点不足！");
            return null;
        }

        int var1 = playerData.getAttribute(attr1);
        int var2 = playerData.getAttribute(attr2);

        var1++;
        var2++;
        /*
        int var1 = 1 + var1_;
        int var2 = 1 + var2_;
        */
        // 加点
        playerData.giveAttribute(attr1,var1);
        playerData.giveAttribute(attr2,var2);
        playerData.setAttribPoints(playerData.getAttributePoints() - 2);
        player.closeInventory();
        return new int[]{var1,var2};
    }

    public static void reload(){
        // 重新赋值
        // 赋值
        try {
            plugin = JavaPlugin.getPlugin(Main.class);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/config.yml"));
            pUrl = config.getString("picture.url");
            pX = config.getInt("picture.x");
            pY = config.getInt("picture.y");
            pWidth = config.getInt("picture.width");
            pHeight = config.getInt("picture.height");
            attrX = config.getInt("attributePoints.X");
            attrY = config.getInt("attributePoints.Y");
            lvc = getButtons(config);
            if (plugin == null || pUrl == null){
                throw new Exception();
            }
        } catch (Exception e){
            JavaPlugin.getPlugin(Main.class).getLogger().info(ChatColor.RED + "AttrTree加载失败，加载时发现问题！");
            e.printStackTrace();
        }
    }
}
