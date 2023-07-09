package com.khopan.bromine;

import java.awt.Rectangle;

import com.khopan.bromine.property.Property;
import com.khopan.bromine.property.SimpleProperty;

public abstract class ModernItem<T extends ModernItem<T>> extends Item<T> {
	protected double roundness;
	protected double borderThickness;
	protected ValueCalculator<Double> naturalRoundnessCalculator;
	protected ValueCalculator<Double> naturalBorderThicknessCalculator;

	private boolean naturalRoundness;
	private boolean naturalBorderThickness;

	public ModernItem() {
		this.roundness = 0.0d;
		this.borderThickness = 1.0d;
	}

	@SuppressWarnings("unchecked")
	public Property<Double, T> roundness() {
		return new SimpleProperty<Double, T>(() -> this.roundness, roundness -> {
			this.roundness = roundness;
			this.naturalRoundness = false;
		}, this :: onRoundnessUpdate, (T) this).nullable().whenNull(0.0d);
	}

	@SuppressWarnings("unchecked")
	public Property<Double, T> borderThickness() {
		return new SimpleProperty<Double, T>(() -> this.borderThickness, borderThickness -> {
			this.borderThickness = borderThickness;
			this.naturalBorderThickness = false;
		}, this :: onBorderThicknessUpdate, (T) this).nullable().whenNull(0.0d);
	}

	@SuppressWarnings("unchecked")
	public T naturalRoundness() {
		if(this.naturalRoundnessCalculator == null) {
			throw new UnsupportedOperationException("Natural roundness does not supported on this Item");
		}

		this.naturalRoundness = true;
		this.roundness = this.naturalRoundnessCalculator.calculate(this.bounds);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T naturalBorderThickness() {
		if(this.naturalBorderThicknessCalculator == null) {
			throw new UnsupportedOperationException("Natural roundness does not supported on this Item");
		}

		this.naturalBorderThickness = true;
		this.borderThickness = this.naturalBorderThicknessCalculator.calculate(this.bounds);
		return (T) this;
	}

	protected void onRoundnessUpdate(double roundness) {

	}

	protected void onBorderThicknessUpdate(double borderThickness) {

	}

	@Override
	protected void onResize() {
		if(this.naturalRoundness) {
			this.roundness = this.naturalRoundnessCalculator.calculate(this.bounds);
		}

		if(this.naturalBorderThickness) {
			this.borderThickness = this.naturalBorderThicknessCalculator.calculate(this.bounds);
		}
	}

	public static interface ValueCalculator<T> {
		public T calculate(Rectangle bounds);
	}
}
