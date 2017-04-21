package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import net.homeip.mleclerc.omnilinkanclient.model.soap.util.PropertyHelper;
import net.homeip.mleclerc.omnilinkanclient.model.soap.util.VectorString;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class SystemInformationResponse implements KvmSerializable {
	public VectorString dialPhoneNumbers;
	public String localPhoneNumber;
	public SystemType systemType;
	public String version;

	public SystemInformationResponse() {
	}

	public SystemInformationResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("dialPhoneNumbers")) {
			SoapObject j83 = PropertyHelper.getProperties(soapObject, "dialPhoneNumbers"); // ML
			dialPhoneNumbers = new VectorString(j83);
		}
		if (soapObject.hasProperty("localPhoneNumber")) {
			Object obj = soapObject.getProperty("localPhoneNumber");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j84 = (SoapPrimitive) soapObject.getProperty("localPhoneNumber");
				localPhoneNumber = j84.toString();
			}
		}
		if (soapObject.hasProperty("systemType")) {
			Object obj = soapObject.getProperty("systemType");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j85 = (SoapPrimitive) soapObject.getProperty("systemType");
				systemType = SystemType.fromString(j85.toString());
			}
		}
		if (soapObject.hasProperty("version")) {
			Object obj = soapObject.getProperty("version");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j87 = (SoapPrimitive) soapObject.getProperty("version");
				version = j87.toString();
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return dialPhoneNumbers;
		case 1:
			return localPhoneNumber;
		case 2:
			return systemType.toString();
		case 3:
			return version;
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
			info.type = PropertyInfo.VECTOR_CLASS;
			info.name = "dialPhoneNumbers";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "localPhoneNumber";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "systemType";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "version";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			dialPhoneNumbers = (VectorString) value;
			break;
		case 1:
			localPhoneNumber = value.toString();
			break;
		case 2:
			systemType = SystemType.fromString(value.toString());
			break;
		case 3:
			version = value.toString();
			break;
		}
	}
}
