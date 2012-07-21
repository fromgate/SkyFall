package fromgate.skyfall;

import org.bukkit.entity.Player;


public class FGUtil extends FGUtilCore {
	SkyFall plg;



	public FGUtil (SkyFall plg, boolean vcheck, boolean savelng, String language, String devbukkitname, String version_name, String plgcmd, String px){
		super (plg, vcheck, savelng, language, devbukkitname, version_name, plgcmd, px);
		
		this.plg = plg;

		FillMSG();
		InitCmd();
		if (savelng) SaveMSG();

	}

	public void InitCmd(){

		this.cmds.clear();
		this.cmdlist = "";

		AddCmd("help", "config", MSG("hlp_helpcmd", "/skyfall help"));
		AddCmd("cfg", "config", MSG("hlp_cfg", "/skyfall cfg"));
		AddCmd("reload", "config", MSG("hlp_reload", "/skyfall reload"));
		AddCmd("clearworldlinks", "config", MSG("hlp_clearworldlinks", "/skyfall clearworldlinks"));

		AddCmd("list", "config", MSG("hlp_list", "/skyfall list"));
		
		AddCmd("area", "config", MSG("hlp_setc", "/skyfall area [world] <x1,z1 x2,z2>"));
		AddCmd("under", "config", MSG("hlp_setuw", "/skyfall under [world] <world>"));
		AddCmd("above", "config", MSG("hlp_setaw", "/skyfall above [world] <world>"));
		
		
		AddCmd("height", "config", MSG("hlp_height", "/skyfall height [world] <height level>"));
		AddCmd("depth", "config", MSG("hlp_depth", "/skyfall depth [world] <depth level>"));
		
//		AddCmd("add", "config", MSG("hlp_addcmd", "/skyfall add"));
	}

	public void FillMSG(){
		//msg.clear();
		//msglist="";
		addMSG ("disabled","disabled");
		msg.put("disabled", "&c"+msg.get("disabled"));
		addMSG ("enabled","enabled");
		msg.put("enabled", "&2"+msg.get("enabled"));
		addMSG ("cmd_unknown", "Unknown command: %1%");
		addMSG ("hlp_help", "Help");
		addMSG ("hlp_thishelp", "%1% - display help page");
		addMSG ("hlp_execcmd", "%1% - execute command ");
		addMSG ("hlp_typecmd", "Type %1% to get additional help about command");
		addMSG ("hlp_cfg", "%1% - display personal settings");
		addMSG ("hlp_reload", "%1% - reload configuration");
		addMSG ("hlp_clearworldlinks", "%1% - clear all world links");
		addMSG ("hlp_height", "%1% - set worlds height");
		addMSG ("hlp_depth", "%1% - set worlds depth");
		addMSG ("hlp_commands", "Commands:");
		addMSG ("hlp_cmdparam_command", "command");
		addMSG ("hlp_cmdparam_parameter", "parameter");
		addMSG ("hlp_cmdparam_player", "player");
		addMSG ("msg_worldunknown", "World unknown: %1%");
		addMSG ("msg_wrongcoord", "Wrong coordinate: %1%");
		addMSG ("msg_climbmsg", "You went up to the world %1%");
		addMSG ("msg_fallmsg", "You fell into the world %1%");
		addMSG ("msg_list", "List of linked worlds:");
		addMSG ("msg_reloaded", "Configuration reloaded");
		addMSG ("msg_wuadd", "World %1% linked with under-world %2%");
		addMSG ("msg_wuadd_empty", "World %1% has no linked under-worlds");
		addMSG ("msg_waadd", "World %1% linked with above-world %2%");
		addMSG ("msg_waadd_empty", "World %1% has no linked under-worlds");
		addMSG ("hlp_list", "%1% - list all linked worlds");
		addMSG ("hlp_setc", "%1% - define world's perimeter coordinates");
		addMSG ("hlp_setuw", "%1% - define under-world for defined (or current) world");
		addMSG ("hlp_setaw", "%1% - define above-world for defined (or current) world");
		addMSG ("cfg_configuration", "Configuration");
		addMSG ("cfg_showmsg", "Show fall/climb message");
		addMSG ("cfg_fallradius", "Randomize falling coordinates with radius: %1%");
		addMSG ("cfg_language", "Language: %1%");
		addMSG ("cfg_vcheck", "Check updates: %1%");
		addMSG ("msg_worldcoord", "Perimeter coordinates of world %1% set to [%2%x%3%]");
		addMSG ("msg_clearworldlinks", "Worlds informations are reinitialized");
		addMSG ("msg_height", "Height of world %1% was set to %2%");
		addMSG ("msg_depth", "Depth of world %1% was set to %2%");
		addMSG ("msg_wrongheight", "Wrong number: %1%");
		addMSG ("msg_wrongdepth", "Wrong number: %1%");
	}

	public void PrintCfg(Player p){
	    p.sendMessage("");
	    PrintMsg(p, "&6&l" + des.getName() + " v" + this.des.getVersion() + " &r&6| " + MSG("cfg_configuration", '6'));
	    PrintEnDis(p, "cfg_showmsg", plg.showmsg);
	    PrintMSG(p, "cfg_fallradius", plg.fall_random_radius);
	    PrintMSG(p, "cfg_language", plg.language);
	    PrintEnDis(p, "cfg_vcheck", plg.vcheck);
	}

}
