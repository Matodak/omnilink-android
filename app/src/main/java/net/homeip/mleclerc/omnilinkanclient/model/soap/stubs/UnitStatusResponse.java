package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class UnitStatusResponse implements KvmSerializable {
	public UnitControl condition;
	public String unitName;
	public int unitNumber;

	public UnitStatusResponse() {
	}

	public UnitStatusResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("condition")) {
			Object obj = soapObject.getProperty("condition");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j59 = (SoapPrimitive) soapObject.getProperty("condition");
				condition = UnitControl.fromString(j59.toString());
			}
		}
		if (soapObject.hasProperty("unitName")) {
			Object obj = soapObject.getProperty("unitName");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j61 = (SoapPrimitive) soapObject.getProperty("unitName");
				unitName = j61.toString();
			}
		}
		if (soapObject.hasProperty("unitNumber")) {
			Object obj = soapObject.getProperty("unitNumber");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j62 = (SoapPrimitive) soapObject.getProperty("unitNumber");
				unitNumber = Integer.parseInt(j62.toString());
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return condition.toString();
		case 1:
			return unitName;
		case 2:
			return unitNumber;
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
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "condition";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "unitName";
			break;
		case 2:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "unitNumber";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			condition = UnitControl.fromString(value.toString());
			break;
		case 1:
			unitName = value.toString();
			break;
		case 2:
			unitNumber = Integer.parseInt(value.toString());
			break;
		}
	}
}
