package robomuss.rc.block.render;

import org.lwjgl.opengl.GL11;

import robomuss.rc.block.model.ModelConveyor;
import robomuss.rc.block.model.ModelTrackFabricator;
import robomuss.rc.block.te.TileEntityConveyor;
import robomuss.rc.block.te.TileEntityTrackFabricator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityRenderConveyor extends TileEntitySpecialRenderer {

	private ModelBase model;
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		ResourceLocation textures = (new ResourceLocation("rc:textures/models/conveyor.png"));

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5f, (float) z + 0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		switch(((TileEntityConveyor) te).direction){
        case 1:
        		GL11.glRotatef(180f, -180f, 0f, 0f);
                break;
        case 2:
                GL11.glRotatef(180f, 180f, 0f, 180f);
                break;
        case 3:
                GL11.glRotatef(180f, 0f, 0f, 180f);
                break;
        default:
        		GL11.glRotatef(180f, -180f, 0f, 180f);
        		break;
		}
		model = new ModelConveyor();
		this.model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}

}