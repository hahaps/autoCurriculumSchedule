package com.algorithm.course.scheduling.automatic;

/***
 * 课程表
 * 
 * @version 0.1
 * @author 李细鹏
 *
 */
public class TimeTable {

	// 课程表
	private Integer[] timetable;
	// 班级id
	private Integer class_id;

	/***
	 * 课程表构造函数
	 * 
	 * @param course_count_each_week
	 */
	public TimeTable(int course_count_each_week) {
		this.timetable = new Integer[course_count_each_week];
		for (int i = 0; i < this.timetable.length; i++) {
			this.timetable[i] = -1;
		}
	}
	
	/***
	 * 课程表构造函数
	 * 
	 * @param course_count_each_week
	 */
	public TimeTable(int course_count_each_week, TimeTable t) {
		this.timetable = new Integer[course_count_each_week];
		for (int i = 0; i < this.timetable.length; i++) {
			if(t.getTimetable()[i] > 0) {
				this.timetable[i] = t.getTimetable()[i];
			} else {
				this.timetable[i] = -1;
			}
		}
	}
	
	/***
	 * 获取课程id
	 * 
	 * @return
	 */
	public Integer getClass_id() {
		return this.class_id;
	}
	
	/***
	 * 设置课程id
	 * 
	 * @param id
	 */
	public void setClass_id(Integer id) {
		this.class_id = id;
	}

	/***
	 * 获取课程表
	 * 
	 * @return time table
	 */
	public Integer[] getTimetable() {
		return timetable;
	}
	
	/***
	 * 设置课程表
	 * 
	 * @param tmp
	 */
	public void setTimeTable(Integer [] tmp) {
		this.timetable = new Integer [tmp.length];
		for (int i = 0;i < tmp.length;i ++) {
			this.timetable[i] = tmp[i];
		}
	}
}
