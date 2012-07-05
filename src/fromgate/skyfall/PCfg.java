/*  
 *  SkyFall, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/skyfall/
 *    
 *  This file is part of Dogtags.
 *  
 *  SkyFall is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SkyFall is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package fromgate.skyfall;

import org.bukkit.entity.Player;

public 	class PCfg{
	String world;
	int x1;
	int z1;
	int x2;
	int z2;
	String world_under;
	String world_above;

	public PCfg (Player p){
		this.world = p.getWorld().getName();
		this.x1 = p.getLocation().getBlockX();
		this.z1 = p.getLocation().getBlockZ();
		this.x2 = p.getLocation().getBlockX();
		this.z2 = p.getLocation().getBlockZ();
		this.world_under="";
		this.world_above="";
	}
}