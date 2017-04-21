package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum SystemType {
	HAI_OMNI(0), HAI_OMNI_PRO(1), HAI_OMNI_PRO_II(2), HAI_OMNI_LT(3), HAI_OMNI_II(4), HAI_OMNI_IIE(5), HAI_OMNI_IIE_B(6), AEGIS_2000(
			7), AEGIS(8);

	private int code;

	SystemType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SystemType fromString(String str) {
		if (str.equals("HAI_OMNI"))
			return HAI_OMNI;
		if (str.equals("HAI_OMNI_PRO"))
			return HAI_OMNI_PRO;
		if (str.equals("HAI_OMNI_PRO_II"))
			return HAI_OMNI_PRO_II;
		if (str.equals("HAI_OMNI_LT"))
			return HAI_OMNI_LT;
		if (str.equals("HAI_OMNI_II"))
			return HAI_OMNI_II;
		if (str.equals("HAI_OMNI_IIE"))
			return HAI_OMNI_IIE;
		if (str.equals("HAI_OMNI_IIE_B"))
			return HAI_OMNI_IIE_B;
		if (str.equals("AEGIS_2000"))
			return AEGIS_2000;
		if (str.equals("AEGIS"))
			return AEGIS;
		return null;
	}
}