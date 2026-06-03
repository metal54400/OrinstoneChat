package fr.orinstone.orinstonechat.listeners;

import fr.orinstone.orinstonechat.OrinstoneChat;
import fr.orinstone.orinstonechat.utils.ColorUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final OrinstoneChat plugin;

    public ChatListener(OrinstoneChat plugin) {
        this.plugin = plugin;
    }

    // LOWEST priority : on modifie le message AVANT que LPC le récupère
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        // Récupère le message brut écrit par le joueur
        String rawMessage = LegacyComponentSerializer.legacySection().serialize(event.message());

        // Traduit les codes & selon les permissions du joueur
        String colored = ColorUtil.translateColors(player, rawMessage);

        // Réinjecte uniquement le message coloré — LPC s'occupe du reste (format, prefix, suffix)
        Component coloredComponent = LegacyComponentSerializer.legacySection().deserialize(colored);
        event.message(coloredComponent);
    }
}
