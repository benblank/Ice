package com.five35.minecraft.ice.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class IceModel extends ModelBase {
	private final ModelRenderer head;
	private final ModelRenderer hat;

	public IceModel() {
		this.textureHeight = 64;
		this.textureWidth = 64;

		this.head = new ModelRenderer(this);
		this.head.addBox(-4, -4, -4, 8, 8, 8);

		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.addBox(-4, -4, -4, 8, 8, 8, 0.25f);
	}

	@Override
	public void render(final Entity entity, final float x, final float y, final float z, final float yaw, final float pitch, final float scale) {
		this.setRotationAngles(x, y, z, yaw, pitch, scale, entity);
		this.head.render(scale);
		this.hat.render(scale);
	}

	@Override
	public void setRotationAngles(final float x, final float y, final float z, final float yaw, final float pitch, final float scale, final Entity entity) {
		this.head.rotateAngleX = (float) (pitch * Math.PI / 180);
		this.head.rotateAngleY = (float) (yaw * Math.PI / 180);

		this.hat.rotateAngleX = this.head.rotateAngleX;
		this.hat.rotateAngleY = this.head.rotateAngleY;
	}
}
