package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Main {

	public static void testPer(ArrayList<Template> templates,
			Map<Integer, ConstraintTemplate> constraints, int course_each_day,
			int day_each_week, int morning_course_count) throws SchedulExceptions {
		long start = System.currentTimeMillis();
		int class_count_each_week = course_each_day * day_each_week;
		// 初始化所有班级
		Collection<Class> classes = Util.init_all_classes(templates,
				class_count_each_week, morning_course_count);
		// 初始化聚类分组
		Group[] groups = Management.initGroup(classes, course_each_day, day_each_week, morning_course_count);
		// 排课
		for (int i = 0; i < groups.length; i++) {
			Management.initGroupConstraint(groups, constraints, i);
			Management.management(groups[i], course_each_day, day_each_week, 0,
					true);
		}
		System.out.println((System.currentTimeMillis() - start) / 1000.0 + " ms \n\n");
		ArrayList<TimeTable> tables = new ArrayList<TimeTable>();
		for (int ss = 0; ss < groups.length; ss ++) {
			tables.add(groups[ss].getBestRecord()[0]);
		}
		for (int s = 0; s < groups.length; s ++) {
			if (groups[s].getMinimux_fitness() > 0) {
				System.out.println("==========" + groups[s].getClasses().get(0).getGrade() + "年级 "
						+ ((groups[s].getBestRecord()[0].getClass_id() - 1) % 14 + 1)
						+ "班重排==========");
				Management.schedulePer(Helper.read_data("testData"),
						tables, groups[s].getClasses().get(0).getClass_mode(),
						Helper.read_constraint("constraint"), course_each_day, day_each_week, morning_course_count);
			}
		}
	}

	/**
	 * @param args
	 * @throws SchedulExceptions
	 */
	public static void main(String[] args) throws SchedulExceptions {
		//Management.scheduleAll(Helper.read_data("testData"),
		//		Helper.read_constraint("constraint"), 8, 5);
		Management.scheduleAll(Helper.read_data("testData"),
				Helper.read_constraint("constraint"), 8, 5, 4);
	}
}
