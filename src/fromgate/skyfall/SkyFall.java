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

package fromgate.skyfall;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SkyFall extends JavaPlugin {

	//конфигурация
	int fall_random_radius=0;
	boolean showmsg = true;
	String language = "english";
	boolean save_lng = false;
	boolean vcheck = true;



	// разные переменные
	//HashMap<String,PCfg> pset = new HashMap<String,PCfg>();
	HashMap<String,WorldLink> worlds = new HashMap<String,WorldLink>(); 
	FGUtil u;	
	Logger log = Logger.getLogger("Minecraft");
	private SFCmd sfcmd;
	private SFListener sfl;


	public void SaveCfg(){
		getConfig().set("settings.fall-raidus", fall_random_radius);
		getConfig().set("settings.show-world-move-message", showmsg);
		getConfig().set("general.language", language);
		getConfig().set("general.check-updates", vcheck);
		saveConfig();
	}

	public void LoadCfg(){
		fall_random_radius=getConfig().getInt("settings.fall-raidus", 0);
		showmsg=getConfig().getBoolean("settings.show-world-move-message", true);
		language=getConfig().getString("general.language", "english");
		save_lng=getConfig().getBoolean("general.language-save", false);
		vcheck=getConfig().getBoolean("general.check-updates",true);
	}




	@Override
	public void onEnable() {

		LoadCfg();
		SaveCfg();

		u = new FGUtil (this, vcheck, save_lng, language,"skyfall","SkyFall","skyfall",ChatColor.DARK_AQUA+"[SF] "+ ChatColor.WHITE);
		sfcmd = new SFCmd (this);
		getCommand("skyfall").setExecutor(sfcmd);
		sfl =  new SFListener (this);

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(sfl, this);


		InitWorlds();
		LoadWorldLinks();
		SaveWorldLinks();

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			log.info("[SF] failed to submit stats to the Metrics (mcstats.org)");
		}
	}


	public void InitWorlds(){
		for (int i = 0; i<Bukkit.getWorlds().size(); i++){
			World w = Bukkit.getWorlds().get(i);
			
			if (!worlds.containsKey(w.getName())) {
			
			int x = w.getSpawnLocation().getBlockX();
			int z = w.getSpawnLocation().getBlockZ();

			WorldLink wl = new WorldLink(x,z,x,z,255,0,"","");
			worlds.put(w.getName(), wl);
			}
		}


	}

	public void LoadWorldLinks(){
		try {
			YamlConfiguration wl = new YamlConfiguration();
			File f = new File (getDataFolder()+File.separator+"wordlinks.yml");
			if (f.exists()) {
				wl.load(f);
				Set<String> keys = wl.getKeys(false);
				if (keys.size()>0)
					for (String key : keys)
							worlds.put(key, new WorldLink (wl.getInt(key+".x1",0), wl.getInt(key+".z1",0),
									wl.getInt(key+".x2",0), wl.getInt(key+".z2",0),
									wl.getInt(key+".height",255), wl.getInt(key+".depth",0),
									wl.getString(key+".world.under",""), wl.getString(key+".world.above","")));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void SaveWorldLinks(){
		try {
			YamlConfiguration wl = new YamlConfiguration();
			for (String wn : worlds.keySet()){
				wl.set(wn+".x1", worlds.get(wn).x1);
				wl.set(wn+".z1", worlds.get(wn).z1);
				wl.set(wn+".x2", worlds.get(wn).x2);
				wl.set(wn+".z2", worlds.get(wn).z2);
				wl.set(wn+".height", worlds.get(wn).height);
				wl.set(wn+".depth", worlds.get(wn).depth);
				wl.set(wn+".world.under", worlds.get(wn).world_under);
				wl.set(wn+".world.above", worlds.get(wn).world_above);
			}
			File f = new File (getDataFolder()+File.separator+"wordlinks.yml");
			if (f.exists()) f.delete();
			wl.save(f);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public Location getFallLocation (Location sourceloc, int ny, String tworld, int rndradius){
		String sworld = sourceloc.getWorld().getName();

		double x1 = Math.min(worlds.get(sworld).x1, worlds.get(sworld).x2);
		double z1 = Math.min(worlds.get(sworld).z1, worlds.get(sworld).z2);
		double x2 = Math.max(worlds.get(sworld).x1, worlds.get(sworld).x2);
		double z2 = Math.max(worlds.get(sworld).z1, worlds.get(sworld).z2);
		
		double pdx = (sourceloc.getX()-x1)/(x2-x1);
		double pdz = (sourceloc.getZ()-z1)/(z2-z1);


		x1 = Math.min(worlds.get(tworld).x1, worlds.get(tworld).x2);
		z1 = Math.min(worlds.get(tworld).z1, worlds.get(tworld).z2);
		x2 = Math.max(worlds.get(tworld).x1, worlds.get(tworld).x2);
		z2 = Math.max(worlds.get(tworld).z1, worlds.get(tworld).z2);

		double ndx = x1+ pdx*(x2-x1);
		double ndz = z1+ pdz*(z2-z1);

		if (rndradius>0){
			ndx = ndx + u.random.nextInt(rndradius);
			ndz = ndz + u.random.nextInt(rndradius);
		}

		//корректируем координаты, чтобы не выбивались за границы.
		ndx = Math.min(x2-1, Math.max (x1+1, ndx));
		ndz = Math.min(z2-1, Math.max (z1+1, ndz));

		Location loc=new Location (Bukkit.getWorld(tworld), ndx, (double) ny, ndz, sourceloc.getYaw(), sourceloc.getPitch());
		loc.setX(loc.getBlockX()+0.5);
		loc.setZ(loc.getBlockZ()+0.5);

		return loc;
	}


	public String WorldLinkToStr (String wn){
		int x1 = worlds.get(wn).x1;
		int z1 = worlds.get(wn).z1;
		int x2 = worlds.get(wn).x2;
		int z2 = worlds.get(wn).z2;
		int h = worlds.get(wn).height;
		int d = worlds.get(wn).depth;
		String aw = worlds.get(wn).world_above;
		if (aw.isEmpty()) aw="None";
		String uw = worlds.get(wn).world_under;
		if (uw.isEmpty()) uw="None";
		return "&6"+wn+ "&e ["+x1+","+z1+" &6x&e "+x2+","+z2+"] &7H:"+h+"/"+"D:"+d+" &a"+aw+"/"+uw;
	}

}
