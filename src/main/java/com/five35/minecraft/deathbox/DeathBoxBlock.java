package com.five35.minecraft.deathbox;

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

public class DeathBoxBlock extends Block implements ITileEntityProvider {
	DeathBoxBlock() {
		super(DeathBoxMaterial.getInstance());

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
		return new DeathBoxTileEntity();
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

		if (tileEntity instanceof DeathBoxTileEntity) {
			final DeathBoxTileEntity deathBox = (DeathBoxTileEntity) tileEntity;
			final GameProfile owner = deathBox.getOwner();

			if (DeathBox.getProxy().getConfig().canRecover(owner, player)) {
				final String message = String.format("%s is recovering a death box left by %s.", owner.getName(), player.getCommandSenderEntity().getName());
				DeathBox.getProxy().getLogger().info(message);

				deathBox.recover(player);

				return true;
			}

			final String message = String.format("%s does not have permission to recover a death box left by %s.", owner.getName(), player.getCommandSenderEntity().getName());
			DeathBox.getProxy().getLogger().info(message);
		}

		return false;
	}

	@Override
	public void onBlockClicked(final World world, final BlockPos position, final EntityPlayer player) {
		if (world.isRemote) {
			return;
		}

		final TileEntity tileEntity = world.getTileEntity(position);

		if (tileEntity instanceof DeathBoxTileEntity) {
			final DeathBoxTileEntity deathBox = (DeathBoxTileEntity) tileEntity;
			final GameProfile owner = deathBox.getOwner();

			if (DeathBox.getProxy().getConfig().canPop(owner, player)) {
				final String message = String.format("%s is popping a death box left by %s.", owner.getName(), player.getCommandSenderEntity());
				DeathBox.getProxy().getLogger().info(message);

				deathBox.pop();
			} else {
				final String message = String.format("%s does not have permission to pop a death box left by %s.", owner.getName(), player.getCommandSenderEntity());
				DeathBox.getProxy().getLogger().info(message);
			}
		}
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos position, final Explosion explosion) {
		// DeathBox is unexplodable!
	}

	@Override
	public boolean removedByPlayer(final World world, final BlockPos position, final EntityPlayer player, final boolean willHarvest) {
		return false;
	}
}
