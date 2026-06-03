package fr.orinstone.orinstonechat;

import fr.orinstone.orinstonechat.commands.OrinstoneChatCommand;
import fr.orinstone.orinstonechat.listeners.ChatListener;
import org.bukkit.plugin.java.JavaPlugin;

public class OrinstoneChat extends JavaPlugin {

    private static OrinstoneChat instance;

    @Override
    public void onEnable() {
        instance = this;

        // Sauvegarde la config par défaut si elle n'existe pas
        saveDefaultConfig();

        // Enregistre les listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        // Enregistre les commandes
        OrinstoneChatCommand cmd = new OrinstoneChatCommand(this);
        getCommand("orinstonechat").setExecutor(cmd);
        getCommand("orinstonechat").setTabCompleter(cmd);

        getLogger().info("=================================");
        getLogger().info("  OrinstoneChat v" + getDescription().getVersion() + " activé !");
        getLogger().info("  Plugin de chat coloré pour Paper 1.21.1");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("OrinstoneChat désactivé.");
    }

    public static OrinstoneChat getInstance() {
        return instance;
    }
}
