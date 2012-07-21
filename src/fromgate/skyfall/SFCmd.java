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
				if (args.length==1) return ExecuteCmd (p, args[0]);
				else if (args.length==2) return ExecuteCmd (p, args[0],args[1]);
				else if (args.length==3) return ExecuteCmd (p, args[0],args[1],args[2]);
				else if (args.length==4) return ExecuteCmd (p, args[0],args[1],args[2],args[3]);
			} else u.PrintMSG(p, "cmd_cmdpermerr",'c');
		} 
		return false;
	}

	public boolean ExecuteCmd (Player p, String cmd){
		if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p);
			return true;
		} else if (cmd.equalsIgnoreCase("under")){
			plg.worlds.get(p.getWorld().getName()).world_under="";
			plg.SaveWorldLinks();
			u.PrintMSG(p, "msg_wuadd_empty",p.getWorld().getName());
			return true;
		} else if (cmd.equalsIgnoreCase("above")){
			plg.worlds.get(p.getWorld().getName()).world_above="";
			plg.SaveWorldLinks();
			u.PrintMSG(p, "msg_waadd_empty",p.getWorld().getName());
			return true; 
		} else if (cmd.equalsIgnoreCase("cfg")){
			u.PrintCfg(p);
			return true;
		} else if (cmd.equalsIgnoreCase("clearworldlinks")){
			plg.worlds.clear();
			plg.InitWorlds();
			plg.SaveWorldLinks();
			u.PrintMSG(p, "msg_clearworldlinks");
			return true;
		} else if (cmd.equalsIgnoreCase("reload")){
			plg.LoadCfg();
			plg.InitWorlds();
			plg.LoadWorldLinks();
			plg.SaveWorldLinks();
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
		} else u.PrintMSG(p, "msg_wlundefined");



		return false;
	}




	//команда + параметры
	public boolean ExecuteCmd (Player p, String cmd, String arg){
		String wn = p.getWorld().getName();

		if (cmd.equalsIgnoreCase("under")){
			World w = Bukkit.getWorld(arg);
			if (w != null){
				plg.worlds.get(wn).world_under=w.getName();
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_wuadd",wn+";"+w.getName());
			} else u.PrintMSG(p,"msg_worldunknown",arg);
			return true;
		} else if (cmd.equalsIgnoreCase("above")){
			World w = Bukkit.getWorld(arg);
			if (w != null){
				plg.worlds.get(wn).world_above=w.getName();
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_waadd",wn+";"+w.getName());
			} else u.PrintMSG(p,"msg_worldunknown",arg);
			return true;
		} else if (cmd.equalsIgnoreCase("height")){
			if (arg.matches("[1-9]+[0-9]*")){
				plg.worlds.get(wn).height = Integer.parseInt(arg);
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_height",wn+";"+arg);			
			} else u.PrintMSG(p,"msg_wrongheight",arg);
		} else if (cmd.equalsIgnoreCase("depth")){
			if (arg.matches("[1-9]+[0-9]*")){
				plg.worlds.get(wn).depth = Integer.parseInt(arg);
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_depth",wn+";"+arg);			
			} else u.PrintMSG(p,"msg_wrongdepth",arg);

		} else if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p, arg);
			return true;
		}

		return false;
	}


	//команда + 2 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2){

		if (cmd.equalsIgnoreCase("area")){
			String [] c1 = arg1.split(",");
			String [] c2 = arg2.split(",");
			if ((c1.length==2)&&(c1[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c1[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&
					(c2.length==2)&&(c2[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c2[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))){
				String wn = p.getWorld().getName();
				plg.worlds.get(wn).x1=Integer.parseInt(c1[0]);
				plg.worlds.get(wn).z1=Integer.parseInt(c1[1]);
				plg.worlds.get(wn).x2=Integer.parseInt(c2[0]);
				plg.worlds.get(wn).z2=Integer.parseInt(c2[1]);
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_worldcoord",wn+";"+arg1+";"+arg2);
			} else u.PrintMSG (p,"msg_wrongcoord",arg1+";"+arg2);
			return true;
		} else if (cmd.equalsIgnoreCase("under")){

			if (Bukkit.getWorld(arg1) == null) {
				u.PrintMSG(p,"msg_worldunknown",arg1);
				return true;
			}

			World w = Bukkit.getWorld(arg2);

			if (w != null){
				plg.worlds.get(arg1).world_under=w.getName();
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_wuadd",arg1+";"+w.getName());
			} else u.PrintMSG(p,"msg_worldunknown",arg2);
			return true;
		} else if (cmd.equalsIgnoreCase("above")){

			if (Bukkit.getWorld(arg1) == null) {
				plg.SaveWorldLinks();
				u.PrintMSG(p,"msg_worldunknown",arg1);
				return true;
			}

			World w = Bukkit.getWorld(arg2);
			if (w != null){
				plg.worlds.get(arg1).world_above=w.getName();
				plg.SaveWorldLinks();
				u.PrintMSG(p, "msg_waadd",arg1+";"+w.getName());
			} else u.PrintMSG(p,"msg_worldunknown",arg2);
			return true;
		} else if (cmd.equalsIgnoreCase("height")){
			World w = Bukkit.getWorld(arg2);
			if (w != null){
				if (arg2.matches("[1-9]+[0-9]*")){
					plg.worlds.get(w.getName()).height = Integer.parseInt(arg2);
					plg.SaveWorldLinks();
					u.PrintMSG(p, "msg_height",w.getName()+";"+arg2);			
				} else u.PrintMSG(p,"msg_wrongheight",arg2);
			} else u.PrintMSG(p,"msg_worldunknown",arg2);
		} else if (cmd.equalsIgnoreCase("depth")){
			World w = Bukkit.getWorld(arg2);
			if (w != null){
				if (arg2.matches("[1-9]+[0-9]*")){
					plg.worlds.get(w.getName()).depth = Integer.parseInt(arg2);
					plg.SaveWorldLinks();
					u.PrintMSG(p, "msg_depth",w.getName()+";"+arg2);			
				} else u.PrintMSG(p,"msg_wrongdepth",arg2);
			} else u.PrintMSG(p,"msg_worldunknown",arg2);			
		}
		return false;
	}

	//команда + 3 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3){
		if (cmd.equalsIgnoreCase("area")){
			if (Bukkit.getWorld(arg1)!=null){
				String [] c1 = arg2.split(",");
				String [] c2 = arg3.split(",");
				if ((c1.length==2)&&(c1[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c1[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&
						(c2.length==2)&&(c2[0].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))&&(c2[1].matches("^(\\+?\\-? *[0-9]+)[0-9]*"))){
					plg.worlds.get(arg1).x1=Integer.parseInt(c1[0]);
					plg.worlds.get(arg1).z1=Integer.parseInt(c1[1]);
					plg.worlds.get(arg1).x2=Integer.parseInt(c2[0]);
					plg.worlds.get(arg1).z2=Integer.parseInt(c2[1]);
					plg.SaveWorldLinks();
					u.PrintMSG(p, "msg_worldcoord",arg1+";"+arg2+";"+arg3);
				} else u.PrintMSG (p,"msg_wrongcoord",arg2+";"+arg3);

			} else u.PrintMSG(p,"msg_worldunknown",arg1);
			return true;
		}
		return false;
	}




}
