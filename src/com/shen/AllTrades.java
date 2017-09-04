package com.shen;

import java.util.HashMap;

/**
 * 提供所有同花顺行业，会根据同花顺网页的变化而改动
 * 
 * @author shenyanfang
 *
 */

public class AllTrades {
	public static HashMap<String, String> tradeShortAndName = init();

	public static void main(String[] args) {
		AllTrades allTrades = new AllTrades();
	}

	private static HashMap<String, String> init() {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		String shorter = "881121,881131,881156,881138,881107,881164,881145,881120,881124,881153,881127,881136,881135,881166,881150,881148,881149,881112,881122,881147,881110,881111,881109,881140,881108,881151,881161,881160,881130,881163,881139,881128,881115,881116,881158,881105,881159,881103,881104,881126,881125,881123,881146,881134,881132,881142,881106,881162,881129,881117,881152,881114,881144,881133,881154,881119,881113,881155,881143,881102,881165,881157,881141,881118,881137,881101";
		String name = "半导体及元件,白色家电,保险及其他,包装印刷,采掘服务,传媒,电力,电气设备,电子制造,房地产开发,非汽车交运,服装家纺,纺织制造,国防军工,公交,港口航运,公路铁路运输,钢铁,光学光电子,环保工程,化工合成材料,化工新材料,化学制品,化学制药,基础化学,机场航运,酒店及餐饮,景点及旅游,计算机设备,计算机应用,家用轻工,交运设备服务,建筑材料,建筑装饰,零售,煤炭开采,贸易,农产品加工,农业服务,汽车零部件,汽车整车,其他电子,燃气水务,食品加工制造,视听器材,生物制品,石油矿业开采,通信服务,通信设备,通用设备,物流,新材料,医疗器械服务,饮料制造,园区开发,仪器仪表,有色冶炼加工,银行,医药商业,养殖业,综合,证券,中药,专用设备,造纸,种植业与林业";
		String[] shorterArray = shorter.split(",");
		String[] nameArray = name.split(",");
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
