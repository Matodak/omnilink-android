package net.homeip.mleclerc.omnilinkanclient.model.soap;

import net.homeip.mleclerc.omnilinkanclient.model.Model;

public abstract class BaseModelImpl implements Model {
	protected <T extends Enum<T>> T convert(Enum enum1, Class<T> enum2Class) {
		if (enum1 == null) {
			return null;
		}
		
		return Enum.valueOf(enum2Class, enum1.name());
	}
}
