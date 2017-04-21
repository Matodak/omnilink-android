package net.homeip.mleclerc.omnilinkanclient.util;

public class EnumLabel<T> {
	private T enumValue;
	private String resourceString;

	public EnumLabel(T enumValue, String resourceString) {
		this.enumValue = enumValue;
		this.resourceString = resourceString;
	}

	public T getEnumValue() {
		return enumValue;
	}

	public String toString() {
		return resourceString;
	}
	
	public static <T> EnumLabel getEnumLabel(EnumLabel[] enumLabels, T enumValue) {
		for (EnumLabel enumLabel : enumLabels) {
			if (enumLabel.getEnumValue() == enumValue) {
				return enumLabel;
			}
		}
		return null;
	}

	public static <T> String getEnumLabelString(EnumLabel[] enumLabels, T enumValue) {
		EnumLabel enumLabel = getEnumLabel(enumLabels, enumValue);
		return (enumLabel != null) ? enumLabel.toString() : "";
	}
}
