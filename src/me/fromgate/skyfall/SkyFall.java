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

/* Пермишены:
 *  
 *  skyfall.relocation - для получения эффекта падения
 *  skyfall.config - конфигурация
 *  
 * Изменения
 *  v.0.0.2
 *  + Переделаны команды, более человеческий способ настройки
 *  + Добавлен пермишен на взлеты и падения
 *  + Добавлена настройка высоты мира
 * 
 * TODO
 *  - опции для телепорта на спавн вместо взлета или падения
 */

package me.fromgate.skyfall;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public class SkyFall extends JavaPlugin {
	

	//конфигурация
	int fall_random_radius=0;
	boolean showmsg = true;
	String language = "english";
	boolean save_lng = false;
	boolean vcheck = true;
	
	boolean link_time = true;
	int link_time_period = 10; // в секундах

	// разные переменные
	SFWorlds worlds;
	SFUtil u;	
	private SFCmd sfcmd;
	private SFListener sfl;
	
	BukkitTask tid;
	boolean tid_active=false;

	public void saveCfg(){
		getConfig().set("settings.fall-raidus", fall_random_radius);
		getConfig().set("settings.show-world-move-message", showmsg);
		getConfig().set("general.language", language);
		getConfig().set("general.check-updates", vcheck);
		getConfig().set("settings.time-link.enable", link_time);
		getConfig().set("settings.time-link.linking-delay", link_time_period);
		saveConfig();
	}

	public void loadCfg(){
		fall_random_radius=getConfig().getInt("settings.fall-raidus", 0);
		showmsg=getConfig().getBoolean("settings.show-world-move-message", true);
		language=getConfig().getString("general.language", "english");
		save_lng=getConfig().getBoolean("general.language-save", false);
		vcheck=getConfig().getBoolean("general.check-updates",true);
		link_time= getConfig().getBoolean("settings.time-link.enable", false);
		link_time_period= getConfig().getInt("settings.time-link.linking-delay", 30);
	}

	
	public void restartTicks(){
		if (tid_active) getServer().getScheduler().cancelTask(tid.getTaskId());
		if (link_time){
			tid_active = true;
			tid= getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
				public void run (){
					worlds.processWorldTimeLinking();
				}
			}, 200,20*link_time_period);	
		}
	}


	@Override
	public void onEnable() {
		loadCfg();
		saveCfg();
		u = new SFUtil (this, vcheck, save_lng, language,"skyfall","SkyFall","skyfall",ChatColor.DARK_AQUA+"[SkyFall] "+ ChatColor.WHITE);
		worlds = new SFWorlds (this);
		sfcmd = new SFCmd (this);
		getCommand("skyfall").setExecutor(sfcmd);
		sfl =  new SFListener (this);
		getServer().getPluginManager().registerEvents(sfl, this);
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (Exception e) {
		}
	}
}
