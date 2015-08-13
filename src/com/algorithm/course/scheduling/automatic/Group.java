package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;

/***
 * 聚类分组
 * 
 * @author 李细鹏
 * @version 0.1
 */
public class Group {
	// 组内班级集合
	private ArrayList<Class> classes;
	// 组限制条件
	private Constraint constraint;
	// 组课程表
	private TimeTable[] bestRecord;
	// 最小适应值
	private Double minimux_fitness;
	
	public double PMULTATION;
	
	public double PXOVER;

	public Group() {
		this.classes = new ArrayList<Class>();
		this.minimux_fitness = Double.MAX_VALUE;
		this.constraint = new Constraint();
	}

	/***
	 * 初始化课程表
	 * 
	 * @param week_course_count
	 */
	public void initTimeTable(course_each_day, day_each_week) {
		this.bestRecord = new TimeTable[this.classes.size()];
		int counter = 0;
		for (Class cls : this.classes) {
			this.bestRecord[counter] = new TimeTable(course_each_day * day_each_week);
			this.bestRecord[counter ++].setClass_id(cls.getClass_mode());
		}
	}

	/***
	 * 获取组内所有班级
	 * 
	 * @return
	 */
	public ArrayList<Class> getClasses() {
		return classes;
	}

	/***
	 * 获取组限制条件集合
	 * 
	 * @return
	 */
	public Constraint getConstraint() {
		return constraint;
	}

	/***
	 * 设置限制条件集合
	 * 
	 * @param constraint
	 */
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	/***
	 * 获取组课程表
	 * 
	 * @return
	 */
	public TimeTable[] getBestRecord() {
		return bestRecord;
	}
	

    /***
     * 获取组课程表
     *
     * @return
     */
    public void setBestRecord(int len) {
        this.bestRecord = new TimeTable[len];
    }

	/***
	 * 获取最小适应值
	 * 
	 * @return
	 */
	public Double getMinimux_fitness() {
		return minimux_fitness;
	}

	/***
	 * 设置最小适应值
	 * 
	 * @param minimux_fitness
	 */
	public void setMinimux_fitness(Double minimux_fitness) {
		this.minimux_fitness = minimux_fitness;
	}

}
