package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.EndOfDataException;
import net.homeip.mleclerc.omnilink.enumeration.NameTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.ObjectTypeEnum;
import net.homeip.mleclerc.omnilink.message.ObjectStatusReport;
import net.homeip.mleclerc.omnilink.message.ObjectStatusRequest;
import net.homeip.mleclerc.omnilink.message.ReadNameReport;
import net.homeip.mleclerc.omnilink.message.ReadNameRequest;
import net.homeip.mleclerc.omnilink.message.UploadNameMessageReport.NameInfo;
import net.homeip.mleclerc.omnilink.messagebase.AcknowledgeReplyMessage;
import net.homeip.mleclerc.omnilink.messagebase.CommandMessage;
import net.homeip.mleclerc.omnilink.messagebase.MultipleInfoReplyMessage;
import net.homeip.mleclerc.omnilink.messagebase.MultipleInfoReplyMessage.Info;
import net.homeip.mleclerc.omnilink.messagebase.ReplyMessage;
import net.homeip.mleclerc.omnilink.messagebase.RequestMessage;
import net.homeip.mleclerc.omnilinkanclient.model.Model;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;

public abstract class BaseModelImpl implements Model {
	private final static int MAX_OBJECTS_STATUS_REQUEST = 32;
	
	protected OmniLinkModelFactory factory;
	
	protected BaseModelImpl(OmniLinkModelFactory factory) {
		this.factory = factory;
	}
	
	protected boolean execute(CommandMessage command) throws ModelException {
		try {
			AcknowledgeReplyMessage response = (AcknowledgeReplyMessage) factory.execute(command);
			return (response != null);
		} catch (CommunicationException ex) {
			throw new ModelException(ex);
		}
	}
	
	protected <T extends ReplyMessage> T execute(RequestMessage request) throws CommunicationException {
		return factory.execute(request);
	}
	
	protected List<NameInfo> getObjectsName(NameTypeEnum nameType) throws CommunicationException {
		List<NameInfo> objectsName = new ArrayList<NameInfo>();
		
		int objectNo = 0;
		while (true) {
			try {
				ReadNameReport readNameResponse = execute(new ReadNameRequest(nameType, objectNo));
				NameInfo nameInfo = readNameResponse.getInfo();
				objectsName.add(nameInfo);
				objectNo = nameInfo.getObjectNo();
			} catch(EndOfDataException ex) {
				// Reached the end of the properties
				break;
			}
		}
		
		return objectsName;
	}
	
	protected <T extends Info> T getObjectStatus(ObjectTypeEnum objectType, int objectNo) throws CommunicationException {
		ObjectStatusReport objectStatusResponse = execute(new ObjectStatusRequest(objectType, objectNo));
		if (objectStatusResponse != null) {
			MultipleInfoReplyMessage objectStatusReport = objectStatusResponse.getObjectStatus();
			@SuppressWarnings("unchecked")
			T objectStatusInfo = (T) objectStatusReport.getInfo(objectNo);
			return objectStatusInfo;
		}
		
		return null;
	}

	protected <T extends Info> List<T> getObjectsStatusForNames(ObjectTypeEnum objectType, List<NameInfo> objectsName)  throws CommunicationException {
		List<T> objectsStatusInfo = new ArrayList<T>();
		
		int firstObjectNo = objectsName.get(0).getObjectNo();
		int lastObjectNo = objectsName.get(objectsName.size() - 1).getObjectNo();

		int currFirstObjectNo = firstObjectNo;
		while(true) {
			int currLastObjectNo = (lastObjectNo - currFirstObjectNo > MAX_OBJECTS_STATUS_REQUEST) ? currFirstObjectNo + MAX_OBJECTS_STATUS_REQUEST : lastObjectNo;
			ObjectStatusReport objectStatusResponse = execute(new ObjectStatusRequest(objectType, currFirstObjectNo, currLastObjectNo));
			if (objectStatusResponse != null) {
				MultipleInfoReplyMessage objectStatusReport = objectStatusResponse.getObjectStatus();
				for (NameInfo objectName : objectsName) {
					int objectNo = objectName.getObjectNo();
					try {
						@SuppressWarnings("unchecked")
						T objectStatusInfo = (T) objectStatusReport.getInfo(objectNo);
						objectsStatusInfo.add(objectStatusInfo);
					} catch(CommunicationException ex) {
						// Object not found
					}
				}
			}
			
			if (currLastObjectNo >= lastObjectNo) {
				break;
			}
			
			currFirstObjectNo = currLastObjectNo + 1;
		}

		return objectsStatusInfo;
	}

	protected <T extends Enum<T>> T convert(net.homeip.mleclerc.omnilink.enumeration.Enum enum1, Class<T> enum2Class) {
		if (enum1 == null) {
			return null;
		}

		return Enum.valueOf(enum2Class, getEnumAttributeName(enum1));
	}

	protected <T extends net.homeip.mleclerc.omnilink.enumeration.Enum> T convert(Enum enum1, Class<T> enum2Class) {
		if (enum1 == null) {
			return null;
		}

		return getEnumAttribute(enum2Class, enum1.name());
	}
	
	private String getEnumAttributeName(net.homeip.mleclerc.omnilink.enumeration.Enum enumSelected) {
		Class enumClass = enumSelected.getClass();
		Field[] fields = enumClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				Object fieldValue = field.get(null);
				if (fieldValue == enumSelected) {
					return field.getName();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends net.homeip.mleclerc.omnilink.enumeration.Enum> T getEnumAttribute(Class enumClass, String attributeName) {
		Field[] fields = enumClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.getName().equals(attributeName)) {
					return (T) field.get(null);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;		
	}
}
