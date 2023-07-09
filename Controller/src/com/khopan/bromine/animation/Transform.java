package com.khopan.bromine.animation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.khopan.bromine.animation.interpolator.Interpolator;
import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;

public abstract class Transform<T> {
	private final T defaultValue;

	private int duration;
	private int framerate;
	private Interpolator interpolator;
	private T value;
	private UpdateCallback updater;
	private TickCallback<T> ticker;
	private ValueUpdateCallback valueUpdater;

	private ScheduledFuture<?> future;
	private int frame;

	public Transform(T defaultValue) {
		this.defaultValue = defaultValue;
		this.value = this.defaultValue;
	}

	public Property<Integer, Transform<T>> duration() {
		return new SimpleProperty<Integer, Transform<T>>(() -> this.duration, duration -> this.duration = duration, this).nullable().whenNull(0);
	}

	public Property<Integer, Transform<T>> framerate() {
		return new SimpleProperty<Integer, Transform<T>>(() -> this.framerate, framerate -> this.framerate = framerate, this).nullable().whenNull(0);
	}

	public Property<Interpolator, Transform<T>> interpolator() {
		return new SimpleProperty<Interpolator, Transform<T>>(() -> this.interpolator, interpolator -> this.interpolator = interpolator, this).nullable().whenNull(Interpolator.LINEAR);
	}

	public Property<T, Transform<T>> value() {
		return new SimpleProperty<T, Transform<T>>(() -> this.value, value -> this.value = value, this).nullable().whenNull(this.defaultValue);
	}

	public Property<UpdateCallback, Transform<T>> updater() {
		return new SimpleProperty<UpdateCallback, Transform<T>>(() -> this.updater, updater -> this.updater = updater, this).nullable().whenNull(null);
	}

	public Property<TickCallback<T>, Transform<T>> ticker() {
		return new SimpleProperty<TickCallback<T>, Transform<T>>(() -> this.ticker, ticker -> this.ticker = ticker, this).nullable().whenNull(null);
	}

	public Property<ValueUpdateCallback, Transform<T>> valueUpdater() {
		return new SimpleProperty<ValueUpdateCallback, Transform<T>>(() -> this.valueUpdater, valueUpdater -> this.valueUpdater = valueUpdater, this).nullable().whenNull(null);
	}

	public void settings(Transform<?> transform) {
		if(transform != null) {
			this.updater = transform.updater;
			this.interpolator = transform.interpolator;
			this.framerate = transform.framerate;
			this.duration = transform.duration;
		}
	}

	public void begin(T to) {
		this.begin(this.value, to);
	}

	public void begin(T from, T to) {
		if(this.duration <= 0) {
			throw new IllegalArgumentException("Duration cannot be less than or equal to zero");
		}

		if(this.framerate <= 0) {
			throw new IllegalArgumentException("Framerate cannot be less than or equal to zero");
		}

		if(this.future != null) {
			this.future.cancel(true);
			this.future = null;
		}

		double baseNumber = 0.0d;
		TimeUnit unit = null;

		if(this.framerate <= 1000) {
			baseNumber = 1000.0d;
			unit = TimeUnit.MILLISECONDS;
		} else if(this.framerate <= 1000000) {
			baseNumber = 1000000.0d;
			unit = TimeUnit.MICROSECONDS;
		} else if(this.framerate <= 1000000000) {
			baseNumber = 1000000000.0d;
			unit = TimeUnit.NANOSECONDS;
		}

		int frames = (int) Math.round(((double) this.duration) * (((double) this.framerate) / 1000.0d));
		this.frame = 0;
		this.future = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {			
			if(this.frame > frames) {
				this.future.cancel(true);
				this.future = null;
				return;
			}

			double result = ((double) this.frame) / ((double) frames);

			if(this.interpolator != null) {
				result = this.interpolator.interpolate(result);
			}

			result = result < 0.0d ? 0.0d : result > 1.0d ? 1.0d : result;

			if(this.valueUpdater != null) {
				this.valueUpdater.update(result);
			}

			if(this.ticker != null) {
				this.ticker.tick(this.value = this.interpolate(result, from, to));
			}

			if(this.updater != null) {
				this.updater.update();
			}

			this.frame++;
		}, 0, Math.round(baseNumber / this.framerate), unit);
	}

	protected abstract T interpolate(double time, T from, T to);
}
