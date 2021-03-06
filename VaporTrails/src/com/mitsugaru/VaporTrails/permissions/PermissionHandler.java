package com.mitsugaru.VaporTrails.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.mitsugaru.VaporTrails.VaporTrails;

/**
 * Class to handle permission node checks. Mostly only to support PEX natively,
 * due to SuperPerm compatibility with PEX issues.
 * 
 * @author Mitsugaru
 * 
 */
public class PermissionHandler
{
	private static Permission perm;
	private static boolean hasVault;

	public static void init(VaporTrails plugin)
	{
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null)
		{
			hasVault = true;
			RegisteredServiceProvider<Permission> permissionProvider = plugin
					.getServer()
					.getServicesManager()
					.getRegistration(
							net.milkbowl.vault.permission.Permission.class);
			if (permissionProvider != null)
			{
				perm = permissionProvider.getProvider();
			}
		}
		else
		{
			hasVault = false;
		}
	}

	public static boolean has(CommandSender sender, PermissionNode node)
	{
		return has(sender, node.getNode());
	}

	/**
	 * 
	 * @param CommandSender
	 *            that sent command
	 * @param Permission
	 *            node to check, as String
	 * @return true if sender has the node, else false
	 */
	public static boolean has(CommandSender sender, String node)
	{
		// Use vault if we have it
		if (hasVault)
		{
			return perm.has(sender, node);
		}
		// If not using PEX / Vault, OR if sender is not a player (in PEX only
		// case)
		// Attempt to use SuperPerms or check if they're op
		if (sender.isOp() || sender.hasPermission(node))
		{
			return true;
		}
		// Else, they don't have permission
		return false;
	}
}
