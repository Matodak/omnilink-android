package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import java.util.List;

import net.homeip.mleclerc.omnilink.CommunicationException;
import net.homeip.mleclerc.omnilink.enumeration.NameTypeEnum;
import net.homeip.mleclerc.omnilink.message.MacroButtonCommand;
import net.homeip.mleclerc.omnilink.message.UploadNameMessageReport.NameInfo;
import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;

public class ButtonModelImpl extends BaseModelImpl implements ButtonModel {
	private final static String CACHE_FILE = "cachedButtonNames";
	
	private List<NameInfo> cachedButtonsName;
	
	public ButtonModelImpl(OmniLinkModelFactory factory) {
		super(factory);
	}
	
	@Override
	public int getButtonNumber(int index) {
		return cachedButtonsName.get(index).getObjectNo();
	}

	@Override
	public String getButtonName(int index) {
		return cachedButtonsName.get(index).getText();
	}

	@Override
	public int getButtonCount() {
		return cachedButtonsName.size();
	}

	@Override
	public boolean isLoaded() {
		return cachedButtonsName != null;
	}

	@Override
	public void load() throws ModelException {
		try {
			// Load the cached button names from the persistent cache if available or download from controller
			List<NameInfo> cachedButtonsName = factory.load(CACHE_FILE);
			this.cachedButtonsName = (cachedButtonsName != null) ? cachedButtonsName : getButtonsName();
		} catch(CommunicationException ex) {
			throw new ModelException(ex);
		}
	}

	@Override
	public void reset() {
		factory.delete(CACHE_FILE);
		
		cachedButtonsName = null;
	}
	
	@Override
	public void destroy() {
		factory.save(CACHE_FILE, cachedButtonsName);
	}

	@Override
	public boolean executeButton(int buttonNumber) throws ModelException {
		return execute(new MacroButtonCommand(buttonNumber));
	}
	
	private List<NameInfo> getButtonsName() throws CommunicationException{
		return getObjectsName(NameTypeEnum.BUTTON);
	}
}
