package com.algorithm.course.scheduling.automatic;

/***
 * 录入班级课程模板，录入时以（某班）课程为最小单位
 * 
 * @version 0.1
 * @author 李细鹏
 */
public class Template {

	// 年级id
	private int grade;
	// 班级id
	private int class_id;
	// 教师id
	private int teacher_id;
	// 教师名称
	private String teacher_name;
	// 课程名称
	private String course_name;
	// 课程id
	private int course_id;
	// 课程每周上的数
	private int week_count;
	// 是否是主课
	private boolean is_main;

	/***
	 * 模板构造函数
	 * 
	 * @param grade
	 * @param class_id
	 * @param course_name
	 * @param course_id
	 * @param week_count
	 * @param is_main
	 * @param teacher_name
	 * @param teacher_id
	 */
	public Template(int grade, int class_id, String course_name, int course_id,
			int week_count, boolean is_main, String teacher_name, int teacher_id) {
		this.grade = grade;
		this.class_id = class_id;
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;
		this.week_count = week_count;
		this.is_main = is_main;
		this.course_id = course_id;
		this.course_name = course_name;
	}

	/***
	 * 获取班级id
	 * 
	 * @return
	 */
	public int getClass_id() {
		return class_id;
	}

	/***
	 * 获取教师id
	 * 
	 * @return
	 */
	public int getTeacher_id() {
		return teacher_id;
	}

	/***
	 * 获取教师名称
	 * 
	 * @return
	 */
	public String getTeacher_name() {
		return teacher_name;
	}

	/***
	 * 获取课程每周授课节数
	 * 
	 * @return
	 */
	public int getWeek_count() {
		return week_count;
	}

	/***
	 * 获取该课是否为主课
	 * 
	 * @return
	 */
	public boolean getIs_main() {
		return is_main;
	}

	/***
	 * 获取课程名称
	 * 
	 * @return
	 */
	public String getCourse_name() {
		return course_name;
	}

	/***
	 * 获取课程id
	 * 
	 * @return
	 */
	public int getCourse_id() {
		return course_id;
	}
	
	/***
	 * 获取年级id
	 * 
	 * @return
	 */
	public int getGrade () {
		return this.grade;
	}
}
