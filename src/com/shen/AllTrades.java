package com.shen;

import java.util.HashMap;

public abstract class AllTrades {
	public static HashMap<String, String> tradeShortAndName = init();

	@SuppressWarnings("unused")
    public static void main(String[] args) {
		AllTrades allTrades = new AllTrades() {
		};
	}

	private static HashMap<String, String> init() {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		String shorter = "bsjd bdtjyj bzys bxjqt cjfw cm dl dqsb dzzz fdckf fzzz fqcjy fzjf gt gkhy gj gltlys gxgdz gfjg hghccl hgxcl hxzp hxzy hbgc jchy jchx jsjsb jsjyy jyqg jzcl jzzs jysbfw jdjly jdjcy ls my mtkc ncpjg nyfw qtdz qclbj qczc rqsw swzp sykykc spjgzz stqc txfw txsb tysb wl xcl yzy ylqxfw yysy yqyb yx ylzz ysyljg yqkf zz zq zy zzyyly zysb zh";
		String name = "白色家电 半导体及元件 包装印刷 保险及其他 采掘服务 传媒 电力 电气设备 电子制造 房地产开发 纺织制造 非汽车交运 服装家纺 钢铁 港口航运 公交 公路铁路运输 光学光电子 国防军工 化工合成材料 化工新材料 化学制品 化学制药 环保工程 机场航运 基础化学 计算机设备 计算机应用 家用轻工 建筑材料 建筑装饰 交运设备服务 景点及旅游 酒店及餐饮 零售 贸易 煤炭开采 农产品加工 农业服务 其他电子 汽车零部件 汽车整车 燃气水务 生物制品 石油矿业开采 食品加工制造 视听器材 通信服务 通信设备 通用设备 物流 新材料 养殖业 医疗器械服务 医药商业 仪器仪表 银行 饮料制造 有色冶炼加工 园区开发 造纸 证券 中药 种植业与林业 专用设备 综合";
		String[] shorterArray = shorter.split(" ");
		String[] nameArray = name.split(" ");
		int shorterLen = shorterArray.length;
		int nameLen = nameArray.length;

		if (shorterLen == nameLen) {
			for (int i = 0; i < shorterLen; i++) {
				hashMap.put(shorterArray[i], nameArray[i]);
				hashMap.put(nameArray[i], shorterArray[i]);
			}
		}
		System.out.println(hashMap);
		return hashMap;
	}

	public HashMap<String, String> getTradeShortAndName() {
		return tradeShortAndName;
	}

	public void setTradeShortAndName(HashMap<String, String> tradeShortAndName) {
		AllTrades.tradeShortAndName = tradeShortAndName;
	}

}
