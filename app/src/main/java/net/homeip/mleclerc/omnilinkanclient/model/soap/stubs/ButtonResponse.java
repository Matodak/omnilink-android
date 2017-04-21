package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ButtonResponse implements KvmSerializable {
	public String buttonName;
	public int buttonNumber;

	public ButtonResponse() {
	}

	public ButtonResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("buttonName")) {
			Object obj = soapObject.getProperty("buttonName");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j98 = (SoapPrimitive) soapObject.getProperty("buttonName");
				buttonName = j98.toString();
			}
		}
		if (soapObject.hasProperty("buttonNumber")) {
			Object obj = soapObject.getProperty("buttonNumber");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j99 = (SoapPrimitive) soapObject.getProperty("buttonNumber");
				buttonNumber = Integer.parseInt(j99.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return buttonName;
		case 1:
			return buttonNumber;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "buttonName";
			break;
		case 1:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "buttonNumber";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			buttonName = value.toString();
			break;
		case 1:
			buttonNumber = Integer.parseInt(value.toString());
			break;
		}
	}
}
