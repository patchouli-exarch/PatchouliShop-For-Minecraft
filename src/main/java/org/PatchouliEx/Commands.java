package org.PatchouliEx;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Commands implements CommandExecutor {

    public static HashMap<Player,String> createShopMap = new HashMap<>();
    public static HashMap<Player,String> changeOwnerMap = new HashMap<>();
    public static HashMap<Player,String> changePriceMap = new HashMap<>();
    public static HashMap<Player,String> addHelperMap = new HashMap<>();
    public static HashMap<Player,String> removeHelperMap = new HashMap<>();

    private final String pluginCommand = "ps";
    private final String addHelper     = "addHelper";
    private final String createShop    = "createShop";
    private final String changePrice   = "changePrice";
    private final String changeOwner   = "changeOwner";
    private final String removeHelper  = "removeHelper";
    private final String reload = "reload";

    private final String addHelperPerm = "patchouli.addHelper";
    private final String createshopPerm= "patchouli.createShop";
    private final String changePricePerm = "patchouli.changePrice";
    private final String changeOwnerPerm = "patchouli.changeOwner";
    private final String removeHelperPerm = "patchouli.removeHelper";
    private final String reloadPerm = "patchouli.reload";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (pluginCommand.equalsIgnoreCase(s)){
            if(strings.length != 0) {
                if (commandSender instanceof Player) {
                    Player excutor = (Player)commandSender;

                    //SubCommand 1 功能 >>>>>>>>>> 创建一个新的商店
                    if (createShop.equalsIgnoreCase(strings[0]) || "cs".equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(createshopPerm)) {
                            if (strings.length == 2) {
                                createShopMap.put(excutor,strings[1]);
                                Utils.counter(PatchouliShop.pluginConfig.getInt("waitSec"),excutor,createShopMap);

                                excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.GREEN + " 请放下需要出售物品的箱子!");
                                excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.BLUE + " 请在 " + PatchouliShop.pluginConfig.getInt("waitSec") + " 秒内完成，否则操作自动取消!");
                                return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l 正确用法: §e/PatchouliShop createShop <商店价格>");
                                return true;
                            }
                        }else{
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §4§l 你没有此命令的权限: §b" + createshopPerm);
                            return true;
                        }
                    }

                    //SubCommand 2 功能 >>>>>>>>>> 修改一个商店的价格
                    if (changePrice.equalsIgnoreCase(strings[0]) || "cp".equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(changePricePerm)) {
                            if (strings.length == 2) {
                                    changePriceMap.put(excutor,strings[1]);
                                    Utils.counter(PatchouliShop.pluginConfig.getInt("waitSec"),excutor,changePriceMap);
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.GREEN + " 已经进入改价格模式啦！ 左键点击一下你需要改价格的箱子就能改价格了！");
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.BLUE + " 请在 " + PatchouliShop.pluginConfig.getInt("waitSec") + " 秒内完成，否则操作自动取消!");
                                return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l 正确用法: §e/PatchouliShop changePrice <新的价格> ");
                                return true;
                            }
                        }else{
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §4§l 你没有此命令的权限: §b" + changePricePerm);
                            return true;
                        }
                    }

                    //SubCommand 3 功能 >>>>>>>>>> 修改一个商店的老板
                    if (changeOwner.equalsIgnoreCase(strings[0]) || "co".equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(changeOwnerPerm)) {
                            if (strings.length == 2) {
                                List<String> l = new ArrayList<>();
                                for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                                    l.add(p.getName());
                                }
                                if (Bukkit.getPlayer(strings[1]) != null || Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(strings[1])) || l.contains(strings[1])) {
                                    changeOwnerMap.put(excutor,strings[1]);
                                    Utils.counter(PatchouliShop.pluginConfig.getInt("waitSec"),excutor,changeOwnerMap);

                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.GREEN + " 已经进入转让模式啦！ 左键点击一下你需要转让的箱子就能转让了！");
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.BLUE + " 请在 " + PatchouliShop.pluginConfig.getInt("waitSec") + " 秒内完成，否则操作自动取消!");
                                }else{
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c您输入的玩家不存在，请重新输入!");
                                    return true;
                                }
                                return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l 正确用法: §e/PatchouliShop changeOwner <玩家名称> ");
                                return true;
                            }
                        }else{
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §4§l 你没有此命令的权限: §b" + changeOwnerPerm);
                            return true;
                        }
                    }

                    //SubCommand 4 功能 >>>>>>>>>> 给选中商店添加助手
                    if (addHelper.equalsIgnoreCase(strings[0]) || "ah".equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(addHelperPerm)) {
                            if (strings.length == 2) {
                                List<String> l = new ArrayList<>();
                                for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                                    l.add(p.getName());
                                }
                                if (Bukkit.getPlayer(strings[1]) != null || Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(strings[1])) || l.contains(strings[1])) {
                                    addHelperMap.put(excutor,strings[1]);
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.GREEN + " 左键点击箱子为商店添加助理！");
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.BLUE + " 请在 " + PatchouliShop.pluginConfig.getInt("waitSec") + " 秒内完成，否则操作自动取消!");
                                    Utils.counter(PatchouliShop.pluginConfig.getInt("waitSec"), excutor, addHelperMap);
                                }else{
                                    excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c您输入的玩家不存在，请重新输入!");
                                    return true;
                                }
                                return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l 正确用法: §e/PatchouliShop addHelper <玩家名称> ");
                                return true;
                            }
                        }else{
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §4§l 你没有此命令的权限: §b" + addHelperPerm);
                            return true;
                        }
                    }

                    //SubCommand 5 功能 >>>>>>>>>> 删除指定商店的指定助手
                    if (removeHelper.equalsIgnoreCase(strings[0]) || "rh".equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(removeHelperPerm)) {
                                if (strings.length == 2) {
                                    List<String> l = new ArrayList<>();
                                    for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                                        l.add(p.getName());
                                    }
                                    if (Bukkit.getPlayer(strings[1]) != null || Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(strings[1])) || l.contains(strings[1])) {
                                        removeHelperMap.put(excutor, strings[1]);
                                        excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + ChatColor.GREEN + " 左键点击箱子删除助理！");
                                        excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + ChatColor.BLUE + " 请在 " + PatchouliShop.pluginConfig.getInt("waitSec") + " 秒内完成，否则操作自动取消!");
                                        Utils.counter(PatchouliShop.pluginConfig.getInt("waitSec"), excutor, removeHelperMap);
                                    } else {
                                        excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c您输入的玩家不存在，请重新输入!");
                                        return true;
                                    }
                                    return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c§l 正确用法: §e/PatchouliShop removeHelper <玩家名称> ");
                                return true;
                            }
                        }else {
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §4§l 你没有此命令的权限: §b" + removeHelperPerm);
                            return true;
                        }
                    }

                    //SubCommand 6 功能 >>>>>>>>>> 重载插件配置文件
                    if (reload.equalsIgnoreCase(strings[0])) {
                        if (excutor.hasPermission(reloadPerm)) {
                            if (strings.length == 1) {
                                PatchouliShop.plugin.reloadConfig();
                                excutor.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §a配置文件成功重载!");
                                return true;
                            } else {
                                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c§l 正确用法: §e/PatchouliShop reload ");
                                return true;
                            }
                        }else {
                            commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §4§l 你没有此命令的权限: §b" + reloadPerm);
                            return true;
                        }
                    }
                }
            }else {
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l========== [PatchouliShop] ==========");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §c§l==========     帮助信息      ==========");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §b/ps createShop  <商店价格>  创建一个商店");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §b/ps changePrice <新的价格>  给一个商店更改价格");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §b/ps changeOwner <玩家名称>  给一个商店更改老板");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §b/ps addHelper   <玩家名称>  给一个商店添加助理");
                commandSender.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §b/ps removeHelper <玩家名称> 给一个商店删除助理");
                return true;
            }
        }
        return true;
    }
}
