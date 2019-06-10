package org.PatchouliEx;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class Utils {
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    public static BlockFace getPlayerDirection(Player player) {
        float yaw = Math.abs(player.getLocation().getYaw());
        if (yaw > 315 && yaw < 45) {
            return BlockFace.NORTH;
        } else if (yaw > 45 && yaw < 135) {
            return BlockFace.EAST;
        } else if (yaw > 135 && yaw < 225) {
            return BlockFace.SOUTH;
        } else if (yaw > 225 && yaw < 315) {
            return BlockFace.WEST;
        }
        return BlockFace.NORTH;
    }
    public static void putSignOnChest(Block block, Player player,String line1,String line2,String line3) {
        //要贴上的牌子位于箱子的哪个方向
        Block shopSign = block.getRelative(getPlayerDirection(player));
        //设置牌子形态为墙上的牌子
        shopSign.setType(Material.WALL_SIGN);
        Sign sign = (Sign) shopSign.getState();


        org.bukkit.material.Sign shopSignData = new org.bukkit.material.Sign(Material.WALL_SIGN);
        shopSignData.setFacingDirection(getPlayerDirection(player));
        sign.setData(shopSignData);

        sign.setLine(0, PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"));
        sign.setLine(1, line1);
        sign.setLine(2, line2);
        sign.setLine(3, line3);
        sign.update();
    }
    public static void putSignOnChest(Block block, BlockFace blockFace,String line1,String line2,String line3) {
        //要贴上的牌子位于箱子的哪个方向
        Block shopSign = block.getRelative(blockFace);
        //设置牌子形态为墙上的牌子
        shopSign.setType(Material.WALL_SIGN);
        Sign sign = (Sign) shopSign.getState();


        org.bukkit.material.Sign shopSignData = new org.bukkit.material.Sign(Material.WALL_SIGN);
        shopSignData.setFacingDirection(blockFace);
        sign.setData(shopSignData);

        sign.setLine(0, PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"));
        sign.setLine(1, line1);
        sign.setLine(2, line2);
        sign.setLine(3, line3);
        sign.update();
    }
    public static boolean isContainChestFile(String fileName){
        for(File f : PatchouliShop.shopDataFolder.listFiles()){
            if (f.getName().equalsIgnoreCase(fileName)) {
                return true;
            }
        }
        return false;
    }
    public static File getChestFile(String fileName){
        if (isContainChestFile(fileName)) {
            for(File f : PatchouliShop.shopDataFolder.listFiles()){
                if(f.getName().equalsIgnoreCase(fileName)) {
                    return f;
                }
            }
        }
        return null;
    }
    public static void createNewChestFile(int hashCode){
        try {
            File newChestFile = new File(PatchouliShop.shopDataFolder, hashCode + ".yml");
            if (!newChestFile.exists()) {
                newChestFile.createNewFile();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void setChestFileValue(String name , String path , Object value) {
        File shop = getChestFile(name);
        if (shop != null) {
            try {
                YamlConfiguration chestYaml = YamlConfiguration.loadConfiguration(shop);
                chestYaml.set(path, value);
                chestYaml.save(shop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static Object getChestFileValue(String name , String path){
        File shop = getChestFile(name);
        if(shop != null) {
            YamlConfiguration chestYaml = YamlConfiguration.loadConfiguration(shop);
            return chestYaml.get(path);
        }
        return null;
    }
    public static void counter(int sec, Player p, HashMap<Player, String> map) {
        BukkitTask countTask = new BukkitRunnable() {
            int count = sec;
            @Override
            public void run() {
                count--;
                if (count == 0) {
                    p.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§")+" §c 时间到，商店操作自动取消...");
                    map.remove(p);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(PatchouliShop.plugin, 0, 20);
        tasks.put(p, countTask);
    }
    public static BlockFace getSignDirection(Block block) {

        if (block.getRelative(BlockFace.NORTH) != null) {
            if (block.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(BlockFace.NORTH).getState();
                if(sign.getLine(0).equals(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"))) {
                    return BlockFace.NORTH;
                }
            }
        }
        if (block.getRelative(BlockFace.EAST) != null) {
            if (block.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(BlockFace.EAST).getState();
                if(sign.getLine(0).equals(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"))) {
                    return BlockFace.EAST;
                }

            }
        }
        if (block.getRelative(BlockFace.WEST) != null) {
            if (block.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(BlockFace.WEST).getState();
                if(sign.getLine(0).equals(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"))) {
                    return BlockFace.WEST;
                }

            }
        }
        if (block.getRelative(BlockFace.SOUTH) != null) {
            if (block.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(BlockFace.SOUTH).getState();
                if(sign.getLine(0).equals(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"))) {
                    return BlockFace.SOUTH;
                }

            }
        }
        return null;
    }
    public static boolean isShop(Block block){
        if(block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST){
            if(getSignDirection(block) != null){
                Sign shopSign = (Sign) block.getRelative(getSignDirection(block)).getState();
                if(shopSign.getLine(0).equalsIgnoreCase(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§"))){
                    if(getChestFile(shopSign.getLine(3)+".yml") != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
