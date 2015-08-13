package com.algorithm.course.scheduling.automatic;

/***
 * 课程
 * 
 * @version 0.1
 * @author 李细鹏
 * 
 */
public class Course {
	// 课程id
	private Integer id;
	// 初始输入的课程id
	private Integer course_id;
	// 课程名称
	private String course_name;
	// 授课教师
	private Teacher teacher;
	// 每周该课课节数
	private int times_each_week;
	// 是否为主课，默认为主课
	private boolean is_main_course = true;
	// 课程每次授课节数，默认为1节
	private int length = 1;
	// 课程优先级，默认是一个比较大的值，代表优先级低
	// 如果为 0，则需要初始化该课程
	private int priority = 1000;
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return this.priority;
	}

	/***
	 * 课程构造函数
	 * 
	 * @param course_id
	 * @param course_name
	 * @param teacher
	 */
	public Course(Integer course_id, String course_name, Teacher teacher) {
		this.course_id = course_id;
		this.id = course_id;
		this.course_name = course_name;
		this.teacher = teacher;
	}

	/***
	 * 获取课程每次授课节数
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/***
	 * 设置每次授课节数
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/***
	 * 设置该课程是否为主课
	 * 
	 * @param is_main_course
	 */
	public void setIs_main_course(boolean is_main_course) {
		this.is_main_course = is_main_course;
	}

	/***
	 * 获取课程id
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/***
	 * 设置课程id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/***
	 * 获取课程是否为主课
	 * 
	 * @return
	 */
	public boolean getIs_main_course() {
		return this.is_main_course;
	}

	/***
	 * 设置该课每周所上节数
	 * 
	 * @param times_each_week
	 */
	public void setTimes_each_week(int times_each_week) {
		this.times_each_week = times_each_week;
	}

	/***
	 * 获取课程实际输入id
	 * 
	 * @return course id
	 */
	public Integer getCourse_id() {
		return course_id;
	}

	/***
	 * 获取课程名称
	 * 
	 * @return course name
	 */
	public String getCourse_name() {
		return course_name;
	}

	/***
	 * 获取课程教师
	 * 
	 * @return teacher
	 */
	public Teacher getTeacher() {
		return teacher;
	}

	/***
	 * 获取课程每周节数
	 * 
	 * @return times_each_week
	 */
	public int getTimes_each_week() {
		return times_each_week;
	}

}
