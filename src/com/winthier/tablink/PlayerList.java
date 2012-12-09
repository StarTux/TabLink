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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This class caches a list of online players on a remote
 * server.
 */
public class PlayerList {
        private TabLinkPlugin plugin;
        private String name;
        private Map<String, PlayerStatus> players = new TreeMap<String, PlayerStatus>(String.CASE_INSENSITIVE_ORDER);
        private String format = "%s";

        public PlayerList(TabLinkPlugin plugin, String name) {
                this.plugin = plugin;
                this.name = name;
                loadConfiguration();
        }

        public void reloadConfiguration() {
                terminate();
                loadConfiguration();
                broadcast();
        }

        public void loadConfiguration() {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("clients." + name);
                if (section == null) {
                        format = ChatColor.ITALIC.toString() + "%s";
                } else {
                        String format = section.getString("Format", "&o{player}");
                        format = format.replace("{player}", "%s");
                        this.format = Util.replaceColorCodes(format);
                }
        }

        public void update(PlayerStatus status) {
                if (status.online) {
                        PlayerStatus entry = players.get(status.name);
                        if (entry == null) {
                                entry = new PlayerStatus(status);
                                players.put(status.name, entry);
                        } else {
                                entry.update(status);
                        }
                } else {
                        PlayerStatus entry = players.remove(status.name);
                        if (entry != null) {
                                entry.update(status);
                        }
                }
        }

        public PlayerStatus get(final String name) {
                return players.get(name);
        }

        public String getName() {
                return name;
        }

        public List<PlayerStatus> getList() {
                return new ArrayList<PlayerStatus>(players.values());
        }

        public void setFormat(String format) {
                this.format = format;
        }

        public String getDisplayName(String name) {
                return String.format(format, name);
        }

        public String getDisplayName(PlayerStatus status) {
                return getDisplayName(status.name);
        }

        public void send(PlayerStatus status, Player player) {
                Util.sendStatus(status, this, player);
        }

        public void send(PlayerStatus status, boolean online, Player player) {
                Util.sendStatus(status, this, online, player);
        }

        public void send(Player player) {
                for (PlayerStatus status : players.values()) send(status, player);
        }

        public void broadcast(PlayerStatus status) {
                Util.broadcastStatus(status, this);
        }

        public void broadcast() {
                for (PlayerStatus status : players.values()) {
                        broadcast(status);
                }
        }

        public void terminate() {
                for (PlayerStatus status : players.values()) {
                        Util.broadcastStatus(getDisplayName(status.name), false);
                }
                //players.clear();
        }
}
