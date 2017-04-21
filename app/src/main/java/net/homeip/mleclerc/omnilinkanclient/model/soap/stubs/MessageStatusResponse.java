package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class MessageStatusResponse implements KvmSerializable {
	public boolean displayed;
	public String messageName;
	public int messageNumber;

	public MessageStatusResponse() {
	}

	public MessageStatusResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("displayed")) {
			Object obj = soapObject.getProperty("displayed");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j89 = (SoapPrimitive) soapObject.getProperty("displayed");
				displayed = Boolean.parseBoolean(j89.toString());
			}
		}
		if (soapObject.hasProperty("messageName")) {
			Object obj = soapObject.getProperty("messageName");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j90 = (SoapPrimitive) soapObject.getProperty("messageName");
				messageName = j90.toString();
			}
		}
		if (soapObject.hasProperty("messageNumber")) {
			Object obj = soapObject.getProperty("messageNumber");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j91 = (SoapPrimitive) soapObject.getProperty("messageNumber");
				messageNumber = Integer.parseInt(j91.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return displayed;
		case 1:
			return messageName;
		case 2:
			return messageNumber;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.BOOLEAN_CLASS;
			info.name = "displayed";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "messageName";
			break;
		case 2:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "messageNumber";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			displayed = Boolean.parseBoolean(value.toString());
			break;
		case 1:
			messageName = value.toString();
			break;
		case 2:
			messageNumber = Integer.parseInt(value.toString());
			break;
		}
	}
}
