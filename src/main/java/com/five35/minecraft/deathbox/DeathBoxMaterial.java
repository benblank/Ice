package com.five35.minecraft.deathbox;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

final class DeathBoxMaterial extends Material {
	private static final DeathBoxMaterial INSTANCE = new DeathBoxMaterial();

	static DeathBoxMaterial getInstance() {
		return DeathBoxMaterial.INSTANCE;
	}

	private DeathBoxMaterial() {
		super(MapColor.airColor);

		this.setImmovableMobility();
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}

	@Override
	public boolean getCanBlockGrass() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
