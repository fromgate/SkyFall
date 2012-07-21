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


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class SFListener implements Listener{
	SkyFall plg;
	FGUtilCore u;

	public SFListener (SkyFall plg){
		this.plg = plg;
		this.u = plg.u;
	}



	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoin (PlayerJoinEvent event){
		u.UpdateMsg(event.getPlayer());
	}


	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove (PlayerMoveEvent event){
		Player p = event.getPlayer();
		String wn = p.getWorld().getName();
		if ((plg.worlds.size()>0)&&((p.getLocation().getY()<plg.worlds.get(wn).depth)||(p.getLocation().getY()>plg.worlds.get(wn).height))&&p.hasPermission("skyfall.relocation")){
			

			if ((p.getLocation().getY()<plg.worlds.get(wn).depth)&&((!plg.worlds.get(wn).world_under.isEmpty()))){
				// Падение
				Location loc = plg.getFallLocation(p.getLocation(), plg.worlds.get(plg.worlds.get(wn).world_under).height, plg.worlds.get(wn).world_under, plg.fall_random_radius);



				while (!loc.getChunk().isLoaded())
					loc.getChunk().load();
				p.teleport(loc, TeleportCause.PLUGIN);

				if (plg.showmsg) u.PrintMSG (p, "msg_fallmsg",plg.worlds.get(wn).world_under);  

			} else if ((p.getLocation().getY()>plg.worlds.get(wn).height)&&((!plg.worlds.get(wn).world_above.isEmpty()))){
				// Взлёт ;)
				Location loc = plg.getFallLocation(p.getLocation(), plg.worlds.get(plg.worlds.get(wn).world_above).depth, plg.worlds.get(wn).world_above, 0);
				while (!loc.getChunk().isLoaded())
					loc.getChunk().load();

				p.teleport(loc, TeleportCause.PLUGIN);
				if (plg.showmsg) u.PrintMSG (p, "msg_climbmsg",plg.worlds.get(wn).world_above);


				///////////////////////////////////////////////////////////////////////
				// Нужно продумать как наиболее эффективно сделать "подставку" 
				// для поднимающегося игрока
				///////////////////////////////////////////////////////////////////////


			}

		}


	}


}




