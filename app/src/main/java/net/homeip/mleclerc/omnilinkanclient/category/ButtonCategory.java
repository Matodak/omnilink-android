package net.homeip.mleclerc.omnilinkanclient.category;

import java.text.MessageFormat;

import net.homeip.mleclerc.omnilinkanclient.R;
import net.homeip.mleclerc.omnilinkanclient.model.ButtonModel;
import net.homeip.mleclerc.omnilinkanclient.model.ModelException;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class ButtonCategory extends Category {
	private ButtonModel buttonModel;
	
	public ButtonCategory(Context context, ButtonModel buttonModel, DisplayMetrics displayMetrics) {
		super(context, displayMetrics);
		
		this.buttonModel = buttonModel;
	}

	@Override
	public void destroy() {
		super.destroy();
		
		buttonModel.destroy();
	}

	@Override
	public String getName() {
		return getContext().getString(R.string.BUTTONS);
	}

	@Override
	public View[] getViews() {
		int buttonCount = buttonModel.getButtonCount();
		View[] buttonViews = new View[buttonCount];
		for (int i = 0; i < buttonCount; i++) {
			final int buttonNumber = buttonModel.getButtonNumber(i);
			String buttonName = buttonModel.getButtonName(i);
			Execution exec = new Execution() {				
				public boolean execute(Object selection) throws ModelException {
					return buttonModel.executeButton(buttonNumber);
				}
			};
			View buttonView = createLabelAndButtonView(MessageFormat.format(getContext().getString(R.string.BUTTON_NAME), new Object[] {Integer.valueOf(buttonNumber), buttonName}), getContext().getString(R.string.BUTTON_EXECUTE), exec);
			buttonViews[i] = buttonView;
		}
		
		return buttonViews;
	}
	
	@Override
	public boolean isLoaded() {
		return buttonModel.isLoaded();
	}

	@Override
	public void reset() {
		buttonModel.reset();
	}
	
	@Override
	public void load() throws ModelException {
		buttonModel.load();
	}
}
