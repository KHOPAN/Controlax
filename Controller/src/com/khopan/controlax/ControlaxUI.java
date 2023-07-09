package com.khopan.controlax;

import java.awt.Dimension;

import com.khopan.bromine.BromineApplication;
import com.khopan.bromine.item.HolderPane;
import com.khopan.bromine.item.button.Button;
import com.khopan.bromine.item.menu.MenuBar;
import com.khopan.bromine.item.menu.MenuOption;
import com.khopan.bromine.item.textfield.TextField;
import com.khopan.bromine.item.textlabel.TextAlignment;
import com.khopan.bromine.item.textlabel.TextLabel;
import com.khopan.bromine.layout.FullLayout;
import com.khopan.bromine.layout.GridLayout;
import com.khopan.bromine.layout.MenuLayout;
import com.khopan.bromine.theme.DarkTheme;
import com.khopan.bromine.theme.HackerTheme;
import com.khopan.bromine.theme.LightTheme;
import com.khopan.bromine.theme.SakuraTheme;
import com.khopan.controlax.ui.ControlWindow;
import com.khopan.lazel.config.BinaryConfigObject;
import com.khopan.lazel.packet.BinaryConfigPacket;

public class ControlaxUI extends BromineApplication {
	public final HolderPane pane;
	public final TextField outputField;
	public final TextField colorField;
	public final TextField transparencyField;
	public final TextField rainbowMovingRateField;

	public ControlaxUI() {
		this.pane = new HolderPane();
		this.pane.layout().set(new MenuLayout(25));
		MenuOption theme = new MenuOption().text().set("Theme");
		theme.addOption(new MenuOption().text().set("Light Theme").action().set(() -> this.window.theme().set(LightTheme.THEME)));
		theme.addOption(new MenuOption().text().set("Dark Theme").action().set(() -> this.window.theme().set(DarkTheme.THEME)));
		theme.addOption(new MenuOption().text().set("Hacker Theme").action().set(() -> this.window.theme().set(HackerTheme.THEME)));
		theme.addOption(new MenuOption().text().set("Sakura Theme").action().set(() -> this.window.theme().set(SakuraTheme.THEME)));
		MenuBar bar = new MenuBar();
		bar.addOption(theme);
		bar.addOption(new MenuOption().text().set("Launch Fallback").action().set(() -> {
			Controlax.INSTANCE.window = new ControlWindow();
		}));

		this.pane.add(bar);
		HolderPane pane = new HolderPane();
		pane.layout().set(new GridLayout(2, 1));
		this.outputField = new TextField();
		pane.add(new HolderPane().layout().set(new GridLayout(1, 2)).add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Output:").alignment().set(TextAlignment.LEFT)).add(this.outputField)).add(new Button().text().set("Left")));
		HolderPane inputPane = new HolderPane();
		inputPane.layout().set(new GridLayout(1, 3));
		HolderPane colorCommandPane = new HolderPane();
		colorCommandPane.layout().set(new GridLayout(2, 1));
		HolderPane colorPane = new HolderPane();
		colorPane.layout().set(new GridLayout(1, 7));
		colorPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Color:")).add(this.colorField = new TextField().multiline().disable()));
		colorPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Transparency:")).add(this.transparencyField = new TextField().multiline().disable()));
		colorPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Rainbow Moving Rate:")).add(this.rainbowMovingRateField = new TextField().multiline().disable()));
		colorPane.add(new Button().text().set("Send Color").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 6);
			config.putInt("SubAction", 1);
			int color;

			try {
				color = Integer.parseInt(this.colorField.text().get(), 16);

				if(color < 0x000000 || color > 0xFFFFFF) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				color = 0xFFFFFF;
			}

			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyField.text().get());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			config.putInt("Color", color);
			config.putFloat("Transparency", transparency);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		}));

		colorPane.add(new Button().text().set("Clear Color").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 6);
			config.putInt("SubAction", 0);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		}));

		colorPane.add(new Button().text().set("Moving Rainbow").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 6);
			config.putInt("SubAction", 2);
			config.putBoolean("Moving", true);
			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyField.text().get());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			float rate;

			try {
				rate = Float.parseFloat(this.rainbowMovingRateField.text().get());
			} catch(Throwable Errors) {
				rate = 0.0001f;
			}

			config.putFloat("Transparency", transparency);
			config.putFloat("Rate", rate);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		}));

		colorPane.add(new Button().text().set("Rainbow").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 6);
			config.putInt("SubAction", 2);
			config.putBoolean("Moving", false);
			float transparency;

			try {
				transparency = Float.parseFloat(this.transparencyField.text().get());

				if(transparency < 0.0f || transparency > 1.0f) {
					throw new IllegalArgumentException();
				}
			} catch(Throwable Errors) {
				transparency = 1.0f;
			}

			config.putFloat("Transparency", transparency);
			config.putFloat("Rate", 0.0001f);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		}));

		colorCommandPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Color:").alignment().set(TextAlignment.LEFT)).add(colorPane));
		HolderPane commandPane = new HolderPane();
		commandPane.layout().set(new GridLayout(1, 5));
		commandPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Directory:")).add(new TextField().multiline().disable()));
		commandPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Command:")).add(new TextField().multiline().disable()));
		commandPane.add(new HolderPane().layout().set(new GridLayout(3, 1)).add(new Button().text().set("Sleep").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 1);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		})).add(new Button().text().set("Shutdown").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 2);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		})).add(new Button().text().set("Restart").action().set(() -> {
			BinaryConfigObject config = new BinaryConfigObject();
			config.putInt("Action", 2);
			config.putInt("SubAction", 3);
			Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
		})));

		commandPane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new Button().text().set("Send Command").action().set(() -> {
			/*try {
				String command = this.inputCommandField.getText();
				String directory = this.directoryField.getText();

				if(command == null || command.isEmpty()) {
					Controlax.INSTANCE.window.status("Error: Empty Command");
					return;
				}

				this.commandIdentifierCode = ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
				Controlax.INSTANCE.window.status("Sending Command...\nIdentfiier Code: 0x" + String.format("%06x", this.commandIdentifierCode).toUpperCase());
				BinaryConfigObject config = new BinaryConfigObject();
				config.putInt("Action", 1);
				config.putString("Directory", directory);
				config.putString("Command", command);
				config.putInt("IdentifierCode", this.commandIdentifierCode);
				Controlax.INSTANCE.client.sendPacket(new BinaryConfigPacket(config));
			} catch(Throwable Errors) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				new InternalError("Error while sending a command", Errors).printStackTrace(printWriter);
				this.outputField.text().set(stringWriter.toString());
			}*/
		})).add(new Button().text().set("Clear Output Window").action().set(() -> {
			this.outputField.text().set("");
		})));

		commandPane.add(new Button().text().set("Emergency Terminate"));
		colorCommandPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Command:").alignment().set(TextAlignment.LEFT)).add(commandPane));
		inputPane.add(colorCommandPane);
		HolderPane messageScreenshotPane = new HolderPane();
		messageScreenshotPane.layout().set(new GridLayout(2, 1));
		HolderPane messagePane = new HolderPane();
		messagePane.layout().set(new GridLayout(1, 2));
		messagePane.add(new HolderPane().layout().set(new GridLayout(2, 1)).add(new TextLabel().text().set("Message:")).add(new TextField()));
		messagePane.add(new Button().text().set("Send Message"));
		messageScreenshotPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Message:").alignment().set(TextAlignment.LEFT)).add(messagePane));
		HolderPane screenshotPane = new HolderPane();
		screenshotPane.layout().set(new GridLayout(1, 3));
		screenshotPane.add(new TextLabel().text().set("Status: Disconnected"));
		screenshotPane.add(new Button().text().set("Take Screenshot"));
		screenshotPane.add(new Button().text().set("View Screenshot"));
		messageScreenshotPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Screenshot:").alignment().set(TextAlignment.LEFT)).add(screenshotPane));
		inputPane.add(messageScreenshotPane);
		HolderPane controllingPane = new HolderPane();
		controllingPane.layout().set(new GridLayout(2, 1));
		HolderPane mouseKeyboardPane = new HolderPane();
		mouseKeyboardPane.layout().set(new GridLayout(1, 2));
		HolderPane mousePane = new HolderPane();
		mousePane.layout().set(new GridLayout(1, 2));
		mousePane.add(new TextLabel().text().set("Mouse Control: OFF"));
		mousePane.add(new Button().text().set("Toggle Mouse Control"));
		mouseKeyboardPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Mouse:").alignment().set(TextAlignment.LEFT)).add(mousePane));
		HolderPane keyboardPane = new HolderPane();
		keyboardPane.layout().set(new GridLayout(1, 2));
		keyboardPane.add(new TextLabel().text().set("Keyboard Control: OFF"));
		keyboardPane.add(new Button().text().set("Toggle Keyboard Control"));
		mouseKeyboardPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Keyboard:").alignment().set(TextAlignment.LEFT)).add(keyboardPane));
		controllingPane.add(mouseKeyboardPane);
		controllingPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Typing Zone:").alignment().set(TextAlignment.LEFT)).add(new TextField()));
		inputPane.add(new HolderPane().layout().set(new MenuLayout(25)).add(new TextLabel().text().set("Controlling:").alignment().set(TextAlignment.LEFT)).add(controllingPane));
		pane.add(inputPane);
		this.pane.add(new HolderPane().layout().set(new FullLayout(20)).add(pane));
	}

	@Override
	protected void initialize() {
		this.window.layout().set(FullLayout.INSTANCE);
		this.window.add(this.pane);
		this.window.size().set(new Dimension(600, 400));
		this.window.center();
		this.window.visibility().enable();
	}
}
