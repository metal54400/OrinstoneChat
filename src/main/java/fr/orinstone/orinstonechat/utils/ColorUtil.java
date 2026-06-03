package fr.orinstone.orinstonechat.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ColorUtil {

    // Map des codes couleur et leurs permissions associées
    private static final Map<Character, String> COLOR_PERMISSIONS = new HashMap<>();
    private static final Map<Character, String> FORMAT_PERMISSIONS = new HashMap<>();

    static {
        // Couleurs
        COLOR_PERMISSIONS.put('0', "orinstonechat.color.black");
        COLOR_PERMISSIONS.put('1', "orinstonechat.color.dark_blue");
        COLOR_PERMISSIONS.put('2', "orinstonechat.color.dark_green");
        COLOR_PERMISSIONS.put('3', "orinstonechat.color.dark_aqua");
        COLOR_PERMISSIONS.put('4', "orinstonechat.color.dark_red");
        COLOR_PERMISSIONS.put('5', "orinstonechat.color.dark_purple");
        COLOR_PERMISSIONS.put('6', "orinstonechat.color.gold");
        COLOR_PERMISSIONS.put('7', "orinstonechat.color.gray");
        COLOR_PERMISSIONS.put('8', "orinstonechat.color.dark_gray");
        COLOR_PERMISSIONS.put('9', "orinstonechat.color.blue");
        COLOR_PERMISSIONS.put('a', "orinstonechat.color.green");
        COLOR_PERMISSIONS.put('b', "orinstonechat.color.aqua");
        COLOR_PERMISSIONS.put('c', "orinstonechat.color.red");
        COLOR_PERMISSIONS.put('d', "orinstonechat.color.light_purple");
        COLOR_PERMISSIONS.put('e', "orinstonechat.color.yellow");
        COLOR_PERMISSIONS.put('f', "orinstonechat.color.white");
        COLOR_PERMISSIONS.put('r', "orinstonechat.color");  // reset = permission générale couleur

        // Formatage
        FORMAT_PERMISSIONS.put('l', "orinstonechat.format"); // gras
        FORMAT_PERMISSIONS.put('o', "orinstonechat.format"); // italique
        FORMAT_PERMISSIONS.put('n', "orinstonechat.format"); // souligné
        FORMAT_PERMISSIONS.put('m', "orinstonechat.format"); // barré
        FORMAT_PERMISSIONS.put('k', "orinstonechat.magic");  // magique (obfuscated)
    }

    /**
     * Traduit les codes couleur & d'un message selon les permissions du joueur.
     * Les codes non autorisés sont simplement supprimés (pas affichés en brut).
     *
     * @param player  Le joueur qui envoie le message
     * @param message Le message brut
     * @return Le message avec les codes couleur traduits selon les permissions
     */
    public static String translateColors(Player player, String message) {
        if (message == null || message.isEmpty()) return message;

        StringBuilder result = new StringBuilder();
        char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                char code = Character.toLowerCase(chars[i + 1]);

                if (COLOR_PERMISSIONS.containsKey(code)) {
                    String perm = COLOR_PERMISSIONS.get(code);
                    if (player.hasPermission(perm) || player.hasPermission("orinstonechat.color")) {
                        result.append('§').append(chars[i + 1]);
                        i++; // Saute le caractère suivant
                    } else {
                        // Pas de permission : supprime le code silencieusement
                        i++;
                    }
                } else if (FORMAT_PERMISSIONS.containsKey(code)) {
                    String perm = FORMAT_PERMISSIONS.get(code);
                    if (player.hasPermission(perm)) {
                        result.append('§').append(chars[i + 1]);
                        i++;
                    } else {
                        i++;
                    }
                } else {
                    // Pas un code valide, on laisse tel quel
                    result.append(chars[i]);
                }
            } else {
                result.append(chars[i]);
            }
        }

        return result.toString();
    }

    /**
     * Vérifie si un message contient des codes couleur &
     */
    public static boolean hasColorCodes(String message) {
        if (message == null) return false;
        for (int i = 0; i < message.length() - 1; i++) {
            if (message.charAt(i) == '&') {
                char code = Character.toLowerCase(message.charAt(i + 1));
                if (COLOR_PERMISSIONS.containsKey(code) || FORMAT_PERMISSIONS.containsKey(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Traduit un texte simple (pour les messages de config) en Component Adventure
     */
    public static Component colorize(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    /**
     * Traduit un texte simple (pour les messages de config) en String avec §
     */
    public static String colorizeString(String text) {
        return text.replace("&", "§");
    }
}
