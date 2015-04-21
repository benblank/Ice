package com.five35.minecraft.deathbox.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class DeathBoxModel extends ModelBase {
	private final ModelRenderer renderer;

	public DeathBoxModel() {
		this.renderer = new ModelRenderer(this);
		this.renderer.addBox(-4, -4, -4, 8, 8, 8);
	}

	@Override
	public void render(final Entity entity, final float x, final float y, final float z, final float yaw, final float pitch, final float scale) {
		this.setRotationAngles(x, y, z, yaw, pitch, scale, entity);
		this.renderer.render(scale);
	}
}
