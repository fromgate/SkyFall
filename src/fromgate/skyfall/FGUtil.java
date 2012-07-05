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


import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* +    1. Проверка версий
 * +	2. Процедуры для обработчика комманда (перечень, печать хелпа -)
 */


public class FGUtil {
	SkyFall plg;
	
	
	//конфигурация утилит
	public String px = ChatColor.translateAlternateColorCodes('&', "&3[DT]&f ");
	String pxlog = "[DT] "; // префикс для лог файла
	
	private String permprefix="skyfall.";
	private boolean version_check = false; // включить после заливки на девбукит
	private String version_check_url = "http://dev.bukkit.org/server-mods/skyfall/files.rss";
	private String version_name = "SkyFall"; // идентификатор на девбукките (всегда должен быть такой!!!)
	private String version_info_perm = permprefix+"config"; // кого оповещать об обнволениях
	private String language="english";
	
	// Сообщения+перевод
	private HashMap<String,String> msg = new HashMap<String,String>(); //массив сообщений
	private char c1 = 'a'; //цвет 1 (по умолчанию для текста)
	private char c2 = '2'; //цвет 2 (по умолчанию для значений)
	private String msglist ="";
	
	private HashMap<String,Cmd> cmds = new HashMap<String,Cmd>();
	
	private String cmdlist ="";
	PluginDescriptionFile des;
	private double version_current=0;
	private double version_new=0;
	private String version_new_str="unknown";
	private Logger log = Logger.getLogger("Minecraft");
	Random random = new Random ();
	
	
	
	public FGUtil(SkyFall plg, boolean vcheck, boolean savelng, String language){
		this.plg = plg;
		this.des = plg.getDescription();
		this.version_current = Double.parseDouble(des.getVersion().replaceFirst("\\.", "").replace("/", ""));
		this.version_check=vcheck;
		this.language = language;
		this.LoadMSG();
		if (savelng) this.SaveMSG(); //для получения списка
		this.InitCmd();
	}

	
	/*
	 * Проверка версии 
	 */
	public void SetVersionCheck (boolean vc){
		this.version_check = vc;
	}
	
	
	// Вставить вызов в обработчик PlayerJoinEvent
	public void UpdateMsg (Player p){
		if (version_check){
			if (p.hasPermission(this.version_info_perm)){
				version_new = getNewVersion (version_current);
				if (version_new>version_current){
					PrintMSG(p, "msg_outdated","&6"+des.getName()+" v"+des.getVersion(),'e','6');
					PrintMSG(p,"msg_pleasedownload",version_new_str,'e','6');
					PrintMsg(p, "&3"+version_check_url.replace("files.rss", ""));
				}
			}
		}
	}

	// Вставить в обработчик onEnable
	public void UpdateMsg (){
		if (version_check){
			version_new = getNewVersion (version_current);
			if (version_new>version_current){
				log.info(pxlog+des.getName()+" v"+des.getVersion()+" is outdated! Recommended version is v"+version_new_str);
				log.info(pxlog+version_check_url.replace("files.rss", ""));
			}			
		}
	}
	
	private double getNewVersion(double currentVersion){
		if (version_check){
			try {
				URL url = new URL(version_check_url);
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
				doc.getDocumentElement().normalize();
				NodeList nodes = doc.getElementsByTagName("item");
				Node firstNode = nodes.item(0);
				if (firstNode.getNodeType() == 1) {
					Element firstElement = (Element)firstNode;
					NodeList firstElementTagName = firstElement.getElementsByTagName("title");
					Element firstNameElement = (Element)firstElementTagName.item(0);
					NodeList firstNodes = firstNameElement.getChildNodes();
					version_new_str = firstNodes.item(0).getNodeValue().replace(version_name+" v", "").trim();
					return Double.parseDouble(version_new_str.replaceFirst("\\.", "").replace("/", ""));
				}
			}
			catch (Exception e) {
			}
		}
		return currentVersion;
	}
	
	
	public boolean isIdInList (int id, String str){
		String [] ln = str.split(",");
		if (ln.length>0) {
			for (int i = 0; i<ln.length; i++)
				if (Integer.parseInt(ln[i])==id) return true;
		}		
		return false;
	}

	
	
	/*
	 * Процедуры для обработчика комманд
	 * 
	 */
	
	public void AddCmd (String cmd, String perm, String desc){
		cmds.put(cmd, new Cmd(this.permprefix+perm,desc));
		if (cmdlist.isEmpty()) cmdlist = cmd;
		else cmdlist = cmdlist+", "+cmd;
	}
	
	public void InitCmd(){
		cmds.clear();
		cmdlist = "";

		AddCmd("help", "config",MSG("hlp_helpcmd","/skyfall help"));
		AddCmd("cfg", "config",MSG("hlp_cfg","/skyfall cfg"));
		AddCmd("reload", "config",MSG("hlp_reload","/skyfall reload"));
		
		AddCmd("list", "config",MSG("hlp_list","/skyfall list"));
		AddCmd("setc", "config",MSG("hlp_setc","/skyfall setc [world] <x1,z1 x2,z2>"));
		AddCmd("setuw", "config",MSG("hlp_setuw","/skyfall setuw <world>"));
		AddCmd("setaw", "config",MSG("hlp_setaw","/skyfall setaw <world>"));
		AddCmd("add", "config",MSG("hlp_addcmd","/skyfall add"));
		
	}
	
	
	
	public boolean CheckCmdPerm (Player p, String cmd){
		return ((cmds.containsKey(cmd.toLowerCase()))&&
				(cmds.get(cmd.toLowerCase()).perm.isEmpty()||((!cmds.get(cmd.toLowerCase()).perm.isEmpty())&&p.hasPermission(cmds.get(cmd.toLowerCase()).perm))));
	}
	
	public String getCmdList(){
		return cmdlist;
	}
	
	public boolean equalCmdPerm (String cmd, String perm){
		return (cmds.containsKey(cmd.toLowerCase())&&cmds.get(cmd.toLowerCase()).perm.equalsIgnoreCase(perm));
	}
	
	public class Cmd {
		String perm;
		String desc;
		public Cmd (String perm, String desc){
			this.perm = perm;
			this.desc = desc;
		}
	} 
	
	
	/*
	 * Разные полезные процедурки 
	 * 
	 */
	public void PrintMsg(Player p, String msg){
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public void PrintMsgPX(Player p, String msg){
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', px+msg));
	}


	public void BC (String msg){
		plg.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', px+msg));
	}
	
	
	/*
	 * Перевод
	 * 
	 */
	public void LoadMSG(){
		String lngfile = this.language+".lng";
		try {
			YamlConfiguration lng = new YamlConfiguration();
			File f = new File (plg.getDataFolder()+File.separator+lngfile);
			if (f.exists()) lng.load(f);
			
			FillMSG(lng);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void addMSG(YamlConfiguration cfg,String key, String txt){
		msg.put(key, ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', cfg.getString(key,txt))));
		if (msglist.isEmpty()) msglist=key;
		else msglist=msglist+","+key;
	}

	
	///// процедура для формирования файла
	public void SaveMSG(){
		String lngfile = this.language+".lng";
		String [] keys = msglist.split(",");
		try {
			File f = new File (plg.getDataFolder()+File.separator+lngfile);
			if (!f.exists()) f.createNewFile();
			YamlConfiguration cfg = new YamlConfiguration();
			for (int i = 0; i<keys.length;i++)
				cfg.set(keys[i], msg.get(keys[i]));
			cfg.save(f);
		} catch (Exception e){
			e.printStackTrace();
		}
	} 
	
	public String MSG(String id){
		return MSG (id,"",this.c1, this.c2);
	}
	
	public String MSG(String id, char c){
		return MSG (id,"",c, this.c2);
	}

	public String MSG(String id, String keys){
		return MSG (id,keys,this.c1, this.c2);
	}

	public String MSG(String id, String keys, char c){
		return MSG (id,keys,this.c1, c);
	}

	public String MSG(String id, String keys, char c1, char c2){
		String str = "&4Unknown message ("+id+")";
		if (msg.containsKey(id)){
			str = "&"+c1+msg.get(id);
			String ln[] = keys.split(";");
			if (ln.length>0)
				for (int i = 0; i<ln.length;i++)
					str = str.replace("%"+Integer.toString(i+1)+"%", "&"+c2+ln[i]+"&"+c1);
		} 
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	
	public void PrintMSG (Player p, String msg_key, int key){
		p.sendMessage(MSG (msg_key, Integer.toString(key), this.c1, this.c2));
	}
	
	public void PrintMSG (Player p, String msg_key, String keys){
		p.sendMessage(MSG (msg_key, keys, this.c1, this.c2));
	}

	public void PrintMSG (Player p, String msg_key, String keys, char c1, char c2){
		p.sendMessage(MSG (msg_key, keys, c1, c2));
	}

	public void PrintMSG (Player p, String msg_key, char c1){
		p.sendMessage(MSG (msg_key, c1));
	}

	public void PrintMSG (Player p, String msg_key){
		p.sendMessage(MSG (msg_key));
	}
	
	public void PrintHLP (Player p){
		PrintMsg(p, "&6&l"+version_name+" v"+des.getVersion()+" &r&6| "+MSG("hlp_help",'6'));
		PrintMSG(p, "hlp_helpcmd","/skyfall help");
		PrintMSG(p, "hlp_helpexec","/skyfall <"+MSG("hlp_cmdparam_command",'2')+"> ["+MSG("hlp_cmdparam_parameter",'2')+"]");
		PrintMSG(p, "hlp_helpcmdlist","/dogtag help <"+MSG("hlp_cmdparam_command",'2')+">");
		PrintMsg(p, MSG("hlp_commands")+" &2"+getCmdList());
	}
	
	
	public void PrintCfg (Player p){
		p.sendMessage("");
		PrintMsg (p, "&6&l"+version_name+" v"+des.getVersion()+" &r&6| "+MSG("cfg_configuration",'6'));
		PrintEnDis(p,"cfg_showmsg",plg.showmsg); //Show fall/climb message: %1%
		PrintMSG (p,"cfg_fallradius",plg.fall_random_radius); //randomize falling coordinates with radius...
		PrintMSG (p,"cfg_language",plg.language);
		PrintEnDis(p,"cfg_vcheck",plg.vcheck); //Check updates?
		PrintMSG (p,"cfg_curcfg",'6');
		PrintMsg(p, plg.PlayerConfigToStr(p.getName()));
		
	}
	
	
	public void PrintHLP (Player p, String cmd){
		if (cmds.containsKey(cmd)){
			PrintMsg(p, "&6&l"+version_name+" v"+des.getVersion()+" &r&6| "+MSG("hlp_help",'6'));
			PrintMsg(p, cmds.get(cmd).desc);
		} else PrintMSG(p,"cmd_unknown",cmd);
	}
	
	public String EnDis (boolean b){
		if (b) return MSG ("enabled");
		else return MSG ("disabled");
	}

	public void PrintEnDis (Player p, String msg_id, boolean b){
		p.sendMessage(MSG (msg_id)+": "+EnDis(b));
	}
	
	
	public void FillMSG(YamlConfiguration cfg){
		msg.clear();
		msglist="";
		addMSG (cfg, "disabled","disabled");
		msg.put("disabled", "&c"+msg.get("disabled"));
		addMSG (cfg, "enabled","enabled");
		msg.put("enabled", "&2"+msg.get("enabled"));
		addMSG (cfg, "cmd_wrong", "Unknown command or you have not enought permissions");
		addMSG (cfg, "cmd_unknown", "Unknown command: %1%");
		addMSG (cfg, "hlp_help", "Help");
		addMSG (cfg, "hlp_helpexec", "%1% - execute command ");
		addMSG (cfg, "hlp_helpcmd", "%1% - to get additional help");
		addMSG (cfg, "hlp_cfg", "%1% - display personal settings");
		addMSG (cfg, "hlp_reload", "%1% - reload configuration");
		addMSG (cfg, "hlp_commands", "Commands:");
		addMSG (cfg, "hlp_cmdparam_command", "command");
		addMSG (cfg, "hlp_cmdparam_parameter", "parameter");
		addMSG (cfg, "hlp_cmdparam_player", "player");
		addMSG (cfg, "msg_wladded", "World link added: %1%");
		addMSG (cfg, "msg_wlundefined", "Unable to add world link");
		addMSG (cfg, "msg_worldunknown", "World unknown: %1%");
		addMSG (cfg, "msg_wrongcoord", "Wrong coordinate: %1%");
		addMSG (cfg, "msg_climbmsg", "You went up to the world %1%");
		addMSG (cfg, "msg_fallmsg", "You fell into the world %1%");
		addMSG (cfg, "msg_list", "List of linked worlds:");
		addMSG (cfg, "msg_reloaded", "Configuration reloaded");
		addMSG (cfg, "msg_pcfgerror", "Player config is undefined");
		addMSG (cfg, "msg_pcfg", "World: %1% Under: %2% Above: %3%");
		addMSG (cfg, "msg_wuadd", "Under-world link defined");
		addMSG (cfg, "msg_wuadd_empty", "Under-world link undefined");
		addMSG (cfg, "msg_waadd", "Above-world link defined");
		addMSG (cfg, "msg_waadd_empty", "Above-world link undefined");
		addMSG (cfg, "msg_coordadd", "World coordinates defined");
		addMSG (cfg, "msg_warnlinks", "World %1% is not linked with other worlds, and can be used as target world only");
		addMSG (cfg, "hlp_list", "%1% - list all linked worlds");
		addMSG (cfg, "hlp_setc", "%1% - define current world settings");
		addMSG (cfg, "hlp_setuw", "%1% - define under-world for current settings");
		addMSG (cfg, "hlp_setaw", "%1% - define above-world for current settings");
		addMSG (cfg, "hlp_add", "%1% - add new world link based to current settings");
		addMSG (cfg, "cfg_configuration", "Configuration");
		addMSG (cfg, "cfg_showmsg", "Show fall/climb message");
		addMSG (cfg, "cfg_fallradius", "Randomize falling coordinates with radius: %1%");
		addMSG (cfg, "cfg_language", "Language: %1%");
		addMSG (cfg, "cfg_vcheck", "Check updates: %1%");
		addMSG (cfg, "cfg_curcfg", "Personal settings:");		
		//addMSG (cfg, "", "");
	}

}


