package com.khopan.bromine.item.textfield;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

import com.khopan.bromine.Bromine;
import com.khopan.bromine.ModernItem;
import com.khopan.bromine.MouseCursor;
import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.ColorInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.input.Keyboard;
import com.khopan.bromine.input.Mouse;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.property.state.SimpleState;
import com.khopan.bromine.property.state.State;
import com.khopan.bromine.theme.Theme;

public class TextField extends ModernItem<TextField> {
	private final Caret caret;
	private final Selection selection;
	private final RoundRectangle2D.Double border;
	private final RoundRectangle2D.Double background;
	private final DoubleTransform transform;
	final ColorInterpolator foregroundInterpolator;
	private final ColorInterpolator backgroundInterpolator;
	private final ColorInterpolator borderInterpolator;

	private Font font;
	private boolean multiline;
	private boolean controlDown;
	private int textX;
	private int textY;
	private int lineHeight;
	String text;
	FontMetrics metrics;
	int viewOffsetX;
	int viewOffsetY;
	int centerOffset;

	public TextField() {
		this.caret = new Caret(this);
		this.selection = new Selection(this);
		this.border = new RoundRectangle2D.Double();
		this.background = new RoundRectangle2D.Double();
		this.foregroundInterpolator = new ColorInterpolator();
		this.backgroundInterpolator = new ColorInterpolator();
		this.borderInterpolator = new ColorInterpolator();
		this.foregroundInterpolator.value = this.theme.getColor(Theme.KEY_TEXT_COLOR);
		this.backgroundInterpolator.value = this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR);
		this.borderInterpolator.value = this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR);
		this.transform = new DoubleTransform();
		this.transform.duration().set(150);
		this.transform.value().set(0.0d);
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(time -> {
			this.foregroundInterpolator.interpolate(time);
			this.backgroundInterpolator.interpolate(time);
			this.borderInterpolator.interpolate(time);
			this.update();
		});

		this.text = "";
		this.multiline = true;
		this.font().set(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		double size = ((double) Bromine.MONITOR_SIZE.width) + ((double) Bromine.MONITOR_SIZE.height);
		double roundness = size * 0.00866916589d;
		double thickness = size * 0.000468603561d;
		this.naturalRoundnessCalculator = bounds -> roundness;
		this.naturalBorderThicknessCalculator = bounds -> thickness;
	}

	public Property<String, TextField> text() {
		return new SimpleProperty<String, TextField>(() -> this.text, text -> this.text = text, text -> {
			this.onResize();
			this.update();
		}, this).nullable().whenNull("");
	}

	public Property<Font, TextField> font() {
		return new SimpleProperty<Font, TextField>(() -> this.font, font -> this.font = font, font -> {
			this.metrics = new JLabel().getFontMetrics(this.font);
			this.onResize();
			this.update();
		}, this).nullable().whenNull(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
	}

	public State<TextField> multiline() {
		return new SimpleState<TextField>(() -> this.multiline, multiline -> this.multiline = multiline, this);
	}

	@Override
	protected void keyboardAction(Keyboard keyboard) {
		if(keyboard.action == Keyboard.ACTION_PRESSED) {
			if(keyboard.keyCode == KeyEvent.VK_CONTROL) {
				this.controlDown = true;
			}

			if(this.controlDown) {
				if(keyboard.keyCode == KeyEvent.VK_V) {
					try {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						this.type((String) clipboard.getData(DataFlavor.stringFlavor));
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				}
			} else {
				if(keyboard.keyCode == KeyEvent.VK_BACK_SPACE) {
					if(!this.text.isEmpty()) {
						this.delete();
						this.update();
					}
				} else if(keyboard.keyCode == KeyEvent.VK_LEFT) {
					this.caret.decrease();
				} else if(keyboard.keyCode == KeyEvent.VK_RIGHT) {
					this.caret.increase();
				} else if(keyboard.keyCode == KeyEvent.VK_UP) {
					if(this.text.contains("\n")) {
						int last = this.text.substring(0, this.caret.position).lastIndexOf('\n');

						if(last != -1) {
							int first = this.text.substring(0, last).lastIndexOf('\n');

							if(first == -1) {
								first = 0;
							}

							String before = this.text.substring(first, (first == 0 ? last + 1 : last));
							int caret = this.caret.position - before.length();

							if(caret > last) {
								caret = last;
							}

							this.caret.move(caret);
						}
					}
				} else if(keyboard.keyCode == KeyEvent.VK_DOWN) {
					if(this.text.contains("\n")) {
						int last = this.text.substring(this.caret.position).indexOf('\n');

						if(last != -1) {
							last += this.caret.position;
							int nextLast = this.text.substring(last + 1).indexOf('\n');

							if(nextLast == -1) {
								nextLast = this.text.length();
							} else {
								nextLast += last + 1;
							}

							int first = this.text.substring(0, this.caret.position).lastIndexOf('\n');

							if(first == -1) {
								first = 0;
							}

							String line = this.text.substring(first, last);
							int caret = this.caret.position + line.length() + (first == 0 ? 1 : 0);

							if(caret > nextLast) {
								caret = nextLast;
							}

							this.caret.move(caret);
						}
					}
				}
			}
		} else if(keyboard.action == Keyboard.ACTION_RELEASED) {
			if(keyboard.keyCode == KeyEvent.VK_CONTROL) {
				this.controlDown = false;
			}
		} else if(keyboard.action == Keyboard.ACTION_TYPED) {
			if(!this.controlDown) {
				if(((int) keyboard.keyCharacter) != KeyEvent.VK_BACK_SPACE) {
					this.type(Character.toString(keyboard.keyCharacter));
					this.update();
				}
			}
		}
	}

	private void delete() {
		this.text = this.text.substring(0, this.caret.position - 1) + this.text.substring(this.caret.position);
		this.caret.decrease();
	}

	private void type(String text) {
		if(!this.multiline) {
			text = text.replaceAll("\n", "");
		}

		this.text = this.text.substring(0, this.caret.position) + text + this.text.substring(this.caret.position);
		this.caret.increase(text.length());
	}

	@Override
	protected void mouseAction(Mouse mouse) {
		if(mouse.action == Mouse.ACTION_ENTERED) {
			this.cursor().set(MouseCursor.TEXT_CURSOR);
		} else if(mouse.action == Mouse.ACTION_EXITED) {
			this.cursor().set(MouseCursor.DEFAULT_CURSOR);
		}
	}

	@Override
	protected void onFocusGained() {
		this.caret.focusGain();
	}

	@Override
	protected void onFocusLost() {
		this.caret.focusLost();
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		this.foregroundInterpolator.begin(this.theme.getColor(Theme.KEY_TEXT_COLOR));
		this.backgroundInterpolator.begin(this.theme.getColor(Theme.KEY_ITEM_BACKGROUND_COLOR));
		this.borderInterpolator.begin(this.theme.getColor(Theme.KEY_HOVERED_ITEM_BORDER_COLOR));
		this.transform.begin(0.0d, 1.0d);
	}

	@Override
	protected void onResize() {
		this.border.x = 0;
		this.border.y = 0;
		this.border.width = this.bounds.width;
		this.border.height = this.bounds.height;
		this.border.arcwidth = this.roundness;
		this.border.archeight = this.roundness;
		this.background.x = this.borderThickness;
		this.background.y = this.borderThickness;
		this.background.width = this.border.width - this.borderThickness * 2.0d;
		this.background.height = this.border.height - this.borderThickness * 2.0d;
		this.background.arcwidth = this.border.arcwidth - this.borderThickness * 2.0d;
		this.background.archeight = this.border.archeight - this.borderThickness * 2.0d;
		int fontHeight = this.metrics.getHeight();
		int spacing = (int) Math.round(((double) fontHeight) * 0.384615385d);
		this.caret.caretBounds.x = spacing;
		this.caret.caretBounds.y = spacing;
		this.caret.caretBounds.width = (int) Math.round(this.borderThickness);
		this.caret.caretBounds.height = fontHeight;
		this.textX = spacing;
		this.textY = this.metrics.getAscent() + this.metrics.getLeading() + spacing;
		this.caret.caretLimitX = (int) Math.round(this.bounds.width - ((double) spacing) * 2.0d);
		this.caret.caretLimitY = (int) Math.round(this.bounds.height - ((double) spacing) * 2.0d - ((double) fontHeight));
		this.caret.spacing = spacing;
		this.lineHeight = fontHeight + spacing;

		if(!this.multiline) {
			this.centerOffset = (int) Math.round((this.bounds.height - ((double) fontHeight)) * 0.5d - ((double) spacing));
		}
	}

	@Override
	protected void render(Area area) {
		area.smooth();
		area.color(this.borderInterpolator.value);
		area.fill(this.border);
		area.color(this.backgroundInterpolator.value);
		area.fill(this.background);
		area.color(this.foregroundInterpolator.value);

		if(this.multiline) {
			area.translate(this.viewOffsetX, this.viewOffsetY);
		} else {
			area.translate(this.viewOffsetX, this.centerOffset);
		}

		area.font(this.font);
		String[] textList = this.text.split("\n");

		for(int i = 0; i < textList.length; i++) {
			area.text(textList[i], this.textX, this.textY + this.lineHeight * i);
		}

		this.selection.render(area.create());
		this.caret.render(area.create());
	}

	@Override
	protected void onRoundnessUpdate(double roundness) {
		this.onResize();
		this.update();
	}

	@Override
	protected void onBorderThicknessUpdate(double borderThickness) {
		this.onResize();
		this.update();
	}
}
