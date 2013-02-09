/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright 2012 StarTux
 *
 * This file is part of TabLink.
 *
 * TabLink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TabLink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TabLink.  If not, see <http://www.gnu.org/licenses/>.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package com.winthier.tablink;

import com.winthier.tablink.packet.PlayerStatus;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.server.v1_4_R1.Packet201PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Utiltiy class for sending player status updates to
 * players. This is the only class that should concern itself with
 * packet transfer.
 *
 * It should only be called by PlayerList since that class has
 * knows how to format a player name into its display name.
 */
public class Util {
        /**
         *
         */
        private static void sendInfo(Packet201PlayerInfo info, Player recipient) {
                if (!(recipient instanceof CraftPlayer)) return;
                CraftPlayer player = (CraftPlayer)recipient;
                player.getHandle().playerConnection.sendPacket(info);
        }

        private static Packet201PlayerInfo createPlayerInfo(String name, boolean online, int ping) {
                if (name.length() > 16) name = name.substring(0, 16);
                return new Packet201PlayerInfo(name, online, ping);
        }

        /**
         *
         */
        private static Packet201PlayerInfo getPlayerInfo(PlayerStatus status, PlayerList list) {
                String name = list.getDisplayName(status.name);
                return createPlayerInfo(name, status.online, status.ping);
        }

        /**
         *
         */
        public static void sendStatus(PlayerStatus status, PlayerList list, Player recipient) {
                sendInfo(getPlayerInfo(status, list), recipient);
        }

        /**
         *
         */
        public static void sendStatus(PlayerStatus status, PlayerList list, boolean online, Player recipient) {
                Packet201PlayerInfo info = getPlayerInfo(status, list);
                info.b = online;
                sendInfo(info, recipient);
        }

        public static void broadcastStatus(String name, boolean online) {
                Packet201PlayerInfo info = createPlayerInfo(name, online, 0);
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        sendInfo(info, player);
                }
        }

        /**
         *
         */
        public static void broadcastStatus(PlayerStatus status, PlayerList list) {
                Packet201PlayerInfo info = getPlayerInfo(status, list);
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        sendInfo(info, player);
                }
        }

        public static String replaceColorCodes(String format) {
                Matcher matcher = Pattern.compile("&([0-9a-fklmnor])").matcher(format);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                        matcher.appendReplacement(sb, ChatColor.getByChar(matcher.group(1)).toString());
                }
                matcher.appendTail(sb);
                return sb.toString();
        }
}
