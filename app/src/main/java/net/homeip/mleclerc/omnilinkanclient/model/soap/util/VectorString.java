package net.homeip.mleclerc.omnilinkanclient.model.soap.util;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

@SuppressWarnings("serial")
public class VectorString extends Vector<String> implements KvmSerializable {

	public VectorString() {
	}

	public VectorString(SoapObject soapObject) {
		if (soapObject != null) {
			int size = soapObject.getPropertyCount();
			for (int i0 = 0; i0 < size; i0++) {
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapPrimitive.class)) {
					SoapPrimitive j0 = (SoapPrimitive) soapObject.getProperty(i0);
					String j1 = j0.toString();
					add(j1);
				}
			}
		}
	}

	@Override
	public Object getProperty(int arg0) {
		return this.get(arg0);
	}

	@Override
	public int getPropertyCount() {
		return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		arg2.name = "string";
		arg2.type = String.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((String) arg1);
	}

}
