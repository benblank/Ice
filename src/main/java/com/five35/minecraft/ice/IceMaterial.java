package com.five35.minecraft.ice;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

final class IceMaterial extends Material {
	private static final IceMaterial INSTANCE = new IceMaterial();

	static IceMaterial getInstance() {
		return IceMaterial.INSTANCE;
	}

	private IceMaterial() {
		super(MapColor.airColor);

		this.setImmovableMobility();
	}

	@Override
	public boolean blocksLight() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
