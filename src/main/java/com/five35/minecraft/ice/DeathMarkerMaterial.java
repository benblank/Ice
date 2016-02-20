package com.five35.minecraft.ice;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

final class DeathMarkerMaterial extends Material {
	private static final DeathMarkerMaterial INSTANCE = new DeathMarkerMaterial();

	static DeathMarkerMaterial getInstance() {
		return DeathMarkerMaterial.INSTANCE;
	}

	private DeathMarkerMaterial() {
		super(MapColor.airColor);

		this.setImmovableMobility();
	}

	@Override
	public boolean blocksLight() {
		return false;
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
