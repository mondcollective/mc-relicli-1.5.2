package net.minecraft.reliant.override;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.reliant.Reliant;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.Tessellator;

public class OvRenderLiving extends RenderLiving {

	public OvRenderLiving(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}
	
	protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);
        String name = Reliant.getInstance().relFont.stripControlCodes(par2Str);
        boolean friend = false;
        if (Reliant.getInstance().getModuleHelper().getModuleByName("name").getState()) {
        	for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
        		if (nameArray[0].equalsIgnoreCase(name)) {
        			name = nameArray[1];
        			friend = true;
        		} else {
                	name = name.replaceAll("(?i)" + nameArray[0] ,  nameArray[1]);
        		}
        	}
        }
        if (!name.equalsIgnoreCase(Reliant.getInstance().relFont.stripControlCodes(par2Str))
        		|| Reliant.getInstance().getNameProtect().containsName(name)) {
        	friend = true;
        }
        if (var10 <= Math.pow(128D, 2)) {
            FontRenderer var12 = this.getFontRendererFromRenderManager();
            float distance = (float) Math.sqrt(var10);
            float var13 = distance / 5 > 1.6f ? distance / 5 : 1.6F;
            float var14 = 0.016666668F * var13;
            GL11.glPushMatrix();
            float up = distance / 32 > 0.5f ? distance / 32 : 0.5f;
            GL11.glTranslatef((float) par3 + 0.0F, (float) par5 + par1EntityLiving.height + up, (float) par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var15 = Tessellator.instance;
            byte var16 = 0;

            if (name.equals("deadmau5")) {
                var16 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = var12.getStringWidth(name) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex(-var17 - 1.5, -1.5 + var16, 0.0D);
            var15.addVertex(-var17 - 1.5, 9.5 + var16, 0.0D);
            var15.addVertex(var17 + 2.5, 9.5 + var16, 0.0D);
            var15.addVertex(var17 + 2.5, -1.5 + var16, 0.0D);
            var15.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int color = friend ? 0xFF5ABDFC : var10 >= Math.pow(64D, 2) ? 0xFF03B705 : 0xFFFFFFFF;
            var12.drawStringWithShadow(name, -var12.getStringWidth(name) / 2, var16, color);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var12.drawStringWithShadow(name, -var12.getStringWidth(name) / 2, var16, color);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
	
	protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        if (Minecraft.isGuiEnabled() && par1EntityLiving != this.renderManager.livingPlayer 
        		&& !par1EntityLiving.func_98034_c(Minecraft.getMinecraft().thePlayer) 
        		&& (par1EntityLiving.func_94059_bO() || par1EntityLiving.func_94056_bM() 
        				&& par1EntityLiving == this.renderManager.field_96451_i)) {
        	float distance = Reliant.getInstance().getPlayer().getDistanceToEntity(par1EntityLiving);
            float var8 = distance / 5 > 1.6f ? distance / 5 : 1.6F;
            float var9 = 0.016666668F * var8;
            double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float var12 = par1EntityLiving.isSneaking() ? 32.0F : 64.0F;

            if (var10 < Math.pow(128D, 2)) {
                String var13 = par1EntityLiving.getTranslatedEntityName();
                String name = Reliant.getInstance().relFont.stripControlCodes(var13);
                boolean friend = false;  
                if (Reliant.getInstance().getModuleHelper().getModuleByName("name").getState()) {
                	for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
                		if (nameArray[0].equalsIgnoreCase(name)) {
                			name = nameArray[1];
                			friend = true;
                		} else {
                        	name = name.replaceAll("(?i)" + nameArray[0] , nameArray[1]);
                		}
                	}
                }
                if (!name.equalsIgnoreCase(Reliant.getInstance().relFont.stripControlCodes(var13))
                		|| Reliant.getInstance().getNameProtect().containsName(name)) {
                	friend = true;
                }
                
                if (par1EntityLiving.isSneaking()) {
                    FontRenderer var14 = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    float up = distance / 32 > 0.5f ? distance / 32 : 0.5f;
                    GL11.glTranslatef((float) par2 + 0.0F, (float) par4 + par1EntityLiving.height + up, (float) par6);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-var9, -var9, var9);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator var15 = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    var15.startDrawingQuads();
                    int var16 = var14.getStringWidth(name) / 2;
                    var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    var15.addVertex(-var16 - 1.5, -1.5D, 0.0D);
                    var15.addVertex(-var16 - 1.5, 9.5, 0.0D);
                    var15.addVertex(var16 + 2.5, 9.5, 0.0D);
                    var15.addVertex(var16 + 2.5, -1.0D, 0.0D);
                    var15.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    int color = friend ? 0xFF5ABDFC : var10 >= Math.pow(64D, 2) ? 0xFF03B705 : 0xFFFC0000;
                    var14.drawStringWithShadow(name, -var14.getStringWidth(name) / 2, 0, color);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(true);
                    var14.drawStringWithShadow(name, -var14.getStringWidth(name) / 2, 0, color);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                else
                {
                    this.func_96449_a(par1EntityLiving, par2, par4, par6, var13, var9, var10);
                }
            }
        }
    }

}
