package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ClearMessageResponse implements KvmSerializable {
	public boolean _return;

	public ClearMessageResponse() {
	}

	public ClearMessageResponse(SoapObject soapObject) {
		if (soapObject.hasProperty("return")) {
			Object obj = soapObject.getProperty("return");
			if (obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j102 = (SoapPrimitive) soapObject.getProperty("return");
				_return = Boolean.parseBoolean(j102.toString());
			}
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
			info.type = PropertyInfo.BOOLEAN_CLASS;
			info.name = "return";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			_return = Boolean.parseBoolean(value.toString());
			break;
		}
	}
}
