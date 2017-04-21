package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

import net.homeip.mleclerc.omnilinkanclient.model.soap.util.MarshalDouble;
import net.homeip.mleclerc.omnilinkanclient.model.soap.util.PropertyHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.RemoteException;

public class AegisWebService {
	private String url = "{0}";
	private int timeOut = 60000;

	public void setTimeOut(int seconds) {
		this.timeOut = seconds * 1000;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SetUnitControlResponse setUnitControl(SetUnitControl setUnitControl) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setUnitControl");
			PropertyHelper.addProperties(soapReq, setUnitControl);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setUnitControl", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetUnitControlResponse resultVariable = new SetUnitControlResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public ShowMessageResponse showMessage(ShowMessage showMessage) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "showMessage");
			PropertyHelper.addProperties(soapReq, showMessage);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/showMessage", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			ShowMessageResponse resultVariable = new ShowMessageResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public BypassZoneResponse bypassZone(BypassZone bypassZone) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "bypassZone");
			PropertyHelper.addProperties(soapReq, bypassZone);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/bypassZone", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			BypassZoneResponse resultVariable = new BypassZoneResponse(result);
			return resultVariable;

		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public ClearMessageResponse clearMessage(ClearMessage clearMessage) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "clearMessage");
			PropertyHelper.addProperties(soapReq, clearMessage);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/clearMessage", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			ClearMessageResponse resultVariable = new ClearMessageResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public ExecuteButtonResponse executeButton(ExecuteButton executeButton) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "executeButton");
			PropertyHelper.addProperties(soapReq, executeButton);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/executeButton", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			ExecuteButtonResponse resultVariable = new ExecuteButtonResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetButtonsResponse getButtons(GetButtons getButtons) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getButtons");
			PropertyHelper.addProperties(soapReq, getButtons);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getButtons", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetButtonsResponse getButtonsResult = new GetButtonsResponse(result);
			return getButtonsResult;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetEventLogResponse getEventLog(GetEventLog getEventLog) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getEventLog");
			PropertyHelper.addProperties(soapReq, getEventLog);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getEventLog", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetEventLogResponse getEventLogResult = new GetEventLogResponse(result);
			return getEventLogResult;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetMessageStatusesResponse getMessageStatuses(GetMessageStatuses getMessageStatuses) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getMessageStatuses");
			PropertyHelper.addProperties(soapReq, getMessageStatuses);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getMessageStatuses", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetMessageStatusesResponse getMessageStatusesResult = new GetMessageStatusesResponse(result);
			return getMessageStatusesResult;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetSystemInformationResponse getSystemInformation(GetSystemInformation getSystemInformation)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getSystemInformation");
			PropertyHelper.addProperties(soapReq, getSystemInformation);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getSystemInformation", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetSystemInformationResponse resultVariable = new GetSystemInformationResponse(result);
			return resultVariable;

		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetSystemStatusResponse getSystemStatus(GetSystemStatus getSystemStatus) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getSystemStatus");
			PropertyHelper.addProperties(soapReq, getSystemStatus);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getSystemStatus", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetSystemStatusResponse resultVariable = new GetSystemStatusResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetThermostatsStatusResponse getThermostatsStatus(GetThermostatsStatus getThermostatsStatus)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getThermostatsStatus");
			PropertyHelper.addProperties(soapReq, getThermostatsStatus);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getThermostatsStatus", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetThermostatsStatusResponse resultVariable = new GetThermostatsStatusResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetUnitStatusesResponse getUnitStatuses(GetUnitStatuses getUnitStatuses) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getUnitStatuses");
			PropertyHelper.addProperties(soapReq, getUnitStatuses);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getUnitStatuses", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetUnitStatusesResponse getUnitStatusesResult = new GetUnitStatusesResponse(result);
			return getUnitStatusesResult;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public GetZoneStatusesResponse getZoneStatuses(GetZoneStatuses getZoneStatuses) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "getZoneStatuses");
			PropertyHelper.addProperties(soapReq, getZoneStatuses);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/getZoneStatuses", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			GetZoneStatusesResponse getZoneStatusesResult = new GetZoneStatusesResponse(result);
			return getZoneStatusesResult;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public LogMessageResponse logMessage(LogMessage logMessage) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "logMessage");
			PropertyHelper.addProperties(soapReq, logMessage);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/logMessage", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			LogMessageResponse resultVariable = new LogMessageResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public PlayMessageResponse playMessage(PlayMessage playMessage) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "playMessage");
			PropertyHelper.addProperties(soapReq, playMessage);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/playMessage", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			PlayMessageResponse resultVariable = new PlayMessageResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public RestoreZoneResponse restoreZone(RestoreZone restoreZone) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "restoreZone");
			PropertyHelper.addProperties(soapReq, restoreZone);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/restoreZone", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			RestoreZoneResponse resultVariable = new RestoreZoneResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetAllUnitsControlResponse setAllUnitsControl(SetAllUnitsControl setAllUnitsControl) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setAllUnitsControl");
			PropertyHelper.addProperties(soapReq, setAllUnitsControl);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setAllUnitsControl", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetAllUnitsControlResponse resultVariable = new SetAllUnitsControlResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetSecurityResponse setSecurity(SetSecurity setSecurity) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setSecurity");
			PropertyHelper.addProperties(soapReq, setSecurity);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setSecurity", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetSecurityResponse resultVariable = new SetSecurityResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetThermostatFanModeResponse setThermostatFanMode(SetThermostatFanMode setThermostatFanMode)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setThermostatFanMode");
			PropertyHelper.addProperties(soapReq, setThermostatFanMode);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setThermostatFanMode", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetThermostatFanModeResponse resultVariable = new SetThermostatFanModeResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetThermostatHighSetPointResponse setThermostatHighSetPoint(
			SetThermostatHighSetPoint setThermostatHighSetPoint) throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			MarshalDouble md = new MarshalDouble();
			md.register(soapEnvelope);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setThermostatHighSetPoint");
			PropertyHelper.addProperties(soapReq, setThermostatHighSetPoint);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setThermostatHighSetPoint", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetThermostatHighSetPointResponse resultVariable = new SetThermostatHighSetPointResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetThermostatHoldModeResponse setThermostatHoldMode(SetThermostatHoldMode setThermostatHoldMode)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setThermostatHoldMode");
			PropertyHelper.addProperties(soapReq, setThermostatHoldMode);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setThermostatHoldMode", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetThermostatHoldModeResponse resultVariable = new SetThermostatHoldModeResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetThermostatLowSetPointResponse setThermostatLowSetPoint(SetThermostatLowSetPoint setThermostatLowSetPoint)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			MarshalDouble md = new MarshalDouble();
			md.register(soapEnvelope);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setThermostatLowSetPoint");
			PropertyHelper.addProperties(soapReq, setThermostatLowSetPoint);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setThermostatLowSetPoint", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetThermostatLowSetPointResponse resultVariable = new SetThermostatLowSetPointResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}

	public SetThermostatSystemModeResponse setThermostatSystemMode(SetThermostatSystemMode setThermostatSystemMode)
			throws RemoteException {
		try {
			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			SoapObject soapReq = new SoapObject("http://webservice/", "setThermostatSystemMode");
			PropertyHelper.addProperties(soapReq, setThermostatSystemMode);
			soapEnvelope.setOutputSoapObject(soapReq);
			HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
			httpTransport.call("http://webservice/setThermostatSystemMode", soapEnvelope);
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			SetThermostatSystemModeResponse resultVariable = new SetThermostatSystemModeResponse(result);
			return resultVariable;
		} catch (Throwable e) {
			RemoteException rex = new RemoteException();
			rex.initCause(e);
			throw rex;
		}
	}
}
