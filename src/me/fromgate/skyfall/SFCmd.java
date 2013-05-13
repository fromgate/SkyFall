/*  
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

package me.fromgate.skyfall;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SFCmd implements CommandExecutor{
	SkyFall plg;

	SFUtil u;

	public SFCmd (SkyFall plg){
		this.plg = plg;
		this.u = this.plg.u;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		if (sender instanceof Player){
			Player p = (Player) sender;
			if ((args.length>0)&&(u.checkCmdPerm(p, args[0]))){
				if (args.length==1) return ExecuteCmd (p, args[0]);
				else if (args.length==2) return ExecuteCmd (p, args[0],args[1]);
				else if (args.length==3) return ExecuteCmd (p, args[0],args[1],args[2]);
				else if (args.length==4) return ExecuteCmd (p, args[0],args[1],args[2],args[3]);
			} else u.printMSG(p, "cmd_cmdpermerr",'c');
		} 
		return false;
	}

	public boolean ExecuteCmd (Player p, String cmd){
		String pw = p.getWorld().getName();
		
		if (cmd.equalsIgnoreCase("help")){
			u.PrintHlpList(p, 1, 15);
			return true;
		} else if (cmd.equalsIgnoreCase("linktime")){
			plg.link_time = !plg.link_time;
			plg.saveConfig();
			plg.restartTicks();
			u.printEnDis(p, "msg_linktimeenabled",plg.link_time);
		} else if (cmd.equalsIgnoreCase("fallrnd")){	
			if (plg.worlds.setRandomFallInEnabled(pw, !plg.worlds.isRandomFallInEnabled(pw)))
				u.printMSG(p, "msg_fallrnd",pw,u.EnDis(plg.worlds.isRandomFallInEnabled(pw)));
			else u.printMSG(p, "msg_fallrndfailed",pw);
		} else if (cmd.equalsIgnoreCase("climbrnd")){	
			if (plg.worlds.setRandomClimbInEnabled(pw, !plg.worlds.isRandomClimbInEnabled(pw)))
				u.printMSG(p, "msg_climbrnd",pw,u.EnDis(plg.worlds.isRandomClimbInEnabled(pw)));
			else u.printMSG(p, "msg_climbrndfailed",pw);
		} else if (cmd.equalsIgnoreCase("climbin")){	
			if (plg.worlds.setClimbInLocEnabled(pw, !plg.worlds.isClimbInLocEnabled(pw)))
				u.printMSG(p, "msg_climbin",pw,u.EnDis(plg.worlds.isClimbInLocEnabled(pw)));
			else u.printMSG(p, "msg_climbinfailed",pw);
		} else if (cmd.equalsIgnoreCase("fallin")){	
			if (plg.worlds.setFallInLocEnabled(pw, !plg.worlds.isFallInLocEnabled(pw)))
				u.printMSG(p, "msg_fallin",pw,u.EnDis(plg.worlds.isFallInLocEnabled(pw)));
			else u.printMSG(p, "msg_fallinfailed",pw);
		} else if (cmd.equalsIgnoreCase("clearfall")){
			if (plg.worlds.clearFallInLoc(p.getWorld().getName()))
				u.printMSG(p, "msg_clearfall",p.getWorld().getName());
			else u.printMSG(p, "msg_clearfallfailed",p.getWorld().getName());
		} else if (cmd.equalsIgnoreCase("clearclimb")){
			if (plg.worlds.clearFallInLoc(p.getWorld().getName()))
				u.printMSG(p, "msg_clearclimb",p.getWorld().getName());
			else u.printMSG(p, "msg_clearclimbfailed",p.getWorld().getName());
		} else if (cmd.equalsIgnoreCase("locfall")){
			plg.worlds.addFallInLoc(p.getLocation(), "");
			u.printMSG(p, "msg_locfall",p.getLocation());
		} else if (cmd.equalsIgnoreCase("locclimb")){
			plg.worlds.addClimbInLoc(p.getLocation(), "");
			u.printMSG(p, "msg_locclimb",p.getLocation());
		} else if (cmd.equalsIgnoreCase("under")){
			plg.worlds.setUnder(p.getWorld().getName(), "");
			u.printMSG(p, "msg_wuadd_empty",p.getWorld().getName());
		} else if (cmd.equalsIgnoreCase("above")){
			plg.worlds.setAbove(p.getWorld().getName(), "");
			u.printMSG(p, "msg_waadd_empty",p.getWorld().getName());
		} else if (cmd.equalsIgnoreCase("cfg")){
			u.PrintCfg(p);
		} else if (cmd.equalsIgnoreCase("clearworldlinks")){
			plg.worlds.clear();
			u.printMSG(p, "msg_clearworldlinks");
		} else if (cmd.equalsIgnoreCase("reload")){
			plg.reloadConfig();
			plg.loadCfg();
			plg.worlds.initWorlds();
			plg.worlds.loadWorldLinks();
			plg.worlds.saveWorldLinks();
			u.printMSG(p, "msg_reloaded");
		} else if (cmd.equalsIgnoreCase("list")){
			plg.worlds.printWorldList(p);
			
		} else return false;
		
		return true;
	}




	//команда + параметры
	public boolean ExecuteCmd (Player p, String cmd, String arg){
		String wn = p.getWorld().getName();

		if (cmd.equalsIgnoreCase("under")){
			World w = Bukkit.getWorld(arg);
			if (w != null){
				plg.worlds.setUnder(wn, w.getName());
				u.printMSG(p, "msg_wuadd",wn,w.getName());
			} else u.printMSG(p,"msg_worldunknown",arg);
		} else if (cmd.equalsIgnoreCase("linktime")){
			World w = Bukkit.getWorld(arg);
			if ((w != null)||(arg.equalsIgnoreCase("remove"))){
				plg.worlds.setWorldTimeLink(wn, arg);
				if (plg.worlds.isWorldTimeLinked(wn)) u.printMSG(p, "msg_linktimelinked",wn,arg);
				else u.printMSG(p, "msg_linktimeunlinked",wn);
			} else u.printMSG(p,"msg_worldunknown",arg);
		} else if (cmd.equalsIgnoreCase("fallrnd")){	
			if (plg.worlds.setRandomFallInEnabled(arg, !plg.worlds.isRandomFallInEnabled(arg)))
				u.printMSG(p, "msg_fallrnd",arg,u.EnDis(plg.worlds.isRandomFallInEnabled(arg)));
			else u.printMSG(p, "msg_fallrndfailed",arg);
		} else if (cmd.equalsIgnoreCase("climbrnd")){	
			if (plg.worlds.setRandomClimbInEnabled(arg, !plg.worlds.isRandomClimbInEnabled(arg)))
				u.printMSG(p, "msg_climbrnd",arg,u.EnDis(plg.worlds.isRandomClimbInEnabled(arg)));
			else u.printMSG(p, "msg_climbrndfailed",arg);
			
		} else if (cmd.equalsIgnoreCase("climbin")){	
			if (plg.worlds.setClimbInLocEnabled(arg, !plg.worlds.isClimbInLocEnabled(arg)))
				u.printMSG(p, "msg_climbin",arg, u.EnDis(plg.worlds.isClimbInLocEnabled(arg)));
			else u.printMSG(p, "msg_climbinfailed",arg);
		} else if (cmd.equalsIgnoreCase("fallin")){	
			if (plg.worlds.setFallInLocEnabled(arg, !plg.worlds.isFallInLocEnabled(arg)))
				u.printMSG(p, "msg_fallin",arg, u.EnDis(plg.worlds.isFallInLocEnabled(arg)));
			else u.printMSG(p, "msg_fallinfailed",arg);
		} else if (cmd.equalsIgnoreCase("clearfall")){
			if (plg.worlds.clearFallInLoc(arg))
				u.printMSG(p, "msg_clearfall",arg);
			else u.printMSG(p, "msg_clearfallfailed",arg);
		} else if (cmd.equalsIgnoreCase("clearclimb")){
			if (plg.worlds.clearFallInLoc(arg))
				u.printMSG(p, "msg_clearclimb",arg);
			else u.printMSG(p, "msg_clearclimbfailed",arg);
			
		} else if (cmd.equalsIgnoreCase("locfall")){
			plg.worlds.addFallInLoc(p.getLocation(), arg);
			u.printMSG(p, "msg_locfall2",p.getLocation(),arg);
		} else if (cmd.equalsIgnoreCase("locclimb")){
			plg.worlds.addClimbInLoc(p.getLocation(), arg);
			u.printMSG(p, "msg_locclimb2",p.getLocation(),arg);
		} else if (cmd.equalsIgnoreCase("above")){
			World w = Bukkit.getWorld(arg);
			if (w != null){
				plg.worlds.setAbove(wn, w.getName());
				u.printMSG(p, "msg_waadd",wn,w.getName());
			} else u.printMSG(p,"msg_worldunknown",arg);
		} else if (cmd.equalsIgnoreCase("height")){
			if (arg.matches("[1-9]+[0-9]*")){
				plg.worlds.setHeightLevel(wn, Integer.parseInt(arg));
				u.printMSG(p, "msg_height",wn,arg);			
			} else u.printMSG(p,"msg_wrongheight",arg);
		} else if (cmd.equalsIgnoreCase("depth")){
			if (arg.matches("[1-9]+[0-9]*")){
				plg.worlds.setDepthLevel(wn, Integer.parseInt(arg));
				u.printMSG(p, "msg_depth",wn,arg);			
			} else u.printMSG(p,"msg_wrongdepth",arg);

		} else if (cmd.equalsIgnoreCase("help")){
			if (u.isInteger(arg)) u.PrintHlpList(p, Integer.parseInt(arg), 15);
			else u.PrintHlpList(p, 1, 15);
			
		} else return false;

		return true;
	}


	//команда + 2 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2){
		if (cmd.equalsIgnoreCase("area")){
			String [] c1 = arg1.split(",");
			String [] c2 = arg2.split(",");
			if ((c1.length == 2)&&(c2.length==2)&&u.isIntegerSigned(c1[0],c1[1],c2[0],c2[1])){
				String wn = p.getWorld().getName();
				plg.worlds.setArea(wn, Integer.parseInt(c1[0]), Integer.parseInt(c1[1]), Integer.parseInt(c2[0]), Integer.parseInt(c2[1]));
				u.printMSG(p, "msg_worldcoord",wn,arg1,arg2);
			} else u.printMSG (p,"msg_wrongcoord",arg1,arg2);
		} else if (cmd.equalsIgnoreCase("under")){
			if (Bukkit.getWorld(arg1) == null) {
				u.printMSG(p,"msg_worldunknown",arg1);
				return true;
			}
			World w = Bukkit.getWorld(arg2);
			if (w != null){
				plg.worlds.setUnder(arg1, w.getName());
				u.printMSG(p, "msg_wuadd",arg1,w.getName());
			} else u.printMSG(p,"msg_worldunknown",arg2);
		} else if (cmd.equalsIgnoreCase("above")){
			if (Bukkit.getWorld(arg1) == null) {
				u.printMSG(p,"msg_worldunknown",arg1);
				return true;
			}
			World w = Bukkit.getWorld(arg2);
			if (w != null){
				plg.worlds.setAbove(arg1, w.getName());
				u.printMSG(p, "msg_waadd",arg1,w.getName());
			} else u.printMSG(p,"msg_worldunknown",arg2);
		} else if (cmd.equalsIgnoreCase("height")){
			World w = Bukkit.getWorld(arg1);
			if (w != null){
				if (u.isIntegerGZ(arg2)){
					plg.worlds.setHeightLevel(w.getName(), Integer.parseInt(arg2));
					u.printMSG(p, "msg_height",w.getName(),arg2);			
				} else u.printMSG(p,"msg_wrongheight",arg2);
			} else u.printMSG(p,"msg_worldunknown",arg1);
		} else if (cmd.equalsIgnoreCase("depth")){
			World w = Bukkit.getWorld(arg1);
			if (w != null){
				if (u.isIntegerGZ(arg2)){
					plg.worlds.setDepthLevel(w.getName(), Integer.parseInt(arg2));
					u.printMSG(p, "msg_depth",w.getName(),arg2);			
				} else u.printMSG(p,"msg_wrongdepth",arg2);
			} else u.printMSG(p,"msg_worldunknown",arg1);
		} else if (cmd.equalsIgnoreCase("locfall")||cmd.equalsIgnoreCase("locclimb")){
			return ExecuteCmd (p,cmd, arg1+" "+arg2);
		} else return false;
		
		return true;
		
	}
	//команда + 3 параметра
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3){
		if (cmd.equalsIgnoreCase("area")){
			if (Bukkit.getWorld(arg1)!=null){
				String [] c1 = arg2.split(",");
				String [] c2 = arg3.split(",");
				if ((c1.length == 2)&&(c2.length==2)&&u.isIntegerSigned(c1[0],c1[1],c2[0],c2[1])){
					plg.worlds.setArea(arg1, Integer.parseInt(c1[0]), Integer.parseInt(c1[1]), Integer.parseInt(c2[0]), Integer.parseInt(c2[1]));
					u.printMSG(p, "msg_worldcoord",arg1,arg2,arg3);
				} else u.printMSG (p,"msg_wrongcoord",arg2,arg3);
			} else u.printMSG(p,"msg_worldunknown",arg1);
		} else if (cmd.equalsIgnoreCase("locfall")||cmd.equalsIgnoreCase("locclimb")){
			return ExecuteCmd (p,cmd, arg1+" "+arg2+" "+arg3);
		}else return false;
		return true;
	}




}
