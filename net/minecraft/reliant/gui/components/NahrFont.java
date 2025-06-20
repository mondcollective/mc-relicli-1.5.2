package net.minecraft.reliant.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.reliant.Reliant;
import net.minecraft.src.StringUtils;
import net.minecraft.src.Tessellator;

public class NahrFont {

	private Font theFont;
	private Graphics2D theGraphics;
	private FontMetrics theMetrics;
	private float fontSize;
	private int startChar;
	private int endChar;
	private float xPos[], yPos[];
	private int textureID;
	private BufferedImage bufferedImage;
	private boolean textureNeedsRefresh = false;
	public static final int TEXT_NORMAL = -1, 
			TEXT_OUTLINE = 0, 
			TEXT_SHADOW_THIN = 1, 
			TEXT_SHADOW_THICK = 2, 
			TEXT_EMBOSSED_BOTTOM = 3, 
			TEXT_EMBOSSED_TOP = 4;
	private float extraSpacing = 0f;
	private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-ORU]");
    private final Pattern patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]"); 
	
    public NahrFont(Object font, float size) {
    	this(font, size, 0f);
    }
    
	public NahrFont(Object font, float size, float spacing) {
		this.fontSize = size;
		this.startChar = 32;
		this.endChar = 255;
		this.extraSpacing = spacing;
		this.xPos = new float[this.endChar - this.startChar];
		this.yPos = new float[this.endChar - this.startChar];
		this.setupGraphics2D();
		this.createFont(font, size);	
	}
	
	private void setupGraphics2D() {
		this.bufferedImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		this.theGraphics = (Graphics2D) this.bufferedImage.getGraphics();
		this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	private void createFont(Object font, float size) {
		try {
			if (font instanceof Font) {
				this.theFont = (Font) font;
			} else if (font instanceof File) {
				this.theFont = Font.createFont(Font.TRUETYPE_FONT, (File) font).deriveFont(size);
			} else if (font instanceof InputStream) {
				this.theFont = Font.createFont(Font.TRUETYPE_FONT, (InputStream) font).deriveFont(size);
			} else if (font instanceof String) {
				this.theFont = new Font((String) font, Font.PLAIN, Math.round(size));
			} else {
				this.theFont = new Font("Verdana", Font.PLAIN, Math.round(size));
			}
			this.theGraphics.setFont(this.theFont);
		} catch (Exception e) {
			e.printStackTrace();
			this.theFont = new Font("Verdana", Font.PLAIN, Math.round(size));
			this.theGraphics.setFont(this.theFont);
		}
		this.theGraphics.setColor(new Color(255, 255, 255, 0));
		this.theGraphics.fillRect(0, 0, 256, 256);
		this.theGraphics.setColor(Color.white);
		this.theMetrics = this.theGraphics.getFontMetrics();
		
		float x = 5;
		float y = 5;
		for (int i = this.startChar; i < this.endChar; i++) {
			this.theGraphics.drawString(Character.toString((char) i), x, y + this.theMetrics.getAscent());
			this.xPos[i - this.startChar] = x;
			this.yPos[i - this.startChar] = y - this.theMetrics.getMaxDescent();
			x += this.theMetrics.stringWidth(Character.toString((char) i)) + 2f;
			if (x >= 250 - this.theMetrics.getMaxAdvance()) {
				x = 5;
				y += this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + (this.fontSize / 2);
			}
		}
		this.textureID = Reliant.getInstance().getMinecraft().renderEngine.allocateAndSetupTexture(this.bufferedImage);
	}
	
	public void drawString(String text, float x, float y, int TYPE, int color, int color2) {
	    text = this.stripUnsupported(text);
	    
	    // Save matrix state
	    GL11.glPushMatrix();
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glScalef(0.5f, 0.5f, 0.5f);
	    
	    String text2 = this.stripControlCodes(text);
	    switch (TYPE) {
	    case 0:
	        this.drawer(text2, x + 0.5f, y, color2);
	        this.drawer(text2, x - 0.5f, y, color2);
	        this.drawer(text2, x, y + 0.5f, color2);
	        this.drawer(text2, x, y - 0.5f, color2);
	        break;
	    
	    case 1:
	        this.drawer(text2, x + 0.5f, y + 0.5f, color2);
	        break;	
	    
	    case 2:
	        this.drawer(text2, x + 1, y + 1, color2);
	        break;
	    
	    case 3:
	        this.drawer(text2, x, y + 0.5f, color2);
	        break;
	    
	    case 4:
	        this.drawer(text2, x, y - 0.5f, color2);
	        break;
	    
	    default:
	        break;
	    }
	    this.drawer(text, x, y, color);
	    
	    // Restore matrix state
	    GL11.glPopMatrix();
	}
	
	private boolean isTextureValid() {
	    return GL11.glIsTexture(this.textureID);
	}
	
	private void checkGLError(String operation) {
		int error = GL11.glGetError();
		if(error != GL11.GL_NO_ERROR) {
			System.out.println("OpenGL Error during " + operation + ": " + error);
			this.textureNeedsRefresh = true;
		}
	}
	
	private void drawer(String text, float x, float y, int color) {
	    x *= 2;
	    y *= 2;
	    checkGLError("font render start");
	    
	    // Save current OpenGL state
	    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	    int currentTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
	    
	    if (!isTextureValid() || textureNeedsRefresh) {
	        this.textureID = Reliant.getInstance().getMinecraft().renderEngine.allocateAndSetupTexture(this.bufferedImage);
	        this.textureNeedsRefresh = false;
	    }
	    
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureID);
	    float alpha = (float) (color >> 24 & 255) / 255.0F;
	    float red = (float) (color >> 16 & 255) / 255.0F;
	    float green = (float) (color >> 8 & 255) / 255.0F;
	    float blue = (float) (color & 255) / 255.0F;
	    GL11.glColor4f(red, green, blue, alpha);
	    
	    float startX = x;
	    for (int i = 0; i < text.length(); i++) {
	        if (text.charAt(i) == 167 && i + 1 < text.length()) {
	            char oneMore = Character.toLowerCase(text.charAt(i + 1));		
	            if (oneMore == 110) {
	                y += this.theMetrics.getAscent() + 2;
	                x = startX;
	            }	
	            int colorCode = "0123456789abcdefklmnoru".indexOf(oneMore);
	            if (colorCode < 16) {
	                try {
	                    int newColor = Minecraft.getMinecraft().fontRenderer.colorCode[colorCode];
	                    GL11.glColor4f((float) (newColor >> 16) / 255.0F, 
	                        (float) (newColor >> 8 & 255) / 255.0F, 
	                        (float) (newColor & 255) / 255.0F,
	                        alpha);
	                } catch (Exception exception) {
	                    exception.printStackTrace();
	                }
	            } else if (oneMore == 'f') {
	                GL11.glColor4f(1f, 1f, 1f, alpha);
	            } else if (oneMore == 'r') {
	                GL11.glColor4f(red, green, blue, alpha);
	            } else if (oneMore == 'u') {
	                GL11.glColor4f(0.47f, 0.67f, 0.27f, alpha);
	            }
	            i++;
	        } else try {
	            char c = text.charAt(i);
	            this.drawChar(c, x, y);
	            x += this.getStringWidth(Character.toString(c)) * 2;
	        } catch (ArrayIndexOutOfBoundsException indexException) {
	            char c = text.charAt(i);
	            System.out.println("Can't draw character: " + c + " (" + Character.getNumericValue(c) + ")");
	        }
	    }
	    
	    // Restore OpenGL state
	    GL11.glPopAttrib();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Reset color to white
	    
	    checkGLError("font render end");
	}
	
	public static void refreshMinecraftFontTextures() {
	    try {
	        Minecraft mc = Minecraft.getMinecraft();
	        if (mc != null && mc.fontRenderer != null) {
	            mc.fontRenderer.readFontData();
	        }
	    } catch (Exception e) {
	        System.out.println("Failed to refresh Minecraft font textures: " + e.getMessage());
	    }
	}
	
	public float getStringWidth(String text) {
        return (float) (getBounds(text).getWidth() + this.extraSpacing) / 2;
    }
	
	public float getStringHeight(String text) {
        return (float) getBounds(text).getHeight() / 2;
    }
	
	private Rectangle2D getBounds(String text) {
		return this.theMetrics.getStringBounds(text, this.theGraphics);
	}
	
	private void drawChar(char character, float x, float y) throws ArrayIndexOutOfBoundsException {
		Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
		this.drawTexturedModalRect(x, y, xPos[character - this.startChar], yPos[character - this.startChar], (float) bounds.getWidth(), (float) bounds.getHeight() + this.theMetrics.getMaxDescent() + 1);
	}
	
	public List listFormattedStringToWidth(String s, int width) {
        return Arrays.asList(this.wrapFormattedStringToWidth(s, width).split("\n"));
    }
    
    String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = this.sizeStringToWidth(s, width);

        if (s.length() <= wrapWidth) {
            return s;
        } else {
            String split = s.substring(0, wrapWidth);
            String split2 = getFormatFromString(split) + s.substring(wrapWidth + (s.charAt(wrapWidth) == 32 ? 1 : 0));
            return split + "\n" + this.wrapFormattedStringToWidth(split2, width);
        }
    }
	
    private int sizeStringToWidth(String par1Str, float par2) {
        int var3 = par1Str.length();
        float var4 = 0.0F;
        int var5 = 0;
        int var6 = -1;

        for (boolean var7 = false; var5 < var3; var5++) {
            char var8 = par1Str.charAt(var5);

            switch (var8) {
                case 10:
                    var5--;
                    break;

                case 167:
                    if (var5 < var3 - 1) {
                        var5++;
                        char var9 = par1Str.charAt(var5);

                        if (var9 != 108 && var9 != 76) {
                            if (var9 == 114 || var9 == 82 || isFormatColor(var9)) {
                                var7 = false;
                            }
                        } else {
                            var7 = true;
                        }
                    }

                    break;

                    /* Removed to be more aVo
                case 32:
                    var6 = var5;
                    
                case '-':
                	var6 = var5;

                case '_':
                	var6 = var5;
                	
                case ':':
                	var6 = var5;
                	*/
                    
                default:
                	String text = String.valueOf(var8);
                	var4 += this.getStringWidth(text);

                    if (var7) {
                        var4++;
                    }
            }

            if (var8 == 10) {
                var5++;
                var6 = var5;
                break;
            }

            if (var4 > par2) {
                break;
            }
        }

        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }
	
	private String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();

        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = par0Str.charAt(var2 + 1);

                if (isFormatColor(var4)) {
                    var1 = "\u00a7" + var4;
                } else if (isFormatSpecial(var4)) {
                    var1 = var1 + "\u00a7" + var4;
                }
            }
        }

        return var1;
    }
	
	private boolean isFormatColor(char par0) {
        return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
    }
    
    private boolean isFormatSpecial(char par0) {
        return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
    }
	
	private void drawTexturedModalRect(float x, float y, float u, float v, float width, float height) {
		float scale = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((x + 0), (y + height), 0, (u + 0) * scale, (v + height) * scale);
        var9.addVertexWithUV((x + width), (y + height), 0, ((u + width) * scale), (v + height) * scale);
        var9.addVertexWithUV((x + width), (y + 0), 0, (u + width) * scale, (v + 0) * scale);
        var9.addVertexWithUV((x + 0), (y + 0), 0, (u + 0) * scale, (v + 0) * scale);
        var9.draw();
    }
	
	public void markTextureForRefresh() {
	    this.textureNeedsRefresh = true;
	}
	
	/**
	 * Strips a string of its control codes.
	 *
	 * @param s
	 * 		The String to strip the control codes from
	 * @return
	 * 		s without control codes
	 */
	public String stripControlCodes(String s) {
		return this.patternControlCode.matcher(s).replaceAll("");
	}
	
	/**
	 * Strips a string of the control codes that
	 * CustomFont / NahrFont doesn't support.
	 * 
	 * @param s
	 * 		The String to strip the control codes from
	 * @return
	 * 		s without unsupported control codes
	 */
	public String stripUnsupported(String s) {
		return this.patternUnsupported.matcher(s).replaceAll("");
	}
	
	public Graphics2D getGraphics() {
		return this.theGraphics;
	}
	
	public Font getFont() {
		return this.theFont;
	}
	
}