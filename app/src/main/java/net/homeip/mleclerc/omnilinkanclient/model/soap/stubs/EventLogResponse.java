package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class EventLogResponse implements KvmSerializable {
	public long date;
	public EventType eventType;
	public int p1;
	public int p2;

	public EventLogResponse() {
	}

	public EventLogResponse(SoapObject soapObject) {

		if (soapObject.hasProperty("date")) {
			Object obj = soapObject.getProperty("date");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j92 = (SoapPrimitive) soapObject.getProperty("date");
				date = Long.parseLong(j92.toString());
			}
		}
		if (soapObject.hasProperty("eventType")) {
			Object obj = soapObject.getProperty("eventType");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j93 = (SoapPrimitive) soapObject.getProperty("eventType");
				eventType = EventType.fromString(j93.toString());
			}
		}
		if (soapObject.hasProperty("p1")) {
			Object obj = soapObject.getProperty("p1");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j95 = (SoapPrimitive) soapObject.getProperty("p1");
				p1 = Integer.parseInt(j95.toString());
			}
		}
		if (soapObject.hasProperty("p2")) {
			Object obj = soapObject.getProperty("p2");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j96 = (SoapPrimitive) soapObject.getProperty("p2");
				p2 = Integer.parseInt(j96.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return date;
		case 1:
			return eventType.toString();
		case 2:
			return p1;
		case 3:
			return p2;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = Long.class;
			info.name = "date";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "eventType";
			break;
		case 2:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "p1";
			break;
		case 3:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "p2";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			date = Long.parseLong(value.toString());
			break;
		case 1:
			eventType = EventType.fromString(value.toString());
			break;
		case 2:
			p1 = Integer.parseInt(value.toString());
			break;
		case 3:
			p2 = Integer.parseInt(value.toString());
			break;
		}
	}
}
