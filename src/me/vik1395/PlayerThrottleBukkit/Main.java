package me.vik1395.PlayerThrottleBukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
/*

Author: Vik1395
Project: PlayerThrottleBukkit

Copyright 2014

Licensed under Creative CommonsAttribution-ShareAlike 4.0 International Public License (the "License");
You may not use this file except in compliance with the License.

You may obtain a copy of the License at http://creativecommons.org/licenses/by-sa/4.0/legalcode

You may find an abridged version of the License at http://creativecommons.org/licenses/by-sa/4.0/
 */

public class Main extends JavaPlugin implements Listener
{
	private HashMap<String, String> throttle1 = new HashMap<String, String>();
	private String msg = "";
	private int maxconn = 1;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		getLogger().info("PlayerThrottleBukkit has successfully started!");
		getLogger().info("Created by Vik1395");
		msg = getConfig().getString("Kick Message");
		maxconn = Integer.parseInt(getConfig().getString("Allowed Connections"));
	}
	
	@EventHandler
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent apple)
	{
		String ip = apple.getAddress().getHostAddress();
		boolean first = true;
		
		for(Map.Entry<String, String> entry1 : throttle1.entrySet()) 
		{
			if(entry1.getKey().equals(ip))
			{
				first = false;
				int s = Integer.parseInt(throttle1.get(ip));
				
				if(s>=maxconn)
				{
					apple.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg);
				}
				
				else
				{
					s = s+1;
					String num = "" + s;
					throttle1.remove(entry1.getKey());
					throttle1.put(ip, num);
				}
			}
		}
		if(first==true)
		{
			String num = "1";
			throttle1.put(ip, num);
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent pqe)
	{
		String ipn = "" + pqe.getPlayer().getAddress().getAddress();
		String ip = ipn.substring(1,ipn.length());
		
		for(Map.Entry<String, String> entry1 : throttle1.entrySet()) 
		{
			if(entry1.getKey().equals(ip))
			{
				int s = Integer.parseInt(throttle1.get(ip));
				
				if(s==1)
				{
					throttle1.remove(entry1.getKey());
				}
				
				else
				{
					s = s-1;
					String num = "" + s;
					throttle1.remove(entry1.getKey());
					throttle1.put(ip, num);
				}
			}
		}
		
	}
}
