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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SFCmd implements CommandExecutor{
	SkyFall plg;

	FGUtil u;

	public SFCmd (SkyFall plg){
		this.plg = plg;
		this.u = this.plg.u;

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		if (sender instanceof Player){
			Player p = (Player) sender;
			if ((args.length>0)&&(u.CheckCmdPerm(p, args[0]))){
				
				if (!plg.pset.containsKey(p.getName())) plg.pset.put(p.getName(), new PCfg(p));
				
				
				if (args.length==1) return ExecuteCmd (p, args[0]);
				else if (args.length==2) return ExecuteCmd (p, args[0],args[1]);
				else if (args.length==3) return ExecuteCmd (p, args[0],args[1],args[2]);
				else if (args.length==4) return ExecuteCmd (p, args[0],args[1],args[2],args[3]);
			} u.PrintMSG(p, "cmd_wrong",'c');
		} 
		return false;
	}

	public boolean ExecuteCmd (Player p, String cmd){
		if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p);
			return true;
		} else if (cmd.equalsIgnoreCase("setuw")){
			plg.pset.get(p.getName()).world_under="";
			u.PrintMSG(p, "msg_wuadd_empty");
			return true;
		} else if (cmd.equalsIgnoreCase("setaw")){
			plg.pset.get(p.getName()).world_above="";
			u.PrintMSG(p, "msg_waadd_empty");
			return true;
		} else if (cmd.equalsIgnoreCase("cfg")){
			u.PrintCfg(p);
			return true;
		} else if (cmd.equalsIgnoreCase("reload")){
			plg.LoadCfg();
			plg.LoadWorldLinks();
			u.PrintMSG(p, "msg_reloaded");
			return true;
			
		} else if (cmd.equalsIgnoreCase("list")){
			if (plg.worlds.size()>0){
				u.PrintMSG(p, "msg_list",'6');
				for (String wn : plg.worlds.keySet()){
					u.PrintMsg(p, plg.WorldLinkToStr(wn));
				}				
			} else u.PrintMSG(p, "msg_listempty",'e');
			return true;

		} else if (cmd.equalsIgnoreCase("add")){
			PCfg pc = plg.pset.get(p.getName());
			if ((!pc.world.isEmpty())&&
					(pc.x1!=pc.x2)&&
					(pc.z1!=pc.z2)/*&&
					((!pc.world_above.isEmpty())||(!pc.world_under.isEmpty()))*/){
				int x1 = Math.min(pc.x1, pc.x2);
				int z1 = Math.min(pc.z1, pc.z2);
				int x2 = Math.max(pc.x1, pc.x2);
				int z2 = Math.max(pc.z1, pc.z2);

				plg.worlds.put(pc.world, new WorldLink (x1, z1, x2, z2, pc.world_under, pc.world_above));
				plg.SaveWordLinks();

				u.PrintMSG (p,"msg_wladded",pc.world);
				if (pc.world_above.isEmpty()&&pc.world_under.isEmpty()) u.PrintMSG(p, "msg_warnlinks",pc.world);
				
				plg.pset.put(p.getName(), new PCfg(p)); //очищаем персональный конфиг
				
			} else u.PrintMSG(p, "msg_wlundefined");


			return true;

		}
		return false;
	}




	//команда + параметры
	public boolean ExecuteCmd (Player p, String cmd, String arg){

		if (cmd.equalsIgnoreCase("setuw")){
			World w = Bukkit.getWorld(arg);
			
			if (w != null){
				plg.pset.get(p.getName()).world_under=w.getName();
				u.PrintMSG(p, "msg_wuadd");
				u.PrintMsg(p, plg.PlayerConfigToStr(p.getName()));
			} else u.PrintMSG(p,"msg_worldunknown",arg);
			return true;
		} else if (cmd.equalsIgnoreCase("setaw")){
			World w = Bukkit.getWorld(arg);
			if (w != null){
				plg.pset.get(p.getName()).world_above=w.getName();
				u.PrintMSG(p, "msg_waadd");
				u.PrintMsg(p, plg.PlayerConfigToStr(p.getName()));
			} else u.PrintMSG(p,"msg_worldunknown",arg);
			return true;
		} else if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p, arg);
			return true;
		}

		return false;
	}


	//команда + 2 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2){
		if (cmd.equalsIgnoreCase("setc")){
			String [] c1 = arg1.split(",");
			String [] c2 = arg2.split(",");
//if ((c1.length==2)&&(c1[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c1[1].matches("-{,1}[1-9]+[0-9]*"))&&
			if ((c1.length==2)&&(c1[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c1[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&
					(c2.length==2)&&(c2[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c2[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))){
				String wn = p.getWorld().getName();
				plg.pset.get(p.getName()).world = wn;
				plg.pset.get(p.getName()).x1 = Integer.parseInt(c1[0]);
				plg.pset.get(p.getName()).z1 = Integer.parseInt(c1[1]);
				plg.pset.get(p.getName()).x2 = Integer.parseInt(c2[0]);
				plg.pset.get(p.getName()).z2 = Integer.parseInt(c2[1]);

				u.PrintMSG(p, "msg_coordadd");
				u.PrintMsg(p, plg.PlayerConfigToStr(p.getName()));
			} else u.PrintMSG (p,"msg_wrongcoord",arg1+";"+arg2);
			return true;
		}
		return false;
	}

	//команда + 3 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3){
		if (cmd.equalsIgnoreCase("setc")){
			if (Bukkit.getWorld(arg1)!=null){


				String [] c1 = arg2.split(",");
				String [] c2 = arg3.split(",");
				if ((c1.length==2)&&(c1[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c1[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&
						(c2.length==2)&&(c2[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c2[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))){

					plg.pset.get(p.getName()).world = arg1;
					plg.pset.get(p.getName()).x1 = Integer.parseInt(c1[0]);
					plg.pset.get(p.getName()).z1 = Integer.parseInt(c1[1]);
					plg.pset.get(p.getName()).x2 = Integer.parseInt(c2[0]);
					plg.pset.get(p.getName()).z2 = Integer.parseInt(c2[1]);
					u.PrintMSG(p, "msg_curcoord",arg1+";"+arg2+";"+arg3);

				} else u.PrintMSG (p,"msg_wrongcoord",arg2+";"+arg3);

			} else u.PrintMSG(p,"msg_worldunknown",arg1);
			return true;
		}
		return false;
	}




}
