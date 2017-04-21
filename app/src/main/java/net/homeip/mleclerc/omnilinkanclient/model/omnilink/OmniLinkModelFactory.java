package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.NetworkCommunication;
import net.homeip.mleclerc.omnilink.NetworkCommunication.NotificationListener;
import net.homeip.mleclerc.omnilink.enumeration.ProtocolTypeEnum;
import net.homeip.mleclerc.omnilink.enumeration.SystemTypeEnum;
import net.homeip.mleclerc.omnilink.message.NotificationsCommand;
import net.homeip.mleclerc.omnilink.messagebase.ReplyMessage;
import net.homeip.mleclerc.omnilink.messagebase.RequestMessage;
import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.InformationModel;
import net.homeip.mleclerc.omnilinkanclient.model.MessageModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelFactory;
import net.homeip.mleclerc.omnilinkanclient.model.SystemModel;
import net.homeip.mleclerc.omnilinkanclient.model.ThermostatModel;
import net.homeip.mleclerc.omnilinkanclient.model.UnitModel;
import net.homeip.mleclerc.omnilinkanclient.model.ZoneModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class OmniLinkModelFactory implements ModelFactory {
	private final static SystemTypeEnum SYSTEM_TYPE = SystemTypeEnum.HAI_OMNI_IIE;
	private final static ProtocolTypeEnum PROTOCOL_TYPE = ProtocolTypeEnum.HAI_OMNI_LINK_II;
	private final static int KEEPALIVE_TIMEOUT = 30 * 1000;
	
	private Context context;
	private NetworkCommunication comm;
	private Timer keepAliveTimer = new Timer();
	private KeepAliveTimerTask keepAliveTimerTask;
	private String ipAddress;
	private int port;
	private int networkTimeout;
	private String privateKey;
	
	public OmniLinkModelFactory(Context context, SharedPreferences sharedPrefs) {
		this.context = context;
		
		ipAddress = sharedPrefs.getString("ip_address", "192.168.0.101");
		String portStr = sharedPrefs.getString("port", "4369");
		port = Integer.valueOf(portStr);
		String privateKey1 = sharedPrefs.getString("private_key_1", "a0-b1-c2-d3-e4-f5-a6-b7");
		String privateKey2 = sharedPrefs.getString("private_key_2", "c8-d9-e0-f1-a2-b3-c4-d5");
		privateKey = privateKey1 + '-' + privateKey2;
		String networkTimeoutStr = sharedPrefs.getString("network_timeout", "15");
		networkTimeout = Integer.valueOf(networkTimeoutStr);
	}

	@Override
	public SystemModel createSystemModel() {
		return new SystemModelImpl(this);
	}

	@Override
	public ThermostatModel createThermostatModel() {
		return new ThermostatModelImpl(this);
	}

	@Override
	public MessageModel createMessageModel() {
		return new MessageModelImpl(this);
	}

	@Override
	public ButtonModel createButtonModel() {
		return new ButtonModelImpl(this);
	}

	@Override
	public UnitModel createUnitModel() {
		return new UnitModelImpl(this);
	}

	@Override
	public ZoneModel createZoneModel() {
		return new ZoneModelImpl(this);
	}

	@Override
	public InformationModel createInformationModel() {
		return new InformationModelImpl(this);
	}

	@Override
	public Class getPreferenceActivityClass() {
		return OmniLinkPreferenceActivity.class;
	}
	
	@Override
	public void destroy() {
		try {
			closeCommunication();
		} catch (CommunicationException ex) {
		}
	}

	protected void delete(String filename) {
		context.deleteFile(filename);
	}
	
	protected <T> T load(String filename) {
		try {
			FileInputStream fis = context.openFileInput(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			T obj = (T) ois.readObject();
			ois.close();
			fis.close();
			
			return obj;
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
		}
		
		return null;
	}
	
	protected <T> void save(String filename, T obj) {
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch(IOException ex) {
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ReplyMessage> T execute(RequestMessage request) throws CommunicationException {
		return (T) getCommunication().execute(request);
	}

	protected NetworkCommunication getCommunication() throws CommunicationException {
		if (comm == null) {
			comm = new NetworkCommunication(SYSTEM_TYPE, ipAddress, port, networkTimeout, privateKey, PROTOCOL_TYPE);
		}

		if (!comm.isOpen()) {
			// Open communications
			comm.open();

			// Cancel previous keep alive timer 
			if (keepAliveTimerTask != null) {
				keepAliveTimerTask.cancel();
			}

			// Enable new keep alive timer
			keepAliveTimerTask = new KeepAliveTimerTask();
			keepAliveTimer.schedule(keepAliveTimerTask, 0, KEEPALIVE_TIMEOUT);
		}

		return comm;
	}

	protected void closeCommunication() throws CommunicationException {
		if (comm != null) {
			// Terminate keep alive timer
			if (keepAliveTimerTask != null) {
				keepAliveTimerTask.cancel();
				keepAliveTimerTask = null;
			}

			// Close communications
			if (comm.isOpen()) {
				new CloseNetworkCommunicationTask();
			}
			comm = null;
		}
	}

	protected void addNotificationListener(NotificationListener listener) {
		if (comm != null) {
			comm.addListener(listener);
		}
	}

	protected void removeNotificationListener(NotificationListener listener) {
		if (comm != null) {
			comm.removeListener(listener);
		}
	}

	private class KeepAliveTimerTask extends TimerTask {
		@Override
		public void run() {
			try {
				// Make sure communication is open before sending keep alive message
				if (comm != null && comm.isOpen()) {
					// Enable notifications
					execute(new NotificationsCommand(true));				
				}
			} catch(CommunicationException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private class CloseNetworkCommunicationTask extends AsyncTask<Void, Void, NetworkCommunication> {
		@Override
		protected NetworkCommunication doInBackground(Void... params) {
			try {
				comm.close();
				return comm;
			} catch (CommunicationException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
}
