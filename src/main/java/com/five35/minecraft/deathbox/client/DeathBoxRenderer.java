package com.five35.minecraft.deathbox.client;

import com.five35.minecraft.deathbox.DeathBoxTileEntity;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DeathBoxRenderer extends TileEntitySpecialRenderer {
	private final ModelSkeletonHead model = new ModelSkeletonHead(0, 0, 64, 32);
	private final SkinManager skinManager;

	public DeathBoxRenderer(final SkinManager skinManager) {
		this.skinManager = skinManager;
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileEntity, final double x, final double y, final double z, final float p_147500_8_) {
		final DeathBoxTileEntity deathBox = (DeathBoxTileEntity) tileEntity;
		final Map<Type, MinecraftProfileTexture> profileTextures = this.skinManager.func_152788_a(deathBox.getOwner());

		final ResourceLocation texture;

		if (profileTextures.containsKey(Type.SKIN)) {
			texture = this.skinManager.func_152792_a(profileTextures.get(Type.SKIN), Type.SKIN);
		} else {
			texture = AbstractClientPlayer.locationStevePng;
		}

		this.bindTexture(texture);

		GL11.glPushMatrix();

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScaled(-1, -1, 1);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		this.model.render(null, 0, 0, 0, 0, 0, 0.0625f);

		GL11.glPopMatrix();
	}
}
