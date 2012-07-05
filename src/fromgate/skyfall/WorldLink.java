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

public class WorldLink{
	int x1;
	int z1;
	int x2;
	int z2;
	String world_under;
	String world_above;

	public WorldLink (int x1, int z1, int x2, int z2, String world_under, String world_above){
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
		this.world_under = world_under;
		this.world_above = world_above;
	}
}
