package com.five35.minecraft.deathbox;

import java.io.File;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class DeathBoxConfiguration {
	private static enum SecurityType {
		NO, OWNER, TEAM, YES
	}

	private static SecurityType getSecurityType(final String value, final SecurityType fallback) {
		try {
			return SecurityType.valueOf(value.toUpperCase());
		} catch (final IllegalArgumentException ex) {
			final String message = String.format("Unrecognized security type '', defaulting to ''.");

			DeathBox.INSTANCE.logger.warn(message, ex);

			return fallback;
		}
	}

	private static boolean hasAccess(final SecurityType securityType, final UUID ownerUuid, final EntityPlayer player) {
		if (securityType == SecurityType.NO) {
			return false;
		}

		if (securityType == SecurityType.YES) {
			return true;
		}

		final UUID playerUuid = EntityPlayer.func_146094_a(player.getGameProfile());

		if (ownerUuid.equals(playerUuid)) {
			return true;
		}

		if (securityType == SecurityType.TEAM) {
			// TODO: figure out how to check teams
		}

		return false;
	}

	private final SecurityType popping;
	private final SecurityType recovering;

	DeathBoxConfiguration(final File file) {
		final Configuration config = new Configuration(file);

		config.setCategoryComment("security", "The allowable values for these actions are 'no' (completely disabled), 'owner' (only the player whose death created the box), 'team' (anyone on the dying player's team), or 'yes' (anyone).");

		DeathBox.INSTANCE.logger.debug("Reading 'security.popping'.");
		final String poppingValue = config.get("security", "popping", "team", "Whether death boxes can be 'popped' by hitting them.").getString();
		this.popping = DeathBoxConfiguration.getSecurityType(poppingValue, SecurityType.TEAM);

		DeathBox.INSTANCE.logger.debug("Reading 'security.recovering'.");
		final String recoveringValue = config.get("security", "recovering", "owner", "Whether the contents of death boxes can be recovered by right-clicking them.").getString();
		this.recovering = DeathBoxConfiguration.getSecurityType(recoveringValue, SecurityType.OWNER);

		config.save();
	}

	boolean canPop(final UUID ownerUuid, final EntityPlayer player) {
		return DeathBoxConfiguration.hasAccess(this.popping, ownerUuid, player);
	}

	boolean canRecover(final UUID ownerUuid, final EntityPlayer player) {
		return DeathBoxConfiguration.hasAccess(this.recovering, ownerUuid, player);
	}
}
