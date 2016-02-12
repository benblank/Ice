package com.five35.minecraft.ice;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class IceBlock extends Block implements ITileEntityProvider {
	IceBlock() {
		super(IceMaterial.getInstance());

		this.disableStats();
		this.setHardness(-1); // == unbreakable
	}

	@Override
	public boolean canEntityDestroy(final IBlockAccess world, final BlockPos position, final Entity entity) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(final IBlockAccess world, final BlockPos position, final EntityPlayer player) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final BlockPos position) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new IceTileEntity();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(final World world, final BlockPos position, final IBlockState state) {
		return null;
	}

	@Override
	public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final BlockPos position, EntityPlayer player) {
		return null;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos position, final IBlockState state, final EntityPlayer player, final EnumFacing side, final float xOffset, final float yOffset, final float zOffset) {
		if (world.isRemote) {
			return false;
		}

		final TileEntity tileEntity = world.getTileEntity(position);

		if (tileEntity instanceof IceTileEntity) {
			final IceTileEntity ice = (IceTileEntity) tileEntity;
			final GameProfile owner = ice.getOwner();

			if (Ice.getProxy().getConfig().canRecover(owner, player)) {
				final String message = String.format("%s is recovering ice left by %s.", owner.getName(), player.getCommandSenderEntity().getName());
				Ice.getProxy().getLogger().info(message);

				ice.recover(player);

				return true;
			}

			final String message = String.format("%s does not have permission to recover ice left by %s.", owner.getName(), player.getCommandSenderEntity().getName());
			Ice.getProxy().getLogger().info(message);
		}

		return false;
	}

	@Override
	public void onBlockClicked(final World world, final BlockPos position, final EntityPlayer player) {
		if (world.isRemote) {
			return;
		}

		final TileEntity tileEntity = world.getTileEntity(position);

		if (tileEntity instanceof IceTileEntity) {
			final IceTileEntity ice = (IceTileEntity) tileEntity;
			final GameProfile owner = ice.getOwner();

			if (Ice.getProxy().getConfig().canPop(owner, player)) {
				final String message = String.format("%s is popping ice left by %s.", owner.getName(), player.getCommandSenderEntity());
				Ice.getProxy().getLogger().info(message);

				ice.pop();
			} else {
				final String message = String.format("%s does not have permission to pop ice left by %s.", owner.getName(), player.getCommandSenderEntity());
				Ice.getProxy().getLogger().info(message);
			}
		}
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos position, final Explosion explosion) {
		// Ice is unexplodable!
	}

	@Override
	public boolean removedByPlayer(final World world, final BlockPos position, final EntityPlayer player, final boolean willHarvest) {
		return false;
	}
}