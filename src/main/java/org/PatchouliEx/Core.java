package org.PatchouliEx;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Core implements Listener {
    private HashMap<Player,Integer> openChestPlayer = new HashMap<>();

    @EventHandler
    public void createShop(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(Commands.createShopMap.containsKey(player)){
            Block placedBlock = event.getBlock();
            if(placedBlock.getType() == Material.CHEST || placedBlock.getType() == Material.TRAPPED_CHEST){
                if (placedBlock.getRelative(Utils.getPlayerDirection(player)).getType() != Material.AIR) {
                    event.setCancelled(true);
                    createFail(player,PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.RED + " 你不能在箱子前面有障碍物的地方创建商店！");
                    return;
                }
                if (placedBlock.getRelative(BlockFace.NORTH).getType() == Material.CHEST ) {
                    event.setCancelled(true);
                    createFail(player, PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.RED + " 你不能在箱子旁边创建商店！");
                    return;
                }
                if (placedBlock.getRelative(BlockFace.EAST).getType() == Material.CHEST ) {
                    event.setCancelled(true);
                    createFail(player,PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.RED + " 你不能在箱子旁边创建商店！");
                    return;
                }
                if (placedBlock.getRelative(BlockFace.WEST).getType() == Material.CHEST) {
                    event.setCancelled(true);
                    createFail(player,PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.RED + " 你不能在箱子旁边创建商店！");
                    return;
                }
                if (placedBlock.getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
                    event.setCancelled(true);
                    createFail(player,PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + ChatColor.RED + " 你不能在箱子旁边创建商店！");
                    return;
                }

                for(ItemStack item : player.getInventory()) {
                    if (item != null) {
                        if (item.getType() == Material.SIGN) {
                            int chestHashCode = placedBlock.hashCode();
                            item.setAmount(item.getAmount()-1);
                            //贴上牌子 并且 创建箱子文件
                            List<String> helperList = new ArrayList<>();
                            int shopPrice = Integer.valueOf(Commands.createShopMap.get(player));

                            String line1 = PatchouliShop.pluginConfig.getString("SignLine1").replace("&","§") + player.getName();
                            String line2 = PatchouliShop.pluginConfig.getString("SignLine2").replace("&","§") + shopPrice +" §bP点";

                            Utils.putSignOnChest(placedBlock,player,line1,line2,String.valueOf(placedBlock.hashCode()));
                            Utils.createNewChestFile(chestHashCode);
                            Utils.setChestFileValue(chestHashCode + ".yml", ".Owner",event.getPlayer().getName());
                            Utils.setChestFileValue(chestHashCode + ".yml", ".Price",shopPrice);
                            Utils.setChestFileValue(chestHashCode + ".yml", ".Location",placedBlock.getLocation().serialize());
                            Utils.setChestFileValue(chestHashCode + ".yml", ".Helper",helperList);
                            Commands.createShopMap.remove(player);
                            Utils.tasks.get(player).cancel();
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §a§l 商店建立成功!");
                            return;
                        }
                    }
                }
                player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&","§") + " §4 你的物品栏至少要有一块牌子来建立商店!");
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void removeShop(BlockBreakEvent event){
        Block breakedBlock = event.getBlock();
        Player player = event.getPlayer();
        if (Utils.isShop(breakedBlock)) {
            if (Utils.getSignDirection(breakedBlock) != null) {
                Sign shopSign = (Sign) breakedBlock.getRelative(Utils.getSignDirection(breakedBlock)).getState();
                if (shopSign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName()) || event.getPlayer().isOp()) {
                    if (Utils.getChestFile(breakedBlock.hashCode() + ".yml") != null) {
                        Utils.getChestFile(breakedBlock.hashCode() + ".yml").delete();
                        player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a商店已经移除!");
                    }
                    return;
                } else {
                    event.setCancelled(true);
                    player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                    return;
                }
            }
        }
        if(breakedBlock.getType() == Material.WALL_SIGN) {
            Sign shopSign = (Sign) event.getBlock().getState();
            if (shopSign.getLine(0) != null) {
                if (shopSign.getLine(0).equals(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§"))) {
                    if (shopSign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName())) {
                        if(Utils.getChestFile(shopSign.getLine(3)+ ".yml") != null) {
                            Utils.getChestFile(shopSign.getLine(3) + ".yml").delete();
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a商店已经移除!");
                        }

                        return;
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                    }
                }
            }
        }
    }
    @EventHandler
    public void changePrice(PlayerInteractEvent event) {
        if (Commands.changePriceMap.containsKey(event.getPlayer())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickBlock = event.getClickedBlock();
                Player player = event.getPlayer();
                if (Utils.isShop(clickBlock)) {
                    if (Utils.getSignDirection(clickBlock) != null) {
                        Sign sign = (Sign) event.getClickedBlock().getRelative(Utils.getSignDirection(clickBlock)).getState();
                        if (sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName())) {
                            String bossAfter = sign.getLine(1);
                            BlockFace blockFace = Utils.getSignDirection(clickBlock);
                            Utils.setChestFileValue(clickBlock.hashCode()+".yml","Price",Integer.valueOf(Commands.changePriceMap.get(player)));
                            Utils.putSignOnChest(clickBlock, blockFace, bossAfter, PatchouliShop.pluginConfig.getString("SignLine2").replace("&","§")+Commands.changePriceMap.get(player) + " §bP点", String.valueOf(clickBlock.hashCode()));

                            Commands.changePriceMap.remove(player);
                            Utils.tasks.get(player).cancel();
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a商店改价成功!");
                        } else {
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                            Commands.changeOwnerMap.remove(player);
                            Utils.tasks.get(player).cancel();
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void changeOwner(PlayerInteractEvent event) {
        if (Commands.changeOwnerMap.containsKey(event.getPlayer())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickBlock = event.getClickedBlock();
                Player player = event.getPlayer();

                if (Utils.isShop(clickBlock)) {
                    if (Utils.getSignDirection(clickBlock) != null) {
                        Sign sign = (Sign) event.getClickedBlock().getRelative(Utils.getSignDirection(clickBlock)).getState();
                        if (sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName())) {
                            String price = sign.getLine(2);
                            String line1 = PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§") + Commands.changeOwnerMap.get(player);
                            BlockFace blockFaceAfter = Utils.getSignDirection(clickBlock);
                            clickBlock.getRelative(blockFaceAfter).setType(Material.AIR);
                            Utils.putSignOnChest(clickBlock, blockFaceAfter, line1, price, String.valueOf(clickBlock.hashCode()));
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a商店转让成功!");
                            Commands.changeOwnerMap.remove(player);
                            Utils.tasks.get(player).cancel();
                        } else {
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                            Commands.changeOwnerMap.remove(player);
                            Utils.tasks.get(player).cancel();
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void addHelper(PlayerInteractEvent event) {
        if (Commands.addHelperMap.containsKey(event.getPlayer())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickBlock = event.getClickedBlock();
                Player player = event.getPlayer();
                if (Utils.isShop(clickBlock)) {
                    if (Utils.getSignDirection(clickBlock) != null) {
                        Sign sign = (Sign) event.getClickedBlock().getRelative(Utils.getSignDirection(clickBlock)).getState();
                        if (sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName())) {
                            List helperList = (List<String>) Utils.getChestFileValue(clickBlock.hashCode() + ".yml", "Helper");

                            if (helperList.contains(Commands.addHelperMap.get(player))) {
                                Commands.addHelperMap.remove(player);
                                Utils.tasks.get(player).cancel();
                                player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c助理已存在，请检查1");
                                return;
                            }
                            helperList.add(Commands.addHelperMap.get(player));
                            Utils.setChestFileValue(clickBlock.hashCode() + ".yml", "Helper", helperList);
                            Commands.addHelperMap.remove(player);
                            Utils.tasks.get(player).cancel();
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a助理添加成功!");
                        } else {
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                            Commands.changeOwnerMap.remove(player);
                            Utils.tasks.get(player).cancel();
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void removeHelper(PlayerInteractEvent event){
        if (Commands.removeHelperMap.containsKey(event.getPlayer())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickBlock = event.getClickedBlock();
                Player player = event.getPlayer();
                if (Utils.isShop(clickBlock)) {
                    if (Utils.getSignDirection(clickBlock) != null) {
                        Sign sign = (Sign) event.getClickedBlock().getRelative(Utils.getSignDirection(clickBlock)).getState();
                        if (sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName())) {
                            List helperList = (List<String>) Utils.getChestFileValue(clickBlock.hashCode() + ".yml", "Helper");

                            if (!helperList.contains(Commands.removeHelperMap.get(player))) {
                                Commands.removeHelperMap.remove(player);
                                Utils.tasks.get(player).cancel();
                                player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c助理不存在，请检查1");
                                return;
                            }
                            helperList.remove(Commands.removeHelperMap.get(player));
                            Utils.setChestFileValue(clickBlock.hashCode() + ".yml", "Helper", helperList);
                            Commands.removeHelperMap.remove(player);
                            Utils.tasks.get(player).cancel();
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §a助理删除成功!");
                        } else {
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                            Commands.changeOwnerMap.remove(player);
                            Utils.tasks.get(player).cancel();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void shopping(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();

        if (openChestPlayer.containsKey(player)) {
            if(Utils.getChestFile(openChestPlayer.get(player)+".yml")!=null) {
                YamlConfiguration chestFile = YamlConfiguration.loadConfiguration(Utils.getChestFile(openChestPlayer.get(player) + ".yml"));
                World world = Bukkit.getWorld(chestFile.getString("Location.world"));
                double x = chestFile.getDouble("Location.x");
                double y = chestFile.getDouble("Location.y");
                double z = chestFile.getDouble("Location.z");
                Location chestLocation = new Location(world, x, y, z, 0, 0);
                Block chestShop = chestLocation.getBlock();

                if (Utils.getSignDirection(chestShop) != null) {
                    Sign sign = (Sign) chestShop.getRelative(Utils.getSignDirection(chestShop)).getState();
                    Player owner = Bukkit.getPlayer(sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), ""));
                    List<String> helperList = chestFile.getStringList("Helper");
                    if (!(sign.getLine(1).replace(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§"), "").equals(player.getName()) || helperList.contains(player.getName()))) {
                        if (event.getClick() == ClickType.SHIFT_LEFT && event.getClickedInventory().getType() == InventoryType.CHEST) {
                            int price = Integer.valueOf(sign.getLine(2).replace(PatchouliShop.pluginConfig.getString("SignLine2").replace("&","§"),"").replace(" §bP点",""));
                            if (PatchouliShop.ppapi.look(player.getUniqueId()) >= Integer.valueOf(price)) {

                                PatchouliShop.ppapi.pay(player.getUniqueId(), owner.getUniqueId(), Integer.valueOf(price));
                                int solt = event.getSlot();
                                if(event.getClickedInventory().getItem(solt) != null) {
                                    if (event.getClickedInventory().getItem(solt).getType() != Material.AIR) {
                                        ItemStack item = event.getCurrentItem();
                                        event.getClickedInventory().setItem(solt, new ItemStack(Material.AIR));
                                        event.getWhoClicked().getInventory().addItem(item);
                                        owner.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " 花费 " + ChatColor.BLUE + price + ChatColor.GREEN + " P点购买了你的一件商品!");
                                        player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + ChatColor.GREEN + " 你花费 " + ChatColor.BLUE + price + ChatColor.GREEN + " P点购买了一件商品!");
                                    }
                                }
                            } else {
                                player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + ChatColor.RED + " 你的P点不足，该商品需要 " + ChatColor.GREEN + price + ChatColor.RED + " P点!");
                                event.setCancelled(true);
                            }
                        } else if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                            event.setCancelled(true);
                            player.sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c商店不是你的,你不能对它进行操作!");
                        } else if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                            event.getWhoClicked().sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c请按下Shift键然后鼠标点击物品进行购买操作!");
                            event.setCancelled(true);
                        }else if (event.getClick() == ClickType.SHIFT_RIGHT){
                            event.getWhoClicked().sendMessage(PatchouliShop.pluginConfig.getString("Prefix").replace("&", "§") + " §c请按下Shift键然后鼠标点击物品进行购买操作!");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void inventoryOpen(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            int blockHashCode = clickedBlock.hashCode();

            if(Utils.isShop(clickedBlock)){
                openChestPlayer.put(player,blockHashCode);
            }

        }
    }
    @EventHandler
    public void inventoryClose(InventoryCloseEvent event){
        if (openChestPlayer.containsKey(event.getPlayer())) {
            openChestPlayer.remove(event.getPlayer());
        }
    }
    @EventHandler
    public void playerPlaceShopEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.CHEST || event.getBlock().getType() == Material.TRAPPED_CHEST) {

            Block chestBlock = event.getBlock();
            if (Utils.isShop(chestBlock.getRelative(BlockFace.NORTH))) {
                event.setCancelled(true);
                player.sendMessage(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§") + ChatColor.RED + " 你不能在商店箱子旁边放箱子！");
                return;
            }
            if (Utils.isShop(chestBlock.getRelative(BlockFace.EAST))) {
                event.setCancelled(true);
                player.sendMessage(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§") + ChatColor.RED + " 你不能在商店箱子旁边放箱子！");
                return;
            }
            if (Utils.isShop(chestBlock.getRelative(BlockFace.WEST))) {
                event.setCancelled(true);
                player.sendMessage(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§") + ChatColor.RED + " 你不能在商店箱子旁边放箱子！");
                return;
            }
            if (Utils.isShop(chestBlock.getRelative(BlockFace.SOUTH))) {
                event.setCancelled(true);
                player.sendMessage(PatchouliShop.pluginConfig.getString("SignLine1").replace("&", "§") + ChatColor.RED + " 你不能在商店箱子旁边放箱子！");
                return;
            }
        }
    }

    private void createFail(Player player,String message){
        player.sendMessage(message);
        player.getInventory().addItem(new ItemStack(Material.SIGN));
        Commands.createShopMap.remove(player);
        Utils.tasks.get(player).cancel();
    }
}
