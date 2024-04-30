import { describe, expect, test } from '@jest/globals';
import { Parser } from '.';

// link: https://gist.github.com/wazho/26ab288d2a46bd85bfa268216467ff5f

// `法規條文應分條書寫，冠以「第某條」字樣，並得分為項、款、目。項不冠數字，空二字書寫，款冠以一、二、三等數字，目冠以（一）、（二）、（三）等數字，並應加具標點符號。
// 前項所定之目再細分者，冠以１、２、３等數字，並稱為第某目之１、２、３。`
/*
 {
  "stratum": "paragraphs",
  "list": [
	{
	  "content": "法規條文應分條書寫，冠以「第某條」字樣，並得分為項、款、目。項不冠數字，空二字書寫，款冠以一、二、三等數字，目冠以（一）、（二）、（三）等數字，並應加具標點符號。"
	},
	{
	  "content": "前項所定之目再細分者，冠以１、２、３等數字，並稱為第某目之１、２、３。"
	}
  ]
}
*/
describe('testtest', () => {
	test('example1', () => {
		const rawData = `法規條文應分條書寫，冠以「第某條」字樣，並得分為項、款、目。項不冠數字，空二字書寫，款冠以一、二、三等數字，目冠以（一）、（二）、（三）等數字，並應加具標點符號。
前項所定之目再細分者，冠以１、２、３等數字，並稱為第某目之１、２、３。`
		const parser = new Parser(rawData);
		const res = parser.parse();
		expect(res).toEqual({
			stratum: 0,
			list: [
				{
					content: "法規條文應分條書寫，冠以「第某條」字樣，並得分為項、款、目。項不冠數字，空二字書寫，款冠以一、二、三等數字，目冠以（一）、（二）、（三）等數字，並應加具標點符號。",
				},
				{
					content: "前項所定之目再細分者，冠以１、２、３等數字，並稱為第某目之１、２、３。",
				}
			],
		});
	})
	test('example2', () => {
		const rawData = `法規有左列情形之一者，修正之：
一、基於政策或事實之需要，有增減內容之必要者。
二、因有關法規之修正或廢止而應配合修正者。
三、規定之主管機關或執行機關已裁併或變更者。
四、同一事項規定於二以上之法規，無分別存在之必要者。
法規修正之程序，準用本法有關法規制定之規定。`
		const parser = new Parser(rawData);
		const res = parser.parse();
		console.log(JSON.stringify(res));
		expect(res).toEqual({
			"stratum": 0,
			"list": [
				{
					"content": "法規有左列情形之一者，修正之："
				}
			],
			"child": [
				{
					"stratum": 1,
					"list": [
						{
							"content": "一、基於政策或事實之需要，有增減內容之必要者。"
						},
						{
							"content": "二、因有關法規之修正或廢止而應配合修正者。"
						},
						{
							"content": "三、規定之主管機關或執行機關已裁併或變更者。"
						},
						{
							"content": "四、同一事項規定於二以上之法規，無分別存在之必要者。"
						}
					]
				}
			]
		});
	})
	test('example3', () => {
		const rawData = `委員會議議程依左列次序編列，於會前分送各出席及列席人員：
一、報告事項：
（一）上次委員會議議決案件執行情形報告。
（二）本會重要措施。
（三）專案研究或重要工作進度報告。
（四）委員報告。
二、討論事項。
三、臨時動議。`
		const parser = new Parser(rawData);
		const res = parser.parse();
		console.log(JSON.stringify(res));
		expect(res).toEqual({
			"stratum": 0,
			"list": [
				{
					"content": "委員會議議程依左列次序編列，於會前分送各出席及列席人員："
				}
			],
			"child": [
				{
					"stratum": 1,
					"list": [
						{
							"content": "一、報告事項："
						},
						{
							"content": "二、討論事項。"
						},
						{
							"content": "三、臨時動議。"
						}
					]
				}
			]
		});
	})

})

