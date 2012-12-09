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

package com.winthier.tablink.packet;

import java.io.Serializable;
import org.bukkit.entity.Player;

public class PlayerStatus implements Serializable, Cloneable {
        public final String name;
        public boolean online;
        public int ping;

        public PlayerStatus(String name, boolean online, int ping) {
                this.name = name;
                this.online = online;
                this.ping = ping;
        }

        public PlayerStatus(String name, boolean online) {
                this(name, online, 0);
        }

        public PlayerStatus(PlayerStatus other) {
                this(other.name, other.online, other.ping);
        }

        @Override
        public PlayerStatus clone() {
                return new PlayerStatus(this);
        }

        public void update(PlayerStatus other) {
                online = other.online;
                ping = other.ping;
        }
}
