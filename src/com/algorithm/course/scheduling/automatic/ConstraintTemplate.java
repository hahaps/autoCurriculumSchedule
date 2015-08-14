package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;

/***
 * 限制条件模板
 * 
 * @author Li Xipeng, lixipeng@hihuron.com
 * @version 0.1
 */
public class ConstraintTemplate {
	// 违约类型
	private int type;
	// 附加的限制条件JSON数据
	private ArrayList<String> meta_data;
	
	/***
	 * 违约模板构造函数
	 * 
	 * @param type
	 */
	public ConstraintTemplate(int type) {
		this.type = type;
		this.meta_data = new ArrayList<String>();
	}

	/***
	 * 获取违约类型
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/***
	 * 获取附加的限制条件JSON数据
	 * 
	 * @return
	 */
	public ArrayList<String> getMeta_data() {
		return meta_data;
	}
}
