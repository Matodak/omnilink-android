package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class GetSystemStatusResponse implements KvmSerializable {
	public SystemStatusResponse _return;

	public GetSystemStatusResponse() {
	}

	public GetSystemStatusResponse(SoapObject soapObject) {

		if (soapObject.hasProperty("return")) {
			SoapObject j82 = (SoapObject) soapObject.getProperty("return");
			_return = new SystemStatusResponse(j82);

		}
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return _return;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 1;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = SystemStatusResponse.class;
			info.name = "return";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			_return = (SystemStatusResponse) value;
			break;
		}
	}
}
