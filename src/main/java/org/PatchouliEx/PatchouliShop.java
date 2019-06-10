package org.PatchouliEx;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class PatchouliShop extends JavaPlugin {
    public static YamlConfiguration pluginConfig;
    public static File shopDataFolder;
    public static Plugin plugin;
    public static PlayerPointsAPI ppapi;
    @Override
    public void onEnable() {
        File config = new File(getDataFolder(),"config.yml");
        shopDataFolder = new File (getDataFolder()+"\\Shop\\");
        plugin = this;
        if(!config.exists()){
            getLogger().info("检测到插件配置文件不存在...");
            getLogger().info("正在创建配置文件 Config.yml !");
            saveDefaultConfig();
            getLogger().info("配置文件 Config.yml 创建成功!");
        }
        pluginConfig = YamlConfiguration.loadConfiguration(config);
        if(!shopDataFolder.exists()){
            getLogger().info("检测到商店储存目录不存在...");
            getLogger().info("正在创建商店储存目录 !");
            shopDataFolder.mkdir();
            getLogger().info("商店储存目录 创建成功!");
        }

        //注册PlayerPoints插件的API
        getLogger().info("开始连接PlayerPoints插件");
        Plugin plugin = getServer().getPluginManager().getPlugin("PlayerPoints");
        PlayerPoints playerPoints = PlayerPoints.class.cast(plugin);
        ppapi = playerPoints.getAPI();
        getLogger().info("PlayerPoints插件连接成功");

        getLogger().info("加载插件事件监听器");
        getServer().getPluginManager().registerEvents(new Core(),this);
        getLogger().info("加载 [插件事件监听器] 成功!");

        getLogger().info("加载插件指令执行器");
        getServer().getPluginCommand("ps").setExecutor(new Commands());
        getLogger().info("加载 [指令执行器] 成功!");

        getLogger().info("插件加载成功!");
        getLogger().info("插件作者: PatchouliEx!");
    }

    @Override
    public void onDisable() {
        getLogger().info("插件卸载成功!");
        getLogger().info("插件作者: PatchouliEx!");
    }
}
