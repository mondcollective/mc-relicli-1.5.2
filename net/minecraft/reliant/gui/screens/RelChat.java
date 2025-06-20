package net.minecraft.reliant.gui.screens;

import java.nio.IntBuffer;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventDrawChat;
import net.minecraft.reliant.event.events.EventInGame;
import net.minecraft.reliant.gui.components.NahrFont;
import net.minecraft.src.ChatLine;
import net.minecraft.src.GuiNewChat;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringUtils;

public class RelChat extends GuiNewChat implements Listener {

	// TODO: Fix scrolling?
	
	private Minecraft mc;
	private int screenWidth, screenHeight;
	private int dragX, dragY;
	private int startX, startY;
	private int minX, minY, maxX, maxY;
	public float chatWidth = 250f;
	private float chatHeight = 180f;
	private float widthMult = 0.56f, heightMult = 0.44f;
	private boolean dragging = false;
	
	public RelChat(Minecraft par1Minecraft) {
		super(par1Minecraft);
		this.mc = par1Minecraft;
		Reliant.getInstance().getEventManager().addListener(this);
	}
	
	@Marker(eventPriority = Priority.LOW)
	public void inGameRender(EventInGame eventInGame) {
		if (this.screenWidth != eventInGame.getScreenDimensions()[0]) {
			this.screenWidth = eventInGame.getScreenDimensions()[0];
			this.func_96132_b();
		}
		if (this.screenHeight != eventInGame.getScreenDimensions()[1]) {
			this.screenHeight = eventInGame.getScreenDimensions()[1];
			this.func_96132_b();
		}
		this.chatWidth = Math.round((float) this.screenWidth * this.widthMult);
		if (this.chatWidth != this.chatWidth - (this.chatWidth % 17f)) {
			this.chatWidth -= this.chatWidth % 22f; 
			this.func_96132_b();
		}
		this.chatHeight = Math.round((float) this.screenHeight * this.heightMult);
		this.chatHeight -= this.chatHeight % 12f;
	}
	
	@Marker(eventPriority = Priority.HIGH) 
	public void onDrawChat(EventDrawChat eventDrawChat) {
		if (Reliant.getInstance().getModuleHelper().getModuleByName("chat").getState()) {
			eventDrawChat.setCancelled(true);
			this.drawChat(eventDrawChat.getUpdateCounter());
		}
	}
	
	public void drawScreen(int i, int j) {
		if (this.dragging) {
			this.mouseDragged(i, j);
		}
	}
	
	public void mouseClicked(int i, int j) {
		if (this.isHovering(i, j)) {
			this.dragging = !this.dragging;
			if (this.dragging) {
				this.startX = i - dragX;
				this.startY = j - dragY + this.screenHeight - 28;
			}
		}
	}
	
	private void mouseDragged(int i, int j) {
		this.dragX = i - startX;
		this.dragY = j - startY + this.screenHeight - 28;
	}
	
	public void mouseReleased(int i, int j) {
		if (this.isHovering(i, j)) {
			Reliant.getInstance().getGuiPositions().saveFile();
		}
		this.dragging = false;
	}
	
	private boolean isHovering(int i, int j) {
		return i >= this.minX
				&& j >= this.minY + this.screenHeight - 28
				&& i <= this.maxX
				&& j <= this.maxY + this.screenHeight - 28;
	}
	
	@Override
	public void drawChat(int i) {
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
                float line = 0;

                for (var9 = 0; var9 + this.field_73768_d < this.field_96134_d.size() && var9 < var2; var9++) {
                    ChatLine var10 = (ChatLine) this.field_96134_d.get(var9 + this.field_73768_d);

                    if (var10 != null) {
                        var11 = i - var10.getUpdatedCounter();

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

                            if (var14 > 3) {
                                line += 11.5f;
                            }
                        }
                    }
                }
                // TODO: No scaling         
                int color = this.dragging ? 0x90000000 : 0x50000000;
                if (var3 /*Chat open*/) {
                	Reliant.getInstance().getGuiUtils().drawOutlinedRectangle(this.dragX, -var2 * 11.5f - 2f + this.dragY, this.chatWidth + 7f + this.dragX, 3 + this.dragY, color, color, 2);
                	Reliant.getInstance().getGuiUtils().drawOutlinedRectangle(this.dragX, -var2 * 11.5f - 16 + this.dragY, this.chatWidth + 7f + this.dragX, -var2 * 11.5f - 3.5f + this.dragY, color, color, 2);
                	Reliant.getInstance().lucidaConsole.drawString("Chat", 2.5f + this.dragX, -var2 * 11.5f - 17.5f + this.dragY, -1, this.dragging ? 0xFFB3B3B3 : 0xFFFFFFFF, 0);
                	this.minX = 0 + this.dragX;
                	this.minY = Math.round(-var2 * 11.5f - 16 + this.dragY);
                	this.maxX = Math.round(this.chatWidth + 7f + this.dragX);
                	this.maxY = Math.round(-var2 * 11.5f - 3.5f + this.dragY);
                } else if (line > 0) {
                	Reliant.getInstance().getGuiUtils().drawOutlinedRectangle(this.dragX, -line - 2f + this.dragY, this.chatWidth + 7f + this.dragX, 3 + this.dragY, color, color, 2);
                }
                
                for (var9 = 0; var9 + this.field_73768_d < this.field_96134_d.size() && var9 < var2; var9++) {
                    ChatLine var10 = (ChatLine) this.field_96134_d.get(var9 + this.field_73768_d);

                    if (var10 != null) {
                        var11 = i - var10.getUpdatedCounter();

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

                            if (var14 > 3) {
                                byte var15 = 0;
                                float var16 = (float) -var9 * 11.5f;
                                GL11.glEnable(GL11.GL_BLEND);
                                String var17 = var10.getChatLineString();

                                if (!this.mc.gameSettings.chatColours) {
                                    var17 = StringUtils.stripControlCodes(var17);
                                }

                                Reliant.getInstance().relFont.drawString(var17, var15 + 3f + this.dragX, var16 - 13 + this.dragY, NahrFont.TEXT_SHADOW_THICK, 0xFFFFFFFF, 0xFF000000);
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
                    }
                }

                GL11.glPopMatrix();
            }
		}
	}
	
	public void printChatMessageWithOptionalDeletion(String par1Str, int par2) {
        this.func_96129_a(par1Str, par2, this.mc.ingameGUI.getUpdateCounter(), false);
    }
	
	public void func_96129_a(String par1Str, int par2, int par3, boolean par4) {
        boolean var5 = this.getChatOpen();
        boolean var6 = true;

        if (par2 != 0) {
            this.deleteChatLine(par2);
        }

        String string2 = par1Str;
        if (Reliant.getInstance().getModuleHelper().getModuleByName("name").getState()) {
            for (String[] nameArray : Reliant.getInstance().getNameProtect().getNameList()) {
            	String orig = "(?i)" + nameArray[0];
            	String alias = "\247u" + nameArray[1] + "\247f";
                string2 = string2.replaceAll("(?i)" + Reliant.getInstance().getMinecraft().session.username, 
                		"\247uChuck Knoblock\247f").replaceAll(orig, alias);
            }
        }        
        Iterator var7 = Reliant.getInstance().relFont.listFormattedStringToWidth(string2, 
        		Math.round(this.chatWidth)).iterator();

        while (var7.hasNext()) {
            String var8 = (String) var7.next();

            if (var5 && this.field_73768_d > 0) {
                this.field_73769_e = true;
                this.scroll(1);
            }

            if (!var6) {
                //var8 = " " + var8;
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
	
	public void func_96132_b() {
        this.field_96134_d.clear();
        this.resetScroll();

        for (int var1 = this.chatLines.size() - 1; var1 >= 0; var1--) {
            ChatLine var2 = (ChatLine) this.chatLines.get(var1);
            this.func_96129_a(var2.getChatLineString(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
        }
    }
	
	public int func_96126_f()
    {
        return func_96128_a(this.mc.gameSettings.chatWidth);
    }

    public int func_96133_g()
    {
        return func_96130_b(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float func_96131_h()
    {
        return this.mc.gameSettings.chatScale;
    }

    public int gfunc_96128_a(float par0) {
        short var1 = 320;
        byte var2 = 40;
        return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
    }

    public int gfunc_96130_b(float par0) {
        short var1 = 180;
        byte var2 = 20;
        return MathHelper.floor_float(par0 * (float)(var1 - var2) + (float)var2);
    }

    public int func_96127_i()
    {
    	return Math.round(this.chatHeight / 12f);
        //return this.func_96133_g() / 9;
    }
    
    /**
	 * @return
	 * 		Integer array containing the drag and start info for
	 * 		the Chat. [0] - dragX, [1] - dragY, [2] - startX,
	 * 		[3] - startY
	 */
	public int[] getDragInfo() {
		return new int[] {
			this.dragX, this.dragY, this.startX, this.startY
		};
	}
	
	/**
	 * Sets the drag and start info for the Chat.
	 * 
	 * @param infoArray
	 * 		Integer array containing the info. [0] - dragX,
	 * 		[1] - dragY, [2] - startX, [3] - startY
	 */
	public void setDragInfo(int[] infoArray) {
		this.dragX = infoArray[0];
		this.dragY = infoArray[1];
		this.startX = infoArray[2];
		this.startY = infoArray[3];
	}
	
}