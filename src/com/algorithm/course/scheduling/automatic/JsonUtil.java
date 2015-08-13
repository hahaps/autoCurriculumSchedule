package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	/**************************************************************************************/
	/***
	 * 将json数据转变成map
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSONStringToMap(String jsonString) {
		JSONObject jsonObject;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			jsonObject = new JSONObject(jsonString);
			for (Iterator<String> iter = jsonObject.keys(); iter.hasNext();) {
				String key = (String) iter.next();
				map.put(key, jsonObject.get(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/***
	 * 通过班级id判断是否有该班级
	 * 
	 * @param group
	 * @param class_id
	 * @return
	 */
	public static boolean hasClassID(Group group, int class_id) {
		for (Class cls : group.getClasses()) {
			if (cls.getClass_mode() == class_id) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 通过年级id判断是否有该年级
	 * 
	 * @param group
	 * @param grade_id
	 * @return
	 */
	public static int [] hasGradeID(Group group, int grade_id, int course_id) {
		int [] pos = new int [2];
		for (Class cls : group.getClasses()) {
			pos[0] = -1;
			if (cls.getGrade() == grade_id) {
				pos[0] ++;
				pos[1] = -1;
				for (Course course: cls.getCourses()) {
					pos[1] ++;
					if (course.getCourse_id() == course_id) {
						return pos;
					}
				}
			}
		}
		return null;
	}

	/***
	 * 通过课程id判断是否有该课程
	 * 
	 * @param group
	 * @param course_id
	 * @return
	 */
	public static boolean hasCourseID(Group group, int course_id) {
		for (Class cls : group.getClasses()) {
			for (Course course : cls.getCourses()) {
				if (course.getCourse_id() == course_id) {
					return true;
				}
			}
		}
		return false;
	}

	/***
	 * 添加和班课，为方便，和班课实际id=20000 + 录入的课程id
	 * 
	 * @param group
	 * @param course_id
	 * @param course_count
	 * @param class_ids
	 * @return
	 */
	public static int addGetClassTogetherCourse(Group group, int course_id,
			int course_count, ArrayList<Integer> class_ids) {
		int counter = 0;
		for (Class cls : group.getClasses()) {
			if (class_ids.contains(cls.getClass_mode())) {
				for (Course course : cls.getCourses()) {
					if (course.getCourse_id() == course_id) {
						Course new_course = new Course(course.getCourse_id(),
								course.getCourse_name(), course.getTeacher());
						new_course
								.setIs_main_course(course.getIs_main_course());
						new_course.setTimes_each_week(course_count);
						new_course.setId(new_course.getCourse_id() + 20000);
						cls.getCourses().add(new_course);
						course.setTimes_each_week(course.getTimes_each_week()
								- course.getLength() * course_count);
						if (course.getTimes_each_week() == 0) {
							cls.getCourses().remove(course);
						}
						counter++;
						if (class_ids.size() == counter)
							return 1;
						break;
					}
				}
			}
		}
		return 0;
	}

	// 某课程最多能允许
	// {best_position: [0, 1]}
	/***
	 * 加载黄金时间段集合
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintBestPosition(
			ConstraintTemplate constraints, Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			JSONArray jsonArray = (JSONArray) map.get("best_position");
			try {
				for (int j = 0; j < jsonArray.length(); j++) {
					group.getConstraint().getBest_position()
							.add(jsonArray.getInt(j));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 合班课
	// {group: [1, 2], course_count: 2, course_id: 9}
	/***
	 * 加载合班课限制条件
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintGetClassTogether(
			ConstraintTemplate constraints, Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			int key = (int) map.get("course_id");
			if (hasCourseID(group, key)) {
				int course_count = (int) map.get("course_count");
				JSONArray jsonArray = (JSONArray) map.get("group");
				TogetherClass tc = new TogetherClass();
				tc.classGroup = new Integer[jsonArray.length()];
				tc.course_id = key;
				tc.courseCount = course_count;
				group.getConstraint().getTogether_course().add(tc);
				try {
					ArrayList<Integer> class_ids = new ArrayList<Integer>();
					for (int j = 0; j < jsonArray.length(); j++) {
						tc.classGroup[j] = jsonArray.getInt(j);
						class_ids.add(tc.classGroup[j]);
					}
					addGetClassTogetherCourse(group, key, course_count,
							class_ids);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 某课程最多能允许
	// {max_allow: 5, course_id: 9}
	/***
	 * 加载允许最多同时上某课的限制条件
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintMaxAllow(ConstraintTemplate constraints,
			Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			int key = (int) map.get("course_id");
			if (hasCourseID(group, key)) {
				int value = (int) map.get("max_allow");
				group.getConstraint().getMax_allow().put(key, value);
			}
		}
	}

	// 课程步长
	// {pace: 2, course_id: 0}
	/***
	 * 加载步长设置集合
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintStep(ConstraintTemplate constraints,
			Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			int key = (int) map.get("course_id");
			if (hasCourseID(group, key)) {
				int value = (int) map.get("pace");
				group.getConstraint().getPaces().put(key, value);
			}
		}
	}

	// 固定课程
	// {fixed_position:[5,12,35…],course_id: 111,grade_id:222, length: 1}
	/***
	 * 加载固定课程限制条件
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintStaticCourse(
			ConstraintTemplate constraints, Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
            int key = (Integer) map.get("course_id");
            int grade_id = (Integer) map.get("grade_id");
            int length = (Integer) map.get("length");
            int [] pos = hasGradeID(group, grade_id, key);
            if (pos != null) {
                String fix_position = map.get("fixed_position")+"";
                List<Integer> jsonArray = Arrays.asList(NumberUtils.parseInts(fix_position.substring(1,fix_position.length()-1)));
                if (!group.getConstraint().getStatic_position().containsKey(grade_id)) {
                    group.getConstraint().getStatic_position().put(grade_id, new ArrayList<FixPosition>());
                }
                Course course = group.getClasses().get(pos[0]).getCourses().get(pos[1]);
                Course new_course = new Course(course.getCourse_id(),course.getCourse_name(), course.getTeacher());
                new_course.setIs_main_course(course.getIs_main_course());
                new_course.setLength(length);
                new_course.setTimes_each_week(jsonArray.size());
                new_course.setId(course.getCourse_id() + 30000);
                // has change.
                new_course.setPriority(Util.PRIORITY_STATIC_COURSE);
                group.getClasses().get(pos[0]).getCourses().add(0, new_course);
                course.setTimes_each_week(course.getTimes_each_week()- jsonArray.size() * length);
                if (course.getTimes_each_week() == 0) {
                    group.getClasses().get(pos[0]).getCourses().remove(course);
                }
                for (int j = 0; j < jsonArray.size(); j++) {
                    FixPosition f = new FixPosition();
                    f.positons = jsonArray.get(j);
                    f.course_id = course.getId();
                    group.getConstraint().getStatic_position().get(grade_id)
                            .add(f);
                }

            }
		}
	}

	// 无课时间
	// {undesired_position: [1,12,33...],teacher_id: 111}
	/***
	 * 加载无课时间限制条件
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintUndesireCourseTime(
			ConstraintTemplate constraints, Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			int key = (int) map.get("teacher_id");
			if (hasCourseID(group, key)) {
				JSONArray jsonArray = (JSONArray) map.get("undesired_position");
				ArrayList<Integer> value = new ArrayList<Integer>();
				group.getConstraint().getUndesire_postion().put(key, value);
				try {
					for (int j = 0; j < jsonArray.length(); j++) {
						group.getConstraint().getUndesire_postion().get(key)
								.add(jsonArray.getInt(j));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 连课
	// {length: 2, course_id: 12, class_id: 1, count: 1}
	/***
	 * 加载连课限制条件
	 * 
	 * @param constraints
	 * @param group
	 */
	public static void LoadConstraintTogetherCourse(
			ConstraintTemplate constraints, Group group) {
		for (String json : constraints.getMeta_data()) {
			Map<String, Object> map = parseJSONStringToMap(json);
			int key = (int) map.get("class_id");
			if (hasClassID(group, key)) {
				int course_id = (int) map.get("course_id");
				int length = (int) map.get("length");
				int count = (int) map.get("count");
				addTogetherCourse(group, key, course_id, length, count);
			}
		}
	}

	/***
	 * 添加连课
	 * 
	 * @param group
	 * @param class_id
	 * @param course_id
	 * @param length
	 * @param count
	 * @return
	 */
	public static int addTogetherCourse(Group group, int class_id,
			int course_id, int length, int count) {
		for (Class cls : group.getClasses()) {
			if (class_id == cls.getClass_mode()) {
				for (Course course : cls.getCourses()) {
					if (course.getCourse_id() == course_id) {
						Course new_course = new Course(course.getCourse_id(),
								course.getCourse_name(), course.getTeacher());
						new_course
								.setIs_main_course(course.getIs_main_course());
						new_course.setLength(length);
						new_course.setTimes_each_week(count);
						cls.getCourses().add(0, new_course);
						course.setTimes_each_week(course.getTimes_each_week()
								- count * length);
						if (course.getTimes_each_week() == 0) {
							cls.getCourses().remove(course);
						}
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
