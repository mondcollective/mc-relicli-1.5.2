package net.minecraft.reliant.gui.screens;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.override.OvGuiMainMenu;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.HttpUtil;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.Session;

public class Login extends GuiScreen {

	private GuiTextField username;
	private GuiTextField password;
	private String[] loginInfo = new String[] {
			"", ""
	};
	private GuiButton okay;
	private GuiButton check;
	private boolean canLogin = false;
	private Session newSession;
	private long lastClicked = -1;
	
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
        this.check = new GuiButton(1, this.width / 2 - 70, this.height / 2 + 18, 140, 20, "Check");
        this.check.enabled = false;
        this.okay = new GuiButton(0, this.width / 2 - 70, this.height / 2 + 40, 140, 20, "Okay");
        this.okay.enabled = false;
        this.username = new GuiTextField(this.fontRenderer, this.width / 2 - 70, this.height / 2 - 40, 140, 20);
        this.username.setFocused(true);
        this.username.setText(this.loginInfo[0]);
        this.password = new GuiTextField(this.fontRenderer, this.width / 2 - 70, this.height / 2 - 10, 140, 20);
        this.password.setFocused(false);
        this.password.setText(this.loginInfo[1]);
        this.buttonList.add(okay);
        this.buttonList.add(check);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 70, this.height / 2 + 62, 140, 20, "Random Alt"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 70, this.height / 2 + 84, 140, 20, "Cancel"));
        this.lastClicked = -1;
        this.canLogin = false;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);
		this.okay.enabled = this.canLogin && Reliant.getInstance().getSystemMillis() - this.lastClicked > 750;
		this.check.enabled = this.username.getText() != null && this.password.getText() != null;
		String loginOrNah;
		if (Reliant.getInstance().getSystemMillis() - this.lastClicked <= 750) {
			loginOrNah = "\247k" + "0o0o0o0o0o0o0";
			this.fontRenderer.drawStringWithShadow(loginOrNah, this.width / 2 - this.fontRenderer.getStringWidth(loginOrNah) / 2, this.height / 2 - 56, 0xFFFFD000);
		} else {
			loginOrNah = this.canLogin ? "\247aWorks!" : "\247cInvalid Info!";
			this.fontRenderer.drawStringWithShadow(loginOrNah, this.width / 2 - this.fontRenderer.getStringWidth(loginOrNah) / 2, this.height / 2 - 56, 0xFFFFFFFF);
		}
		this.fontRenderer.drawStringWithShadow("Username", this.width / 2 - this.fontRenderer.getStringWidth("Username") - 73, this.height / 2 - 34, 0xFFFFFFFF);
		this.fontRenderer.drawStringWithShadow("Password", this.width / 2 - this.fontRenderer.getStringWidth("Password") - 73, this.height / 2 - 4, 0xFFFFFFFF);
        this.drawCenteredString(this.fontRenderer, "Login to Account", (this.width / 2), this.height / 2 - 70, 16777215);
        this.username.drawTextBox();
        this.password.drawTextBox();
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
	
	protected void keyTyped(char par1, int par2) {
    	if (par2 == Keyboard.KEY_ESCAPE) {
    		Reliant.getInstance().getMinecraft().displayGuiScreen(new OvGuiMainMenu());
    	}
    	if (this.username.isFocused()) {
    		this.username.textboxKeyTyped(par1, par2);
    		this.loginInfo[0] = this.username.getText();
    	}else if (this.password.isFocused()) {
    		this.password.textboxKeyTyped(par1, par2);
    		this.loginInfo[1] = this.password.getText();
    	}
    }
	
	public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
			case 0: 
				this.login();
				break;
			
			case 1:
				this.lastClicked = Reliant.getInstance().getSystemMillis();
				this.checkLogin(this.loginInfo);
				break;
				
			case 2:
				this.lastClicked = Reliant.getInstance().getSystemMillis();
				this.getRandomAlt();
				break;
				
			case 3:
				Reliant.getInstance().getMinecraft().displayGuiScreen(new OvGuiMainMenu());
				break;
		}
	}
	
	private void login() {
		if (this.newSession != null) {
			Reliant.getInstance().getMinecraft().session = this.newSession;
			Reliant.getInstance().getMinecraft().displayGuiScreen(new OvGuiMainMenu());
		}
	}
	
	private void checkLogin(String[] loginInfoArray) {
		String[] session = HttpUtil.loginToMinecraft((ILogAgent) null, loginInfoArray[0], loginInfoArray[1]);
		if (session != null) {
			this.newSession = new Session(session[0], session[1]);
			this.canLogin = true;
		} else {
			this.canLogin = false;
		}
	}
	
	private void getRandomAlt() {
		try {
			URL altDB = new URL("http://jfb112697.x10.mx/58ae749f25eded36f486bc85feb3f0ab/altdb.php");
			BufferedReader reader = new BufferedReader(new InputStreamReader(altDB.openStream()));
			String infoArray[] = reader.readLine().split(":");
			reader.close();
			Reliant.getInstance().getMinecraft().session = new Session(infoArray[0], infoArray[1]);
			Reliant.getInstance().getMinecraft().displayGuiScreen(new OvGuiMainMenu());
		} catch (Exception exception) {
			exception.printStackTrace();
			this.canLogin = false;
		}
	}
	
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
	
}
