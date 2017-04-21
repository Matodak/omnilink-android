package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class SystemStatusResponse implements KvmSerializable {
    public long date;
    public PhoneLineStatus phoneLineStatus;
    public SecurityMode securityMode;
    public long sunrise;
    public long sunset;
    public SystemStatus systemStatus;
    
    public SystemStatusResponse(){}
    
	public SystemStatusResponse(SoapObject soapObject){
        if (soapObject.hasProperty("date"))
        {
            Object obj = soapObject.getProperty("date");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j73 =(SoapPrimitive) soapObject.getProperty("date");
                date = Long.parseLong(j73.toString());
            }
        }
        if (soapObject.hasProperty("phoneLineStatus"))
        {
            Object obj = soapObject.getProperty("phoneLineStatus");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j74 =(SoapPrimitive) soapObject.getProperty("phoneLineStatus");
                phoneLineStatus = PhoneLineStatus.fromString(j74.toString());
            }
        }
        if (soapObject.hasProperty("securityMode"))
        {
            Object obj = soapObject.getProperty("securityMode");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j76 =(SoapPrimitive) soapObject.getProperty("securityMode");
                securityMode = SecurityMode.fromString(j76.toString());
            }
        }
        if (soapObject.hasProperty("sunrise"))
        {
            Object obj = soapObject.getProperty("sunrise");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j78 =(SoapPrimitive) soapObject.getProperty("sunrise");
                sunrise = Long.parseLong(j78.toString());
            }
        }
        if (soapObject.hasProperty("sunset"))
        {
            Object obj = soapObject.getProperty("sunset");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j79 =(SoapPrimitive) soapObject.getProperty("sunset");
                sunset = Long.parseLong(j79.toString());
            }
        }
        if (soapObject.hasProperty("systemStatus"))
        {
            Object obj = soapObject.getProperty("systemStatus");
            if (obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j80 =(SoapPrimitive) soapObject.getProperty("systemStatus");
                systemStatus = SystemStatus.fromString(j80.toString());
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
    switch(arg0){
        case 0:
            return date;
        case 1:
            return phoneLineStatus.toString();
        case 2:
            return securityMode.toString();
        case 3:
            return sunrise;
        case 4:
            return sunset;
        case 5:
            return systemStatus.toString();
    }
    return null;
    }
    @Override
    public int getPropertyCount() {
        return 6;
    }
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
    switch(index){
        case 0:
            info.type = Long.class;
            info.name = "date";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "phoneLineStatus";
            break;
        case 2:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "securityMode";
            break;
        case 3:
            info.type = Long.class;
            info.name = "sunrise";
            break;
        case 4:
            info.type = Long.class;
            info.name = "sunset";
            break;
        case 5:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "systemStatus";
            break;
    }
    }
	@Override
    public void setProperty(int index, Object value) {
    switch(index){
        case 0:
        date = Long.parseLong(value.toString()) ;
        break;
        case 1:
        phoneLineStatus = PhoneLineStatus.fromString(value.toString());
        break;
        case 2:
        securityMode = SecurityMode.fromString(value.toString());
        break;
        case 3:
        sunrise = Long.parseLong(value.toString()) ;
        break;
        case 4:
        sunset = Long.parseLong(value.toString()) ;
        break;
        case 5:
        systemStatus = SystemStatus.fromString(value.toString());
        break;
}
}
}
