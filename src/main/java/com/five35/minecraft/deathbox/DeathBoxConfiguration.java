package com.five35.minecraft.deathbox;

import java.io.File;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.common.config.Configuration;

public class DeathBoxConfiguration {
	private static enum SecurityType {
		NO, OWNER, TEAM, YES
	}

	private static SecurityType getSecurityType(final String value, final SecurityType fallback) {
		try {
			return SecurityType.valueOf(value.toUpperCase());
		} catch (final IllegalArgumentException ex) {
			final String message = String.format("Unrecognized security type '%s', defaulting to '%s'.", value, fallback);
			DeathBox.getLogger().warn(message, ex);

			return fallback;
		}
	}

	private static boolean hasAccess(final SecurityType securityType, final String ownerName, final EntityPlayer player) {
		if (securityType == SecurityType.NO) {
			return false;
		}

		if (securityType == SecurityType.YES) {
			return true;
		}

		if (ownerName.equals(player.getCommandSenderName())) {
			return true;
		}

		if (securityType == SecurityType.TEAM) {
			final Team ownerTeam = player.getWorldScoreboard().getPlayersTeam(ownerName);

			if (ownerTeam == null) {
				return false;
			}

			return ownerTeam.equals(player.getTeam());
		}

		return false;
	}

	private final SecurityType popping;
	private final SecurityType recovering;

	DeathBoxConfiguration(final File file) {
		final Configuration config = new Configuration(file);

		config.setCategoryComment("security", "The allowable values for these actions are 'no' (completely disabled), 'owner' (only the player whose death created the box), 'team' (anyone on the dying player's team), or 'yes' (anyone).");

		DeathBox.getLogger().debug("Reading 'security.popping'.");
		final String poppingValue = config.get("security", "popping", "team", "Whether death boxes can be 'popped' by hitting them.").getString();
		this.popping = DeathBoxConfiguration.getSecurityType(poppingValue, SecurityType.TEAM);

		DeathBox.getLogger().debug("Reading 'security.recovering'.");
		final String recoveringValue = config.get("security", "recovering", "owner", "Whether the contents of death boxes can be recovered by right-clicking them.").getString();
		this.recovering = DeathBoxConfiguration.getSecurityType(recoveringValue, SecurityType.OWNER);

		if (this.popping == SecurityType.NO && this.recovering == SecurityType.NO) {
			DeathBox.getLogger().warn("All actions disabled!  Death boxes cannot be removed without creative mode.  (That probably isn't what you want.)");
		}

		config.save();
	}

	boolean canPop(final String ownerName, final EntityPlayer player) {
		return DeathBoxConfiguration.hasAccess(this.popping, ownerName, player);
	}

	boolean canRecover(final String ownerName, final EntityPlayer player) {
		return DeathBoxConfiguration.hasAccess(this.recovering, ownerName, player);
	}
}
