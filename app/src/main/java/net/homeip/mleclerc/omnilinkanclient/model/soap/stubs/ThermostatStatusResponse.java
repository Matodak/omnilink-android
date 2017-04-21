package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ThermostatStatusResponse implements KvmSerializable {
	public double currentTemperature;
	public FanMode fanMode;
	public double highSetPoint;
	public HoldMode holdMode;
	public double lowSetPoint;
	public SystemMode systemMode;

	public ThermostatStatusResponse() {
	}

	public ThermostatStatusResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("currentTemperature")) {
			Object obj = soapObject.getProperty("currentTemperature");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j63 = (SoapPrimitive) soapObject.getProperty("currentTemperature");
				currentTemperature = Double.parseDouble(j63.toString());
			}
		}
		if (soapObject.hasProperty("fanMode")) {
			Object obj = soapObject.getProperty("fanMode");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j64 = (SoapPrimitive) soapObject.getProperty("fanMode");
				fanMode = FanMode.fromString(j64.toString());
			}
		}
		if (soapObject.hasProperty("highSetPoint")) {
			Object obj = soapObject.getProperty("highSetPoint");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j66 = (SoapPrimitive) soapObject.getProperty("highSetPoint");
				highSetPoint = Double.parseDouble(j66.toString());
			}
		}
		if (soapObject.hasProperty("holdMode")) {
			Object obj = soapObject.getProperty("holdMode");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j67 = (SoapPrimitive) soapObject.getProperty("holdMode");
				holdMode = HoldMode.fromString(j67.toString());
			}
		}
		if (soapObject.hasProperty("lowSetPoint")) {
			Object obj = soapObject.getProperty("lowSetPoint");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j69 = (SoapPrimitive) soapObject.getProperty("lowSetPoint");
				lowSetPoint = Double.parseDouble(j69.toString());
			}
		}
		if (soapObject.hasProperty("systemMode")) {
			Object obj = soapObject.getProperty("systemMode");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j70 = (SoapPrimitive) soapObject.getProperty("systemMode");
				systemMode = SystemMode.fromString(j70.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return currentTemperature;
		case 1:
			return fanMode.toString();
		case 2:
			return highSetPoint;
		case 3:
			return holdMode.toString();
		case 4:
			return lowSetPoint;
		case 5:
			return systemMode.toString();
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 6;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = Double.class;
			info.name = "currentTemperature";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "fanMode";
			break;
		case 2:
			info.type = Double.class;
			info.name = "highSetPoint";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "holdMode";
			break;
		case 4:
			info.type = Double.class;
			info.name = "lowSetPoint";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "systemMode";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			currentTemperature = Double.parseDouble(value.toString());
			break;
		case 1:
			fanMode = FanMode.fromString(value.toString());
			break;
		case 2:
			highSetPoint = Double.parseDouble(value.toString());
			break;
		case 3:
			holdMode = HoldMode.fromString(value.toString());
			break;
		case 4:
			lowSetPoint = Double.parseDouble(value.toString());
			break;
		case 5:
			systemMode = SystemMode.fromString(value.toString());
			break;
		}
	}
}
