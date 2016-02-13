package com.five35.minecraft.ice;

import com.mojang.authlib.GameProfile;
import java.io.File;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.common.config.Configuration;

public class IceConfiguration {
	private static enum SecurityType {
		NO, OWNER, TEAM, YES
	}

	private static SecurityType getSecurityType(final String value, final SecurityType fallback) {
		try {
			return SecurityType.valueOf(value.toUpperCase());
		} catch (final IllegalArgumentException ex) {
			final String message = String.format("Unrecognized security type '%s', defaulting to '%s'.", value, fallback);
			Ice.getProxy().getLogger().warn(message, ex);

			return fallback;
		}
	}

	private static boolean hasAccess(final SecurityType securityType, final GameProfile owner, final EntityPlayer player) {
		if (securityType == SecurityType.NO) {
			return false;
		}

		if (securityType == SecurityType.YES) {
			return true;
		}

		if (owner.getId().equals(player.getGameProfile().getId())) {
			return true;
		}

		if (securityType == SecurityType.TEAM) {
			final Team ownerTeam = player.getWorldScoreboard().getPlayersTeam(owner.getName());

			if (ownerTeam == null) {
				return false;
			}

			return ownerTeam.isSameTeam(player.getTeam());
		}

		return false;
	}

	private final SecurityType popping;
	private final SecurityType recovering;

	IceConfiguration(final File file) {
		final Configuration config = new Configuration(file);

		config.setCategoryComment("security", "The allowable values for these actions are 'no' (completely disabled), 'owner' (only the player whose death created the ice), 'team' (anyone on the dying player's team), or 'yes' (anyone).");

		Ice.getProxy().getLogger().debug("Reading 'security.popping'.");
		final String poppingValue = config.get("security", "popping", "team", "Whether death markers can be 'popped' by hitting them.").getString();
		this.popping = IceConfiguration.getSecurityType(poppingValue, SecurityType.TEAM);

		Ice.getProxy().getLogger().debug("Reading 'security.recovering'.");
		final String recoveringValue = config.get("security", "recovering", "owner", "Whether the contents of death markers can be recovered by right-clicking them.").getString();
		this.recovering = IceConfiguration.getSecurityType(recoveringValue, SecurityType.OWNER);

		if (this.popping == SecurityType.NO && this.recovering == SecurityType.NO) {
			Ice.getProxy().getLogger().warn("All actions disabled!  Death markers cannot be removed without creative mode.  (That probably isn't what you want.)");
		}

		config.save();
	}

	boolean canPop(final GameProfile owner, final EntityPlayer player) {
		return IceConfiguration.hasAccess(this.popping, owner, player);
	}

	boolean canRecover(final GameProfile owner, final EntityPlayer player) {
		return IceConfiguration.hasAccess(this.recovering, owner, player);
	}
}
