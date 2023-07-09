package com.khopan.bromine.item.textlabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;

import com.khopan.bromine.Item;
import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.transform.ColorTransform;
import com.khopan.bromine.area.Area;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;
import com.khopan.bromine.theme.Theme;

public class TextLabel extends Item<TextLabel> {
	private final ColorTransform transform;

	private Font font;
	private String text;
	private TextAlignment alignment;
	private Color color;
	private FontMetrics metrics;
	private int textX;
	private int textY;

	public TextLabel() {
		this.transform = new ColorTransform();
		this.transform.duration().set(150);
		this.transform.value().set(this.theme.getColor(Theme.KEY_TEXT_COLOR));
		this.transform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transform.framerate().set(240);
		this.transform.ticker().set(color -> {
			this.color = color;
			this.update();
		});

		this.text = "";
		this.alignment = TextAlignment.CENTER;
		this.font().set(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
	}

	public Property<Font, TextLabel> font() {
		return new SimpleProperty<Font, TextLabel>(() -> this.font, font -> this.font = font, font -> {
			this.metrics = new JLabel().getFontMetrics(this.font);
			this.onResize();
			this.update();
		}, this).nullable().whenNull(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
	}

	public Property<String, TextLabel> text() {
		return new SimpleProperty<String, TextLabel>(() -> this.text, text -> this.text = text, text -> {
			this.onResize();
			this.update();
		}, this).nullable().whenNull("");
	}

	public Property<TextAlignment, TextLabel> alignment() {
		return new SimpleProperty<TextAlignment, TextLabel>(() -> this.alignment, alignment -> this.alignment = alignment, this).nullable().whenNull(TextAlignment.CENTER);
	}

	@Override
	protected void onThemeUpdate(Theme theme) {
		this.transform.begin(theme.getColor(Theme.KEY_TEXT_COLOR));
	}

	@Override
	protected void onResize() {
		this.textY = (int) Math.round((((double) this.bounds.height) - ((double) this.metrics.getHeight())) * 0.5d + ((double) this.metrics.getAscent()));

		if(TextAlignment.LEFT.equals(this.alignment)) {
			this.textX = this.textY - this.metrics.getAscent();
		} else if(TextAlignment.CENTER.equals(this.alignment)) {
			this.textX = (int) Math.round((((double) this.bounds.width) - ((double) this.metrics.stringWidth(this.text))) * 0.5d);
		} else if(TextAlignment.RIGHT.equals(this.alignment)) {
			this.textX = this.bounds.width - this.metrics.stringWidth(this.text) - (this.textY - this.metrics.getAscent());
		}
	}

	@Override
	protected void render(Area area) {
		area.smooth();
		area.font(this.font);
		area.color(this.color);
		area.text(this.text, this.textX, this.textY);
	}
}
