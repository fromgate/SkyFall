package me.fromgate.skyfall;

import org.bukkit.entity.Player;


public class SFUtil extends FGUtilCore {
	SkyFall plg;

	public SFUtil (SkyFall plg, boolean vcheck, boolean savelng, String language, String devbukkitname, String version_name, String plgcmd, String px){
		super (plg, vcheck, savelng, language, devbukkitname, version_name, plgcmd, px);
		this.plg = plg;
		FillMSG();
		InitCmd();
		if (savelng) SaveMSG();
	}

	public void InitCmd(){
		this.cmds.clear();
		this.cmdlist = "";
		addCmd("help", "config", "hlp_helpcmd", "/skyfall help");
		addCmd("cfg", "config", "hlp_cfg", "/skyfall cfg");
		addCmd("reload", "config", "hlp_reload", "/skyfall reload");
		addCmd("clearworldlinks", "config", "hlp_clearworldlinks", "/skyfall clearworldlinks");
		addCmd("list", "config", "hlp_list", "/skyfall list");
		addCmd("area", "config", "hlp_setc", "/skyfall area [world] <x1,z1 x2,z2>");
		addCmd("under", "config", "hlp_setuw", "/skyfall under [world] <world>");
		addCmd("above", "config", "hlp_setaw", "/skyfall above [world] <world>");
		addCmd("height", "config", "hlp_height", "/skyfall height [world] <height level>");
		addCmd("depth", "config", "hlp_depth", "/skyfall depth [world] <depth level>");
		addCmd("locfall", "config", "hlp_tpfall", "/skyfall locfall [name]");
		addCmd("locclimb", "config", "hlp_tpclimb", "/skyfall locclimb [name]");
		addCmd("clearfall", "config", "hlp_clearfall", "/skyfall clearfall [world]");
		addCmd("clearclimb", "config", "hlp_clearclimb", "/skyfall clearclimb [world]");
		addCmd("fallin", "config", "hlp_fallin", "/skyfall fallin [world]");
		addCmd("climbin", "config", "hlp_climbin", "/skyfall climbin [world]");
		addCmd("fallrnd", "config", "hlp_fallrnd", "/skyfall fallrnd [world]");
		addCmd("climbrnd", "config", "hlp_climbrnd", "/skyfall climbrnd [world]");
		addCmd("linktime", "config", "hlp_linktime", "/skyfall linktime [world]");
	}

	public void FillMSG(){
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
		addMSG ("msg_locfall", "Added new fall-in location: %1%");
		addMSG ("msg_locclimb", "Added new climb-in location: %1%");
		addMSG ("msg_locfall2", "Added new fall-in location: %2% (%1%)");
		addMSG ("msg_locclimb2", "Added new climb-in location: %2% (%1%)");
		addMSG ("msg_clearfall", "All fall-in locations in world %1% was removed");
		addMSG ("msg_clearclimb", "All climb-in locations in world %1% was removed");
		addMSG ("msg_clearfallfailed", "Fall-in locations was not removed. Is world %1% exists?");
		addMSG ("msg_clearclimbfailed", "Climb-in locations was not removed. Is world %1% exists?");
		addMSG ("msg_climbin", "Climb-in teleporting at world %1% was %2%");
		addMSG ("msg_fallin", "Fall-in teleporting at world %1% was %2%");
		addMSG ("msg_climbinfailed", "Not possible to change climb-in teleporting state at world %1%");
		addMSG ("msg_fallinfailed", "Not possible to change fall-in teleporting state at world %1%");
		addMSG ("msg_fallrnd", "Random fall-in teleporting in world %1% was %2%");
		addMSG ("msg_climbrnd", "Random climb-in teleporting in world %1% was %2%");
		addMSG ("msg_fallrndfailed", "Not possible to change random fall-in teleporting state at world %1%");
		addMSG ("msg_climbrndfailed", "Not possible to change random climb-in teleporting state at world %1%");
		addMSG ("hlp_tpfall","%1% - to create fall-in teleport point");
		addMSG ("hlp_tpclimb","%1% - to create climb-in teleport point");
		addMSG ("hlp_clearfall","%1% - to remove all fall-in teleport point");
		addMSG ("hlp_clearclimb","%1% - to remove all climb-in teleport point");
		addMSG ("hlp_fallin","%1% - toggle fall-in teleportation in defined (or current world)");
		addMSG ("hlp_climbin","%1% - toggle climb-in teleportation in defined (or current world)");
		addMSG ("hlp_fallrnd","%1% - toggle random fall-in teleportation");
		addMSG ("hlp_climbrnd","%1% - toggle random climb-in teleportation");
		addMSG ("msg_fallmsgloc","You fell into the world %2% and find yourself at %1%");
		addMSG ("msg_climbmsgloc","You went up to the world %2% and find yourself at %1%");
		addMSG ("hlp_linktime","%1% - toggle time synchronization or synchronize time of current world with defined");
		addMSG ("msg_linktimeenabled","Synchronization at separated worlds is");
		addMSG ("msg_linktimelinked","Time at world %1% was synchronized to time of the world %2%");
		addMSG ("msg_linktimeunlinked","Time synchronization of world %1% was removed");
	}

	public void PrintCfg(Player p){
	    p.sendMessage("");
	    printMsg(p, "&6&l" + des.getName() + " v" + this.des.getVersion() + " &r&6| " + getMSG("cfg_configuration", '6'));
	    printEnDis(p, "cfg_showmsg", plg.showmsg);
	    printMSG(p, "cfg_fallradius", plg.fall_random_radius);
	    printMSG(p, "cfg_language", plg.language);
	    printEnDis(p, "cfg_vcheck", plg.vcheck);
	}

}
