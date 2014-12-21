package com.five35.minecraft.deathbox;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DeathBoxBlock extends Block implements ITileEntityProvider {
	DeathBoxBlock() {
		super(DeathBoxMaterial.getInstance());

		this.disableStats();
		this.setHardness(-1); // == unbreakable
	}

	@Override
	public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z, final Entity entity) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(final EntityPlayer player, final int meta) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final int x, final int y, final int z) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new DeathBoxTileEntity();
	}

	@Override
	public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
		// TODO Auto-generated method stub
		return super.getDrops(world, x, y, z, metadata, fortune);
	}

	@Override
	public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z) {
		return null;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float xOffset, final float yOffset, final float zOffset) {
		// if !config.re-equip return false

		// if box.owner == player or config.insecure re-equip and return true

		return false;
	}

	@Override
	public void onBlockClicked(final World world, final int x, final int y, final int z, final EntityPlayer player) {
		// if !config.punchable return

		// if box.owner == player or config.insecure drop items
	}

	@Override
	public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion) {
		// DeathBox is unexplodable!
	}

	@Override
	public void registerBlockIcons(final IIconRegister p_149651_1_) {
		// TODO Auto-generated method stub
		super.registerBlockIcons(p_149651_1_);
	}

	@Override
	public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z) {
		return false;
	}
}
