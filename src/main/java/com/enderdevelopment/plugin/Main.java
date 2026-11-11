package com.enderdevelopment.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {
    private NamespacedKey staffKey;
    private final Map<UUID, BukkitTask> auraTasks = new HashMap<>();

    @Override
    public void onEnable() {
        staffKey = new NamespacedKey(this, "magic_staff");
        Bukkit.getPluginManager().registerEvents(this, this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateAura(player);
        }
    }

    @Override
    public void onDisable() {
        auraTasks.values().forEach(BukkitTask::cancel);
        auraTasks.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("magicstaff")) {
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        ItemStack staff = new ItemStack(Material.STICK);
        ItemMeta meta = staff.getItemMeta();
        if (meta == null) {
            player.sendMessage(Component.text("Could not create the magic staff.", NamedTextColor.RED));
            return true;
        }
        meta.displayName(Component.text("Weathered Oak Staff", NamedTextColor.GOLD));
        meta.lore(java.util.List.of(Component.text("Its grain is worn with age.", NamedTextColor.DARK_GRAY)));
        meta.getPersistentDataContainer().set(staffKey, PersistentDataType.BYTE, (byte) 1);
        staff.setItemMeta(meta);
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(staff);
        leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        player.sendMessage(Component.text("A Weathered Oak Staff is yours. Hold it to reveal its quiet aura.", NamedTextColor.GOLD));
        return true;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().runTask(this, () -> updateAura(event.getPlayer()));
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        updateAura(event.getPlayer());
    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        stopAura(event.getPlayer().getUniqueId());
    }

    private void updateAura(Player player) {
        if (isHoldingStaff(player)) {
            if (auraTasks.containsKey(player.getUniqueId())) {
                return;
            }
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, () -> {
                if (!player.isOnline() || !isHoldingStaff(player)) {
                    stopAura(player.getUniqueId());
                    return;
                }
                Location center = player.getLocation().add(0, 1.05, 0);
                player.getWorld().spawnParticle(Particle.END_ROD, center, 1, 0.28, 0.42, 0.28, 0.005);
                player.getWorld().spawnParticle(Particle.ENCHANT, center, 2, 0.32, 0.48, 0.32, 0.0);
            }, 0L, 10L);
            auraTasks.put(player.getUniqueId(), task);
        } else {
            stopAura(player.getUniqueId());
        }
    }

    private boolean isHoldingStaff(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.STICK || !item.hasItemMeta()) {
            return false;
        }
        Byte marker = item.getItemMeta().getPersistentDataContainer().get(staffKey, PersistentDataType.BYTE);
        return marker != null && marker == (byte) 1;
    }

    private void stopAura(UUID playerId) {
        BukkitTask task = auraTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
}
