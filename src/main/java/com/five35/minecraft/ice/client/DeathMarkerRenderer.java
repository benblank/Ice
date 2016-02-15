package com.five35.minecraft.ice.client;

import com.five35.minecraft.ice.DeathMarkerTileEntity;
import com.five35.minecraft.ice.Ice;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DeathMarkerRenderer extends TileEntitySpecialRenderer<DeathMarkerTileEntity> {
	private final DeathMarkerModel model = new DeathMarkerModel();
	private final SkinManager skinManager;

	public DeathMarkerRenderer(final SkinManager skinManager) {
		this.skinManager = skinManager;
	}

	@Override
	public void renderTileEntityAt(final DeathMarkerTileEntity ice, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
		ResourceLocation texture = DefaultPlayerSkin.getDefaultSkin(new UUID(0, 0));

		final GameProfile owner = ice.getOwner();

		if (owner == null) {
			Ice.getProxy().getLogger().warn("Rendering death marker with no owner at %d,%d,%d!", x, y, z);
		} else {
			final Map<Type, MinecraftProfileTexture> profileTextures = this.skinManager.loadSkinFromCache(owner);

			if (profileTextures.containsKey(Type.SKIN)) {
				texture = this.skinManager.loadSkin(profileTextures.get(Type.SKIN), Type.SKIN);
			} else {
				texture = DefaultPlayerSkin.getDefaultSkin(owner.getId());
			}
		}

		this.bindTexture(texture);

		GL11.glPushMatrix();

		final float age = ice.getAge() + partialTicks;
		final double bobHeight = 0.5 + Math.sin(age * Math.PI / 50) / 4;

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslated(x + 0.5, y + bobHeight, z + 0.5);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScaled(-1, -1, 1);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		this.model.render(null, 0, 0, 0, 3 * age, 0, 0.0625f);

		GL11.glPopMatrix();
	}
}
