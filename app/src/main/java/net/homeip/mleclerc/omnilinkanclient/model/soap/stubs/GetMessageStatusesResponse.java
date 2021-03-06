package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

@SuppressWarnings("serial")
public class GetMessageStatusesResponse extends Vector<MessageStatusResponse> implements KvmSerializable {

	public GetMessageStatusesResponse() {
	}

	public GetMessageStatusesResponse(SoapObject soapObject) {
		if (soapObject != null) {
			int size = soapObject.getPropertyCount();
			for (int i0 = 0; i0 < size; i0++) {
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapObject.class)) {
					SoapObject j0 = (SoapObject) soapObject.getProperty(i0);
					MessageStatusResponse j1 = new MessageStatusResponse(j0);
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
		arg2.name = "messageStatusResponse";
		arg2.type = MessageStatusResponse.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((MessageStatusResponse) arg1);
	}

}
