package net.minecraft.reliant.override;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.reliant.Reliant;
import net.minecraft.src.ChatLine;
import net.minecraft.src.GuiNewChat;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringUtils;

public class OvGuiNewChat extends GuiNewChat {

	private Minecraft mc;
	
	public OvGuiNewChat(Minecraft par1Minecraft) {
		super(par1Minecraft);
		this.mc = par1Minecraft;
	}
	
	public void drawChat(int par1) {
        if (this.mc.gameSettings.chatVisibility != 2) {
            int var2 = this.func_96127_i();
            boolean var3 = false;
            int var4 = 0;
            int var5 = this.field_96134_d.size();
            float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (var5 > 0) {
                if (this.getChatOpen()) {
                    var3 = true;
                }

                float var7 = this.func_96131_h();
                int var8 = MathHelper.ceiling_float_int((float) this.func_96126_f() / var7);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 20.0F, 0.0F);
                GL11.glScalef(var7, var7, 1.0F);
                int var9;
                int var11;
                int var14;

                for (var9 = 0; var9 + this.field_73768_d < this.field_96134_d.size() && var9 < var2; var9++) {
                    ChatLine var10 = (ChatLine) this.field_96134_d.get(var9 + this.field_73768_d);

                    if (var10 != null) {
                        var11 = par1 - var10.getUpdatedCounter();

                        if (var11 < 200 || var3) {
                            double var12 = (double) var11 / 200.0D;
                            var12 = 1.0D - var12;
                            var12 *= 10.0D;

                            if (var12 < 0.0D) {
                                var12 = 0.0D;
                            }

                            if (var12 > 1.0D) {
                                var12 = 1.0D;
                            }

                            var12 *= var12;
                            var14 = (int) (255.0D * var12);

                            if (var3) {
                                var14 = 255;
                            }

                            var14 = (int) ((float) var14 * var6);
                            var4++;

                            if (var14 > 3)
                            {
                                byte var15 = 0;
                                int var16 = -var9 * 9;
                                drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
                                GL11.glEnable(GL11.GL_BLEND);
                                String var17 = var10.getChatLineString();

                                var17 = var17.replaceAll("\247u", "\2479");
                                if (!this.mc.gameSettings.chatColours) {
                                    var17 = Reliant.getInstance().relFont.stripControlCodes(var17);
                                }
                                
                                this.mc.fontRenderer.drawStringWithShadow(var17, var15, var16 - 8, 16777215 + (var14 << 24));
                            }
                        }
                    }
                }

                if (var3) {
                    var9 = this.mc.fontRenderer.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int var18 = var5 * var9 + var5;
                    var11 = var4 * var9 + var4;
                    int var20 = this.field_73768_d * var11 / var5;
                    int var13 = var11 * var11 / var18;

                    if (var18 != var11) {
                        var14 = var20 > 0 ? 170 : 96;
                        int var19 = this.field_73769_e ? 13382451 : 3355562;
                        drawRect(0, -var20, 2, -var20 - var13, var19 + (var14 << 24));
                        drawRect(2, -var20, 1, -var20 - var13, 13421772 + (var14 << 24));
                    }
                }

                GL11.glPopMatrix();
            }
        }
    }
	
	public void printChatMessageWithOptionalDeletion(String par1Str, int par2) {
        this.func_96129_a(par1Str, par2, this.mc.ingameGUI.getUpdateCounter(), false);
        this.mc.getLogAgent().logInfo("[CHAT] " + par1Str);
        Reliant.getInstance().reliantChat.printChatMessageWithOptionalDeletion(par1Str, par2);
    }
	
	private void func_96129_a(String par1Str, int par2, int par3, boolean par4) {
		// TODO: Fix name protect when ReliantChat is disabled
        boolean var5 = this.getChatOpen();
        boolean var6 = true;

        if (par2 != 0) {
            this.deleteChatLine(par2);
        }

        String string2 = par1Str;
        if (Reliant.getInstance().getModuleHelper().getModuleByName("name").getState()) {
            for (String[] nameArray : Reliant.getInstance().getNameProtect().getNameList()) {
            	String orig = "(?i)" + nameArray[0];
            	String alias = "\2479" + nameArray[1] + "\247f";
                string2 = string2.replaceAll("(?i)" + Reliant.getInstance().getMinecraft().session.username, 
                		"\2479Chuck Knoblock\247f").replaceAll(orig, alias);
            }
        }   
        Iterator var7 = this.mc.fontRenderer.listFormattedStringToWidth(string2, 
        		MathHelper.floor_float((float) this.func_96126_f() / this.func_96131_h())).iterator();

        while (var7.hasNext()) {
            String var8 = (String) var7.next();

            if (var5 && this.field_73768_d > 0) {
                this.field_73769_e = true;
                this.scroll(1);
            }

            if (!var6) {
                var8 = " " + var8;
            }

            var6 = false;
            this.field_96134_d.add(0, new ChatLine(par3, var8, par2));
        }

        while (this.field_96134_d.size() > 100) {
            this.field_96134_d.remove(this.field_96134_d.size() - 1);
        }

        if (!par4) {
            this.chatLines.add(0, new ChatLine(par3, par1Str.trim(), par2));

            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

}
