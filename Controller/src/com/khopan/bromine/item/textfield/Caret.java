package com.khopan.bromine.item.textfield;

import java.awt.Point;
import java.awt.Rectangle;

import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.animation.interpolator.value.PointInterpolator;
import com.khopan.bromine.animation.transform.DoubleTransform;
import com.khopan.bromine.animation.transform.IntTransform;
import com.khopan.bromine.area.Area;

class Caret {
	final TextField field;
	final IntTransform transparentTransform;
	final DoubleTransform locationTransform;
	final PointInterpolator location;
	final Rectangle caretBounds;

	volatile boolean blinking;
	volatile long startTime;

	int transparent;
	int position;
	int spacing;
	int caretLimitX;
	int caretLimitY;
	long blinkTime;
	boolean visible;
	boolean caretVisible;
	boolean stop;

	Caret(TextField field) {
		this.field = field;
		this.transparentTransform = new IntTransform();
		this.transparentTransform.duration().set(100);
		this.transparentTransform.value().set(0);
		this.transparentTransform.interpolator().set(Interpolator.SINE_EASE_IN_OUT);
		this.transparentTransform.framerate().set(240);
		this.transparentTransform.ticker().set(transparent -> {
			this.transparent = transparent;
			this.field.update();
		});

		this.location = new PointInterpolator();
		this.locationTransform = new DoubleTransform();
		this.locationTransform.duration().set(50);
		this.locationTransform.value().set(0.0d);
		this.locationTransform.interpolator().set(Interpolator.CUBIC_EASE_IN_OUT);
		this.locationTransform.framerate().set(240);
		this.locationTransform.ticker().set(time -> {
			this.location.interpolate(time);
			this.field.viewOffsetX = 0;
			this.field.viewOffsetY = 0;

			if(this.location.value.x > this.caretLimitX) {
				this.field.viewOffsetX = this.caretLimitX - this.location.value.x;
			}

			if(this.location.value.y > this.caretLimitY) {
				this.field.viewOffsetY = this.caretLimitY - this.location.value.y;
			}

			if(time == 0.0d) {
				this.show();
			}

			this.type();
			this.field.update();
		});

		this.caretBounds = new Rectangle();
		Thread thread = new Thread(this :: blink);
		thread.setPriority(1);
		//thread.start(); // PERFORMANCE LOSS
	}

	void show() {
		this.caretVisible = true;
		this.transparentTransform.begin(255);
	}

	void hide() {
		this.caretVisible = false;
		this.transparentTransform.begin(0);
	}

	void blink() {
		while(true) {
			if(this.field.visibility().getState()) {
				if(this.blinking) {
					long time = System.currentTimeMillis();

					if(time - this.startTime >= 5000) {
						this.stop = true;
					}

					if(time - this.blinkTime >= 530) {
						this.blinkTime = time;

						if(this.caretVisible) {
							this.hide();
						} else {
							this.show();

							if(this.stop) {
								this.stop = false;
								this.blinking = false;
							}
						}
					}
				}
			} else {
				if(this.caretVisible) {
					this.hide();
				}
			}
		}
	}

	void focusGain() {
		this.blinkTime = this.startTime = System.currentTimeMillis();
		this.show();
		this.blinking = true;
	}

	void focusLost() {
		this.blinking = false;
		this.hide();
	}

	void type() {
		this.blinkTime = this.startTime = System.currentTimeMillis();

		if(!this.blinking) {
			this.blinking = true;
		}
	}

	void move(int position) {
		this.position = position;
		int x = 0;
		int y = 0;

		String text = this.field.text.substring(0, this.position);

		if(text.contains("\n")) {
			y = (this.field.metrics.getHeight() + this.spacing) * (text.length() - text.replaceAll("\n", "").length());
			x = this.field.metrics.stringWidth(text.substring(text.lastIndexOf('\n')));
		} else {
			x = this.field.metrics.stringWidth(text);
		}

		this.location.begin(new Point(x, y));
		this.locationTransform.begin(0.0d, 1.0d);
	}

	void increase() {
		this.increase(1);
	}

	void decrease() {
		this.decrease(1);
	}

	void increase(int amount) {
		if(this.position <= this.field.text.length() - amount) {
			this.move(this.position + amount);
		}
	}

	void decrease(int amount) {
		if(this.position >= amount) {
			this.move(this.position - amount);
		}
	}

	void render(Area area) {
		area.color(this.field.foregroundInterpolator.value.getRed(), this.field.foregroundInterpolator.value.getGreen(), this.field.foregroundInterpolator.value.getBlue(), this.transparent);
		area.translate(this.location.value.x, this.location.value.y);
		area.fill(this.caretBounds);
	}
}
