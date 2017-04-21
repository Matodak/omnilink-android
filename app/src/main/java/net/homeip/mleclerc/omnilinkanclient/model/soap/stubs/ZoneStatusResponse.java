package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ZoneStatusResponse implements KvmSerializable {
	public ArmingStatus armingStatus;
	public ZoneCondition condition;
	public LatchedAlarmStatus latchedAlarmStatus;
	public String zoneName;
	public int zoneNumber;

	public ZoneStatusResponse() {
	}

	public ZoneStatusResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("armingStatus")) {
			Object obj = soapObject.getProperty("armingStatus");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j51 = (SoapPrimitive) soapObject.getProperty("armingStatus");
				armingStatus = ArmingStatus.fromString(j51.toString());
			}
		}
		if (soapObject.hasProperty("condition")) {
			Object obj = soapObject.getProperty("condition");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j53 = (SoapPrimitive) soapObject.getProperty("condition");
				condition = ZoneCondition.fromString(j53.toString());
			}
		}
		if (soapObject.hasProperty("latchedAlarmStatus")) {
			Object obj = soapObject.getProperty("latchedAlarmStatus");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j55 = (SoapPrimitive) soapObject.getProperty("latchedAlarmStatus");
				latchedAlarmStatus = LatchedAlarmStatus.fromString(j55.toString());
			}
		}
		if (soapObject.hasProperty("zoneName")) {
			Object obj = soapObject.getProperty("zoneName");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j57 = (SoapPrimitive) soapObject.getProperty("zoneName");
				zoneName = j57.toString();
			}
		}
		if (soapObject.hasProperty("zoneNumber")) {
			Object obj = soapObject.getProperty("zoneNumber");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j58 = (SoapPrimitive) soapObject.getProperty("zoneNumber");
				zoneNumber = Integer.parseInt(j58.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return armingStatus.toString();
		case 1:
			return condition.toString();
		case 2:
			return latchedAlarmStatus.toString();
		case 3:
			return zoneName;
		case 4:
			return zoneNumber;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 5;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "armingStatus";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "condition";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "latchedAlarmStatus";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "zoneName";
			break;
		case 4:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "zoneNumber";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			armingStatus = ArmingStatus.fromString(value.toString());
			break;
		case 1:
			condition = ZoneCondition.fromString(value.toString());
			break;
		case 2:
			latchedAlarmStatus = LatchedAlarmStatus.fromString(value.toString());
			break;
		case 3:
			zoneName = value.toString();
			break;
		case 4:
			zoneNumber = Integer.parseInt(value.toString());
			break;
		}
	}
}
