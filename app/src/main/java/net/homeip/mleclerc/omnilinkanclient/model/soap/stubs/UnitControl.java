package net.homeip.mleclerc.omnilinkanclient.model.soap.stubs;

public enum UnitControl {
	OFF(0), ON(1), DIM_1(2), DIM_2(3), DIM_3(4), DIM_4(5), DIM_5(6), DIM_6(7), DIM_7(8), DIM_8(9), DIM_9(10), BRIGHT_1(
			11), BRIGHT_2(12), BRIGHT_3(13), BRIGHT_4(14), BRIGHT_5(15), BRIGHT_6(16), BRIGHT_7(17), BRIGHT_8(18), BRIGHT_9(
			19), LEVEL_0(20), LEVEL_1(21), LEVEL_2(22), LEVEL_3(23), LEVEL_4(24), LEVEL_5(25), LEVEL_6(26), LEVEL_7(27), LEVEL_8(
			28), LEVEL_9(29), LEVEL_10(30), LEVEL_11(31), LEVEL_12(32), LEVEL_13(33), LEVEL_14(34), LEVEL_15(35), LEVEL_16(
			36), LEVEL_17(37), LEVEL_18(38), LEVEL_19(39), LEVEL_20(40), LEVEL_21(41), LEVEL_22(42), LEVEL_23(43), LEVEL_24(
			44), LEVEL_25(45), LEVEL_26(46), LEVEL_27(47), LEVEL_28(48), LEVEL_29(49), LEVEL_30(50), LEVEL_31(51), LEVEL_32(
			52), LEVEL_33(53), LEVEL_34(54), LEVEL_35(55), LEVEL_36(56), LEVEL_37(57), LEVEL_38(58), LEVEL_39(59), LEVEL_40(
			60), LEVEL_41(61), LEVEL_42(62), LEVEL_43(63), LEVEL_44(64), LEVEL_45(65), LEVEL_46(66), LEVEL_47(67), LEVEL_48(
			68), LEVEL_49(69), LEVEL_50(70), LEVEL_51(71), LEVEL_52(72), LEVEL_53(73), LEVEL_54(74), LEVEL_55(75), LEVEL_56(
			76), LEVEL_57(77), LEVEL_58(78), LEVEL_59(79), LEVEL_60(80), LEVEL_61(81), LEVEL_62(82), LEVEL_63(83), LEVEL_64(
			84), LEVEL_65(85), LEVEL_66(86), LEVEL_67(87), LEVEL_68(88), LEVEL_69(89), LEVEL_70(90), LEVEL_71(91), LEVEL_72(
			92), LEVEL_73(93), LEVEL_74(94), LEVEL_75(95), LEVEL_76(96), LEVEL_77(97), LEVEL_78(98), LEVEL_79(99), LEVEL_80(
			100), LEVEL_81(101), LEVEL_82(102), LEVEL_83(103), LEVEL_84(104), LEVEL_85(105), LEVEL_86(106), LEVEL_87(
			107), LEVEL_88(108), LEVEL_89(109), LEVEL_90(110), LEVEL_91(111), LEVEL_92(112), LEVEL_93(113), LEVEL_94(
			114), LEVEL_95(115), LEVEL_96(116), LEVEL_97(117), LEVEL_98(118), LEVEL_99(119), LEVEL_100(120);

	private int code;

	UnitControl(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static UnitControl fromString(String str) {
		if (str.equals("OFF"))
			return OFF;
		if (str.equals("ON"))
			return ON;
		if (str.equals("DIM_1"))
			return DIM_1;
		if (str.equals("DIM_2"))
			return DIM_2;
		if (str.equals("DIM_3"))
			return DIM_3;
		if (str.equals("DIM_4"))
			return DIM_4;
		if (str.equals("DIM_5"))
			return DIM_5;
		if (str.equals("DIM_6"))
			return DIM_6;
		if (str.equals("DIM_7"))
			return DIM_7;
		if (str.equals("DIM_8"))
			return DIM_8;
		if (str.equals("DIM_9"))
			return DIM_9;
		if (str.equals("BRIGHT_1"))
			return BRIGHT_1;
		if (str.equals("BRIGHT_2"))
			return BRIGHT_2;
		if (str.equals("BRIGHT_3"))
			return BRIGHT_3;
		if (str.equals("BRIGHT_4"))
			return BRIGHT_4;
		if (str.equals("BRIGHT_5"))
			return BRIGHT_5;
		if (str.equals("BRIGHT_6"))
			return BRIGHT_6;
		if (str.equals("BRIGHT_7"))
			return BRIGHT_7;
		if (str.equals("BRIGHT_8"))
			return BRIGHT_8;
		if (str.equals("BRIGHT_9"))
			return BRIGHT_9;
		if (str.equals("LEVEL_0"))
			return LEVEL_0;
		if (str.equals("LEVEL_1"))
			return LEVEL_1;
		if (str.equals("LEVEL_2"))
			return LEVEL_2;
		if (str.equals("LEVEL_3"))
			return LEVEL_3;
		if (str.equals("LEVEL_4"))
			return LEVEL_4;
		if (str.equals("LEVEL_5"))
			return LEVEL_5;
		if (str.equals("LEVEL_6"))
			return LEVEL_6;
		if (str.equals("LEVEL_7"))
			return LEVEL_7;
		if (str.equals("LEVEL_8"))
			return LEVEL_8;
		if (str.equals("LEVEL_9"))
			return LEVEL_9;
		if (str.equals("LEVEL_10"))
			return LEVEL_10;
		if (str.equals("LEVEL_11"))
			return LEVEL_11;
		if (str.equals("LEVEL_12"))
			return LEVEL_12;
		if (str.equals("LEVEL_13"))
			return LEVEL_13;
		if (str.equals("LEVEL_14"))
			return LEVEL_14;
		if (str.equals("LEVEL_15"))
			return LEVEL_15;
		if (str.equals("LEVEL_16"))
			return LEVEL_16;
		if (str.equals("LEVEL_17"))
			return LEVEL_17;
		if (str.equals("LEVEL_18"))
			return LEVEL_18;
		if (str.equals("LEVEL_19"))
			return LEVEL_19;
		if (str.equals("LEVEL_20"))
			return LEVEL_20;
		if (str.equals("LEVEL_21"))
			return LEVEL_21;
		if (str.equals("LEVEL_22"))
			return LEVEL_22;
		if (str.equals("LEVEL_23"))
			return LEVEL_23;
		if (str.equals("LEVEL_24"))
			return LEVEL_24;
		if (str.equals("LEVEL_25"))
			return LEVEL_25;
		if (str.equals("LEVEL_26"))
			return LEVEL_26;
		if (str.equals("LEVEL_27"))
			return LEVEL_27;
		if (str.equals("LEVEL_28"))
			return LEVEL_28;
		if (str.equals("LEVEL_29"))
			return LEVEL_29;
		if (str.equals("LEVEL_30"))
			return LEVEL_30;
		if (str.equals("LEVEL_31"))
			return LEVEL_31;
		if (str.equals("LEVEL_32"))
			return LEVEL_32;
		if (str.equals("LEVEL_33"))
			return LEVEL_33;
		if (str.equals("LEVEL_34"))
			return LEVEL_34;
		if (str.equals("LEVEL_35"))
			return LEVEL_35;
		if (str.equals("LEVEL_36"))
			return LEVEL_36;
		if (str.equals("LEVEL_37"))
			return LEVEL_37;
		if (str.equals("LEVEL_38"))
			return LEVEL_38;
		if (str.equals("LEVEL_39"))
			return LEVEL_39;
		if (str.equals("LEVEL_40"))
			return LEVEL_40;
		if (str.equals("LEVEL_41"))
			return LEVEL_41;
		if (str.equals("LEVEL_42"))
			return LEVEL_42;
		if (str.equals("LEVEL_43"))
			return LEVEL_43;
		if (str.equals("LEVEL_44"))
			return LEVEL_44;
		if (str.equals("LEVEL_45"))
			return LEVEL_45;
		if (str.equals("LEVEL_46"))
			return LEVEL_46;
		if (str.equals("LEVEL_47"))
			return LEVEL_47;
		if (str.equals("LEVEL_48"))
			return LEVEL_48;
		if (str.equals("LEVEL_49"))
			return LEVEL_49;
		if (str.equals("LEVEL_50"))
			return LEVEL_50;
		if (str.equals("LEVEL_51"))
			return LEVEL_51;
		if (str.equals("LEVEL_52"))
			return LEVEL_52;
		if (str.equals("LEVEL_53"))
			return LEVEL_53;
		if (str.equals("LEVEL_54"))
			return LEVEL_54;
		if (str.equals("LEVEL_55"))
			return LEVEL_55;
		if (str.equals("LEVEL_56"))
			return LEVEL_56;
		if (str.equals("LEVEL_57"))
			return LEVEL_57;
		if (str.equals("LEVEL_58"))
			return LEVEL_58;
		if (str.equals("LEVEL_59"))
			return LEVEL_59;
		if (str.equals("LEVEL_60"))
			return LEVEL_60;
		if (str.equals("LEVEL_61"))
			return LEVEL_61;
		if (str.equals("LEVEL_62"))
			return LEVEL_62;
		if (str.equals("LEVEL_63"))
			return LEVEL_63;
		if (str.equals("LEVEL_64"))
			return LEVEL_64;
		if (str.equals("LEVEL_65"))
			return LEVEL_65;
		if (str.equals("LEVEL_66"))
			return LEVEL_66;
		if (str.equals("LEVEL_67"))
			return LEVEL_67;
		if (str.equals("LEVEL_68"))
			return LEVEL_68;
		if (str.equals("LEVEL_69"))
			return LEVEL_69;
		if (str.equals("LEVEL_70"))
			return LEVEL_70;
		if (str.equals("LEVEL_71"))
			return LEVEL_71;
		if (str.equals("LEVEL_72"))
			return LEVEL_72;
		if (str.equals("LEVEL_73"))
			return LEVEL_73;
		if (str.equals("LEVEL_74"))
			return LEVEL_74;
		if (str.equals("LEVEL_75"))
			return LEVEL_75;
		if (str.equals("LEVEL_76"))
			return LEVEL_76;
		if (str.equals("LEVEL_77"))
			return LEVEL_77;
		if (str.equals("LEVEL_78"))
			return LEVEL_78;
		if (str.equals("LEVEL_79"))
			return LEVEL_79;
		if (str.equals("LEVEL_80"))
			return LEVEL_80;
		if (str.equals("LEVEL_81"))
			return LEVEL_81;
		if (str.equals("LEVEL_82"))
			return LEVEL_82;
		if (str.equals("LEVEL_83"))
			return LEVEL_83;
		if (str.equals("LEVEL_84"))
			return LEVEL_84;
		if (str.equals("LEVEL_85"))
			return LEVEL_85;
		if (str.equals("LEVEL_86"))
			return LEVEL_86;
		if (str.equals("LEVEL_87"))
			return LEVEL_87;
		if (str.equals("LEVEL_88"))
			return LEVEL_88;
		if (str.equals("LEVEL_89"))
			return LEVEL_89;
		if (str.equals("LEVEL_90"))
			return LEVEL_90;
		if (str.equals("LEVEL_91"))
			return LEVEL_91;
		if (str.equals("LEVEL_92"))
			return LEVEL_92;
		if (str.equals("LEVEL_93"))
			return LEVEL_93;
		if (str.equals("LEVEL_94"))
			return LEVEL_94;
		if (str.equals("LEVEL_95"))
			return LEVEL_95;
		if (str.equals("LEVEL_96"))
			return LEVEL_96;
		if (str.equals("LEVEL_97"))
			return LEVEL_97;
		if (str.equals("LEVEL_98"))
			return LEVEL_98;
		if (str.equals("LEVEL_99"))
			return LEVEL_99;
		if (str.equals("LEVEL_100"))
			return LEVEL_100;
		return null;
	}
}