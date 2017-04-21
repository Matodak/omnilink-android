package net.homeip.mleclerc.omnilinkanclient.model.soap.util;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PropertyHelper {
	public static SoapObject getProperties(SoapObject soapObject, String propertyName) {
		SoapObject vectorSoapObject = new SoapObject("", "");
		int size = soapObject.getPropertyCount();
		for (int i = 0; i < size; i++) {
			PropertyInfo propertyInfo = new PropertyInfo();
			soapObject.getPropertyInfo(i, propertyInfo);
			if (propertyInfo.getName().equals(propertyName)) {
				Object obj = soapObject.getProperty(i);
				if (obj.getClass().equals(SoapPrimitive.class)) {
					vectorSoapObject.addProperty(propertyName, obj);
				}
			}
		}

		return vectorSoapObject;
	}

	public static void addProperties(SoapObject soapObj, KvmSerializable obj) {
		for (int i = 0; i < obj.getPropertyCount(); i++) {
			PropertyInfo propertyInfo = new PropertyInfo();
			obj.getPropertyInfo(i, null, propertyInfo);
			String propertyName = propertyInfo.getName();
			Object propertyValue = obj.getProperty(i);
			soapObj.addProperty(propertyName, propertyValue);
		}
	}
}
