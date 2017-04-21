package net.homeip.mleclerc.omnilinkanclient.model;

public interface Model {
	boolean isLoaded();
	
	void reset();

	void load() throws ModelException;
	
	void destroy();
}
