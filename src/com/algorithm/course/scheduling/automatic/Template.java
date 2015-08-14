package com.algorithm.course.scheduling.automatic;

/***
 * Template specified
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 */
public class Template {

	// course id
	private int grade;
	// class id
	private int class_id;
	// teacher id
	private int teacher_id;
	// teacher name
	private String teacher_name;
	// course name
	private String course_name;
	// course id
	private int course_id;
	// course each week
	private int week_count;
	// is main course
	private boolean is_main;

	/***
	 * Instructor
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
	 * Get course id
	 * 
	 * @return
	 */
	public int getClass_id() {
		return class_id;
	}

	/***
	 * Get teacher id
	 * 
	 * @return
	 */
	public int getTeacher_id() {
		return teacher_id;
	}

	/***
	 * Get teacher name
	 * 
	 * @return
	 */
	public String getTeacher_name() {
		return teacher_name;
	}

	/***
	 * Get course count each week
	 * 
	 * @return
	 */
	public int getWeek_count() {
		return week_count;
	}

	/***
	 * is main course
	 * 
	 * @return
	 */
	public boolean getIs_main() {
		return is_main;
	}

	/***
	 * Get course name
	 * 
	 * @return
	 */
	public String getCourse_name() {
		return course_name;
	}

	/***
	 * Get course id
	 * 
	 * @return
	 */
	public int getCourse_id() {
		return course_id;
	}
	
	/***
	 * Get grade id
	 * 
	 * @return
	 */
	public int getGrade () {
		return this.grade;
	}
}
