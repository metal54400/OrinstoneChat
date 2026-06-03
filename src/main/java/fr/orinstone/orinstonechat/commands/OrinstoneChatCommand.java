package fr.orinstone.orinstonechat.commands;

import fr.orinstone.orinstonechat.OrinstoneChat;
import fr.orinstone.orinstonechat.utils.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrinstoneChatCommand implements CommandExecutor, TabCompleter {

    private final OrinstoneChat plugin;

    public OrinstoneChatCommand(OrinstoneChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("orinstonechat.admin")) {
            sender.sendMessage(ColorUtil.colorize("&cVous n'avez pas la permission d'utiliser cette commande."));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                String reloadMsg = plugin.getConfig().getString("messages.reloaded",
                        "&aOrinstoneChat rechargé avec succès!");
                sender.sendMessage(ColorUtil.colorize(reloadMsg));
                break;

            case "info":
                String infoMsg = plugin.getConfig().getString("messages.plugin-info",
                        "&6OrinstoneChat &7v%version% &8- &7Plugin de chat coloré");
                infoMsg = infoMsg.replace("%version%", plugin.getDescription().getVersion());
                sender.sendMessage(ColorUtil.colorize(infoMsg));
                sender.sendMessage(ColorUtil.colorize("&7Auteur: &f" + String.join(", ", plugin.getDescription().getAuthors())));
                sender.sendMessage(ColorUtil.colorize("&7API: &fPaper 1.21.1"));
                break;

            case "colors":
                sendColorList(sender);
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ColorUtil.colorize("&6&l--- OrinstoneChat ---"));
        sender.sendMessage(ColorUtil.colorize("&e/ochat reload &7- Recharge la configuration"));
        sender.sendMessage(ColorUtil.colorize("&e/ochat info &7- Informations sur le plugin"));
        sender.sendMessage(ColorUtil.colorize("&e/ochat colors &7- Liste des codes couleur"));
    }

    private void sendColorList(CommandSender sender) {
        sender.sendMessage(ColorUtil.colorize("&6&l--- Codes couleur disponibles ---"));
        sender.sendMessage(ColorUtil.colorize("&00 &11 &22 &33 &44 &55 &66 &77 &88 &99 &aa &bb &cc &dd &ee &ff"));
        sender.sendMessage(ColorUtil.colorize("&7Formatage: &l&lGras &r&o&oItalique &r&n&nSouligné &r&m&mBarré"));
        sender.sendMessage(ColorUtil.colorize("&7Utilisez &e& &7suivi du code dans le chat."));
        sender.sendMessage(ColorUtil.colorize("&7Exemple: &e&7Bonjour &7affiche en gris."));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("orinstonechat.admin")) return List.of();

        if (args.length == 1) {
            List<String> completions = Arrays.asList("reload", "info", "colors");
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
