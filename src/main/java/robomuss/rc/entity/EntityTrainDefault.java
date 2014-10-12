package robomuss.rc.entity;

import net.minecraft.entity.player.EntityPlayer;
  import net.minecraft.tileentity.TileEntity;
  import net.minecraft.util.AxisAlignedBB;
  import net.minecraft.world.World;
  import net.minecraftforge.common.util.ForgeDirection;
  import robomuss.rc.block.BlockTrack;
  import robomuss.rc.block.te.TileEntityTrack;
  import robomuss.rc.track.TrackHandler;
  import robomuss.rc.track.piece.TrackPiece;

public class EntityTrainDefault extends EntityTrain {
	  //TODO: I may have broke this with the switch to ForgeDirection, double check.
	  //North = 0
      //East  = 1
      //South = 2
      //West  = 3
  //    public int direction = 0;
	  public ForgeDirection direction = ForgeDirection.NORTH;

      public EntityTrainDefault(World p_i1722_1_) {
          super(p_i1722_1_);
      }

      public EntityTrainDefault(World p_i1723_1_, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_) {
          super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
      }

      /**
       * First layer of player interaction
       */
      public boolean interactFirst(EntityPlayer player) {
          //TODO
          //if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_))) return true;
          player.mountEntity(this);
          return true;
      }

      public int getMinecartType() {
          return 0;
      }

      private boolean firstTick = false;
      public boolean selfPowered = true;
      public float speed = 0;
      private TileEntity altTileEntity;

      @Override
      public void onUpdate() {
          if (selfPowered) {
              speed = 0.1f;
          }
          TileEntity tileentity = worldObj.getTileEntity((int) posX - 1, (int) posY, (int) posZ);
          //altTileEntity = worldObj.getTileEntity((int) posX - 1, (int) posY, (int) posZ);
          if (!firstTick) {
              speed = 0.1f;
              rotateOnPlace(tileentity);
              firstTick = true;
              this.setPosition(this.posX, this.posY, this.posZ);
          }
          if (firstTick) {
              if (tileentity != null && tileentity instanceof TileEntityTrack) {
                  TileEntityTrack te = (TileEntityTrack) tileentity;
                  if (te.extra != null) {
                      te.extra.applyEffectToTrain(te, this);
                  }
              }
              if (selfPowered || (!selfPowered && speed > 0)) {
                  if (tileentity != null && tileentity instanceof TileEntityTrack) {
                      getTrackTypeFromTE(tileentity).moveTrain((TileEntityTrack) tileentity, this);
                  }
                      /*else if(altTileEntity != null && altTileEntity instanceof TileEntityTrack) {
					      getTrackTypeFromTE(altTileEntity).moveTrain((TileEntityTrack) altTileEntity, this);
				      }*/
                  else {
                      TileEntity te_direction_0 = worldObj.getTileEntity((int) posX - 1, (int) posY - 1, (int) posZ - 2);
                      if ((te_direction_0 != null && te_direction_0 instanceof TileEntityTrack)) {
                          if (((BlockTrack) te_direction_0.getBlockType()).track_type == TrackHandler.findTrackType("slope_down")) {
                              getTrackTypeFromTE(te_direction_0).moveTrain((TileEntityTrack) te_direction_0, this);
                          }
                      }

                      TileEntity te_direction_1 = worldObj.getTileEntity((int) posX + 1, (int) posY - 1, (int) posZ);
                      if ((te_direction_1 != null && te_direction_1 instanceof TileEntityTrack)) {
                          if (((BlockTrack) te_direction_1.getBlockType()).track_type == TrackHandler.findTrackType("slope_down")) {
                              getTrackTypeFromTE(te_direction_1).moveTrain((TileEntityTrack) te_direction_1, this);
                          }
                      }

                      TileEntity te_direction_2 = worldObj.getTileEntity((int) posX - 1, (int) posY - 1, (int) posZ + 2);
                      if ((te_direction_2 != null && te_direction_2 instanceof TileEntityTrack)) {
                          if (((BlockTrack) te_direction_2.getBlockType()).track_type == TrackHandler.findTrackType("slope_down")) {
                              getTrackTypeFromTE(te_direction_2).moveTrain((TileEntityTrack) te_direction_2, this);
                          }
                      }

                      TileEntity te_direction_3 = worldObj.getTileEntity((int) posX - 3, (int) posY - 1, (int) posZ);
                      if ((te_direction_3 != null && te_direction_3 instanceof TileEntityTrack)) {
                          if (((BlockTrack) te_direction_3.getBlockType()).track_type == TrackHandler.findTrackType("slope_down")) {
                              getTrackTypeFromTE(te_direction_3).moveTrain((TileEntityTrack) te_direction_3, this);
                          }
                      }
                  }

                  speed -= 0.005f;
                  this.setPosition(this.posX, this.posY, this.posZ);

				      /*if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer) {
					      EntityPlayer player = (EntityPlayer) this.riddenByEntity;
					      player.updateRiderPosition();
				      }*/
              }
          }
      }

      private TrackPiece getTrackTypeFromTE(TileEntity tileentity) {
          TileEntityTrack te = (TileEntityTrack) tileentity;
          if (te.getBlockType() instanceof BlockTrack) {
              BlockTrack block = (BlockTrack) te.getBlockType();
              return block.track_type;
          }
          return TrackHandler.pieces.get(0);
      }

      private void rotateOnPlace(TileEntity tileentity) {
          if (!(tileentity instanceof TileEntityTrack)) {
              tileentity = altTileEntity;
          }
          if (tileentity != null & tileentity instanceof TileEntityTrack) {
              TileEntityTrack te = (TileEntityTrack) tileentity;
              if (getTrackTypeFromTE(tileentity) == TrackHandler.findTrackType("horizontal")) {
                  if (te.direction == ForgeDirection.NORTH) {
                      this.rotationYaw = 90f;
                  } else if (te.direction == ForgeDirection.EAST) {
                      this.rotationYaw = 0f;
                  } else if (te.direction == ForgeDirection.SOUTH) {
                      this.rotationYaw = 270f;
                  } else if (te.direction == ForgeDirection.WEST) {
                      this.rotationYaw = 180f;
                  }

	              switch (te.direction.ordinal() - 2) {
		              case 0:
			              this.direction = ForgeDirection.NORTH;
			              break;
		              case 1:
			              this.direction = ForgeDirection.EAST;
			              break;
		              case 2:
			              this.direction = ForgeDirection.SOUTH;
			              break;
		              case 3:
			              this.direction = ForgeDirection.WEST;
			              break;
	              }
  //                this.direction = te.direction;
  //	            this.direction = ForgeDirection.getOrientation(te.direction.ordinal()).ordinal();
              }
          }
      }

      @Override
      public AxisAlignedBB getBoundingBox() {
          return AxisAlignedBB.getBoundingBox(-1, -1, -1, 3, 3, 3);
      }
  }