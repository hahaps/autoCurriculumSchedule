package com.algorithm.course.scheduling.automatic;

import java.util.*;

/***
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 * 
 */
public class Util {

	public static final int WEEK = 7;
	public static final int MAX_COURSES_EACH_DAY = 8;
    public static IDataConvent dataConvent=null;
    public static final int PRIORITY_STATIC_COURSE = 0;

    public static ArrayList<String> get_main_course() {
        ArrayList<String> main_course = new ArrayList<String>();
        main_course.add("数学");
        main_course.add("语文");
        main_course.add("英语");
        main_course.add("外语");
        main_course.add("政治");
        main_course.add("历史");
        main_course.add("地理");
        main_course.add("化学");
        main_course.add("生物");
        main_course.add("物理");
        return main_course;
    }

	/***
	 * 按班级为单位初始化数据
	*
	 * @param list
	 * @param class_count_each_week
	 * @return
	 */
	public static Collection<Class> init_all_classes(ArrayList<Template> list,
			int class_count_each_week, int moring_course_count) {
		Map<Integer, Class> map = new HashMap<Integer, Class>();
		for (Template tem : list) {
            if (tem.getWeek_count() < 1) continue;
			if (!map.containsKey(tem.getClass_id())) {
				map.put(tem.getClass_id(),
						new Class(tem.getClass_id(), tem.getGrade()));
			}
			boolean no_add = false;
			for (Course course : map.get(tem.getClass_id()).getCourses()) {
				if (course.getCourse_id().equals(tem.getCourse_id())) {
					no_add = true;
					break;
				}
			}
			if (no_add)
				continue;
			Teacher teacher = null;
			if (tem.getTeacher_name() != null) {
				teacher = new Teacher(tem.getTeacher_id(),
						tem.getTeacher_name());
			}
			Course course = new Course(tem.getCourse_id(),
					tem.getCourse_name(), teacher);
            ArrayList<String> main_course = Util.get_main_course();
            course.setIs_main_course(tem.getIs_main());
            if (main_course.contains(tem.getCourse_name()) && tem.getWeek_count() >= 2) {
                course.setIs_main_course(true);
            } else {
                course.setIs_main_course(tem.getIs_main());
            }
			course.setTimes_each_week(tem.getWeek_count());
			course.setPriority(course.getPriority() - tem.getWeek_count());
			map.get(tem.getClass_id()).setCourseCount(
					map.get(tem.getClass_id()).getCourseCount()
							+ course.getTimes_each_week());
			map.get(tem.getClass_id()).getCourses().add(course);
		}
		return map.values();
	}

	public static void initGroupForPer(Collection<Class> classes,
			Group[] groups, ArrayList<TimeTable> tables, int remanage_class_id,
			int course_each_day, int day_each_week) throws SchedulExceptions {
		groups[0] = new Group();
		groups[0].setBestRecord(classes.size() - 1);
		groups[1] = new Group();
		groups[1].PMULTATION = Define.PMULTATION;
		groups[1].PXOVER = Define.PXOVER;
		for (Class cls : classes) {
			for (TimeTable table : tables) {
				if (cls.getClass_mode().equals(table.getClass_id())) {
					if (cls.getClass_mode() == remanage_class_id) {
						if (groups[1].getClasses().size() > 0) {
							throw new SchedulExceptions(
									HandleType.RUN_ERROR_AT_TOO_MANY_CLASS);
						}
						groups[1].getClasses().add(cls);
						groups[1].initTimeTable(course_each_day, day_each_week);
					} else {
						groups[0].getClasses().add(cls);
						groups[0].getBestRecord()[groups[0].getClasses().size() - 1] = table;
					}
				}
			}
		}
		if (groups[1].getClasses().size() != 1
				|| groups[0].getClasses().size() != classes.size() - 1) {
			throw new SchedulExceptions(HandleType.RUN_ERROR_AT_NO_CLASS);
		}
	}

	/*****************************************************************/
	/***
	 * 初始化课程表
	 * 
	 * @param groups
	 * @param week_course_count
	 */
	public static void initialTimeTable(Group[] groups, int class_each_day, int day_each_week) {
		for (int i = 0; i < groups.length; i++) {
			groups[i].initTimeTable(class_each_day, day_each_week);
		}
	}

	/***
	 * 按条件获取课程集合
	 * 
	 * @param courses
	 * @param len
	 * @return
	 */
	public static Map<Integer, Integer> initCourseCount(
			ArrayList<Course> courses, int len) {
		Map<Integer, Integer> courseCount = new HashMap<Integer, Integer>();
		int position = 0;
		for (Course course : courses) {
			if (len == -1) {
				// 固定课程
				if (course.getId() >= 30000) {
					courseCount.put(position, course.getTimes_each_week());
				}
			} else if (len == 0) {
				// 非固定课程，合班课
				if (course.getId() < 30000 && course.getId() >= 20000) {
					courseCount.put(position, course.getTimes_each_week());
				}
			} else if (len == 1) {
				// 非固定课程，非合班课，非连课
				if (course.getLength() == 1 && course.getId() < 10000
						&& course.getIs_main_course()) {
					courseCount.put(position, course.getTimes_each_week());
				}
			} else if (len == -2) {
				// 副课
				if (!course.getIs_main_course() && course.getId() < 10000) {
					courseCount.put(position, course.getTimes_each_week());
				}
			} else {
				// 非固定课程，非和班课，连课
				if (course.getLength() != 1 && course.getId() < 10000
						&& course.getIs_main_course()) {
					courseCount.put(position, course.getTimes_each_week());
				}
			}
			position++;
		}
		return courseCount;
	}

	/***************************************************************************************/
	/***
	 * 通过id获取课程每次上的节数
	 * 
	 * @param cls
	 * @param id
	 * @return
	 */
	public static int getLengthById(Class cls, Integer id) {
		for (Course course : cls.getCourses()) {
			if (course.getId().equals(id)) {
				return course.getLength();
			}
		}
		return 0;
	}

	/***
	 * 获取冲突课程节数
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static int getConstraintCourseCount(Class A, Class B) {
		int count = 0;
		for (Course a : A.getCourses()) {
			if (a.getTeacher() == null)
				continue;
			for (Course b : B.getCourses()) {
				if (b.getTeacher() == null)
					continue;
				if (a.getTeacher().getTeacher_id()
						.equals(b.getTeacher().getTeacher_id())) {
					count += Math.min(a.getTimes_each_week() * a.getLength(),
							b.getTimes_each_week() * b.getLength());
				}
			}
		}
		return count;
	}

	public static void cloneClassCourse(TimeTable[] timetable,
			TimeTable[] timetable_clone, int week_course_count) {
		for (int i = 0; i < timetable.length; i++) {
			timetable_clone[i] = new TimeTable(week_course_count);
			timetable_clone[i].setClass_id(timetable[i].getClass_id());
			for (int j = 0; j < timetable[i].getTimetable().length; j++) {
				timetable_clone[i].getTimetable()[j] = timetable[i]
						.getTimetable()[j];
			}
		}
	}

	/***
	 * 通过教师id判断是否有该教师
	 * 
	 * @param group
	 * @param teacher_id
	 * @return
	 */
	public static boolean hasTeacherID(Group group, int teacher_id) {
		for (Class cls : group.getClasses()) {
			for (Course course : cls.getCourses()) {
				if (course.getTeacher() == null)
					continue;
				if (course.getTeacher().getTeacher_id() == teacher_id) {
					return true;
				}
			}
		}
		return false;
	}

	/***
	 * 通过课程id获取课程上课时间位置
	 * 
	 * @param course_id
	 * @param timetable
	 * @param class_id
	 * @return
	 */
	public static ArrayList<Integer> getPositionByID(Integer course_id,
			TimeTable[] timetable, int class_id) {
		ArrayList<Integer> position = new ArrayList<Integer>();
		for (int i = 0; i < timetable.length; i++) {
			if (timetable[i].getClass_id() == class_id) {
				for (int j = 0; j < timetable[i].getTimetable().length; j++) {
					if (course_id + 20000 == timetable[i].getTimetable()[j]) {
						position.add(j);
					}
				}
				break;
			}
		}
		return position;
	}

	// 组之间和班课
	/***
	 * 加载和班课组间限制条件
	 * 
	 * @param groups
	 * @param i
	 * @throws SchedulExceptions
	 */
	public static void loadGetClassTogether(Group[] groups, int i)
			throws SchedulExceptions {
		for (TogetherClass tc : groups[i].getConstraint().getTogether_course()) {
			for (int j = 0; j < i; j++) {
				for (int s = 0; s < groups[j].getBestRecord().length; s++) {
					for (int m = 0; m < groups[j].getBestRecord()[s]
							.getTimetable().length; m++) {
						if (tc.course_id == groups[j].getBestRecord()[s]
								.getTimetable()[m] % 10000) {
							int flag = 0;
							int class_id = -1;
							for (int n = 0; n < tc.classGroup.length; n++) {
								for (Class cls : groups[i].getClasses()) {
									if (cls.getClass_mode().equals(
											tc.classGroup[n])) {
										flag++;
										class_id = tc.classGroup[n];
										break;
									}
								}
								if (tc.classGroup[n].equals(groups[j]
										.getBestRecord()[s].getClass_id())) {
									flag++;
								}
							}
							if (flag == 2) {
								if (class_id == -1) {
									throw new SchedulExceptions(
											HandleType.RUN_ERROR_AT_NO_CLASS);
								}
								FixPosition fp = new FixPosition();
								if (!groups[i].getConstraint()
										.getFixed_position()
										.containsKey(class_id)) {
									groups[i]
											.getConstraint()
											.getFixed_position()
											.put(class_id,
													new ArrayList<FixPosition>());
								}
								fp.course_id = tc.course_id;
								fp.positons = m;
								groups[i].getConstraint().getFixed_position()
										.get(class_id).add(fp);
							} else if (flag > 2) {
								throw new SchedulExceptions(
										HandleType.RUN_ERROR);
							}
						}
					}
				}
			}
		}
	}

	// 组之间教师课程冲突
	/***
	 * 加载组间教师授课冲突限制条件
	 * 
	 * @param me
	 * @param other
	 */
	public static void loadNone_position(Group me, Group other) {
		for (Class cls : me.getClasses()) {
			for (Course course : cls.getCourses()) {
				if (course.getTeacher() == null)
					continue;
				for (int i = 0; i < other.getBestRecord().length; i++) {
					for (int j = 0; j < other.getBestRecord()[i].getTimetable().length; j++) {
						if (other.getBestRecord()[i].getTimetable()[j] == -1)
							continue;
						int teacher_id = getTeacherId(
								other,
								i,
								other.getBestRecord()[i].getTimetable()[j] % 10000);
						if (teacher_id == -1) {
							continue;
						}
						if (course.getTeacher().getTeacher_id() == teacher_id) {
							if (course.getId() < 10000 || course.getId() >= 30000) {
								if (!me.getConstraint().getNone_position()
										.containsKey(cls.getClass_mode())) {
									me.getConstraint()
											.getNone_position()
											.put(cls.getClass_mode(),
													new ArrayList<UnfitPosition>());
								}
								UnfitPosition up = new UnfitPosition();
								up.course_id = course.getCourse_id();
								up.positons = j;
								me.getConstraint().getNone_position()
										.get(cls.getClass_mode()).add(up);
							}
						}
					}
				}
			}
		}
	}

	public static void loadSameTeacher(Group[] groups, int i) {
		for (int j = 0; j < i; j++) {
			loadNone_position(groups[i], groups[j]);
		}
	}

	/*****************************************************************************/
	/***
	 * 打印课表
	 * 
	 * @param group
	 * @param course_each_day
	 */
	public static void printTimeTable(Group group, int course_each_day) {
		System.out.println("===================================="
				+ group.getMinimux_fitness());
		for (int a = 0; a < group.getBestRecord().length; a++) {
			int num = -1;
			for (Class cls : group.getClasses()) {
				num++;
				if (cls.getClass_mode().equals(
						group.getBestRecord()[a].getClass_id())) {
					break;
				}
			}
			System.out.print(group.getClasses().get(num).getGrade() + "年级 "
					+ ((group.getBestRecord()[a].getClass_id() - 1) % 14 + 1)
					+ "班");
			for (int m = 0; m < group.getBestRecord()[a].getTimetable().length; m++) {
				if (m % course_each_day == 0) {
					System.out.println();
				}
				for (Course course : group.getClasses().get(num).getCourses()) {
					if (group.getBestRecord()[a].getTimetable()[m] == -1
							|| group.getBestRecord()[a].getTimetable()[m] == -2) {
						System.out.print("自习          ");
						break;
					} else if (course.getCourse_id() == group.getBestRecord()[a]
							.getTimetable()[m] % 10000) {
						String str = course.getCourse_name()
								+ "/"
								+ (course.getTeacher() == null ? "Non" : course
										.getTeacher().getTeacher_name())
								+ "            ";
						System.out.print(str.substring(0, 10));
						break;
					}
				}
			}
			System.out.println("\n");
		}
	}

	public static void formatAuto(Group group) {
		for (int i = 0; i < group.getBestRecord().length; i++) {
			for (int j = 0; j < group.getBestRecord()[i].getTimetable().length; j++) {
				if (group.getBestRecord()[i].getTimetable()[j] == -2) {
					group.getBestRecord()[i].getTimetable()[j] = -1;
				}
			}
		}
	}

	/*********************** caculate constraint **********************************/
	/***
	 * 通过班级id获取班级在链表中的位置
	 * 
	 * @param group
	 * @param class_id
	 * @return
	 */
	public static int getClassPosition(Group group, int class_id) {
		int pos = -1;
		for (Class cls : group.getClasses()) {
			pos++;
			if (cls.getClass_mode() == class_id) {
				break;
			}
		}
		return pos;
	}

	/***
	 * 通过课程id获取授课教师id
	 * 
	 * @param group
	 * @param index
	 * @param course_id
	 * @return
	 */
	public static int getTeacherId(Group group, int index, int course_id) {
		for (Course course : group.getClasses().get(index).getCourses()) {
			if (course.getTeacher() == null)
				continue;
			if (course_id == course.getCourse_id()) {
				return course.getTeacher().getTeacher_id();
			}
		}
		return -1;
	}

	// 老师课程冲突
	/***
	 * 计算教师授课冲突违约次数
	 * 
	 * @param group
	 * @param timetables
	 * @return
	 */
	public static int getConstraintSameTeacher(Group group,
			TimeTable[] timetables) {
		int counter = 0;
		// 组内
		if (timetables.length == 0)
			return counter;
		else {
			for (int j = 0; j < timetables[0].getTimetable().length; j++) {
				for (int i = 0; i < timetables.length; i++) {
					if (timetables[i].getTimetable()[j] == -1) {
						continue;
					}
					for (int s = i + 1; s < timetables.length; s++) {
						if (timetables[s].getTimetable()[j] == -1) {
							continue;
						} else if (timetables[s].getTimetable()[j] != timetables[i]
								.getTimetable()[j]) {
							continue;
						} else {
							int pre_class_pos = getClassPosition(group,
									timetables[i].getClass_id());
							int pre_teacher_id = getTeacherId(group,
									pre_class_pos,
									timetables[i].getTimetable()[j]);
							int cur_teacher_id = getTeacherId(group,
									pre_class_pos,
									timetables[s].getTimetable()[j]);
							if (pre_teacher_id == cur_teacher_id) {
								counter++;
							}
						}
					}
				}
			}
		}

		// 组间
		Iterator<Integer> keys = group.getConstraint().getNone_position()
				.keySet().iterator();
		while (keys.hasNext()) {
			int class_id = keys.next();
			for (int i = 0; i < timetables.length; i++) {
				if (timetables[i].getClass_id() == class_id) {
					for (int j = 0; j < timetables[i].getTimetable().length; j++) {
						for (UnfitPosition up : group.getConstraint()
								.getNone_position().get(class_id)) {
							if (up.course_id.equals(timetables[i]
									.getTimetable()[j] % 10000)
									&& up.positons == j) {
								counter++;
							}
						}
					}
				}
			}
		}
		return counter;
	}

	// ////////////////////////////////end
	// getConstraintSameTeacher/////////////////////////////////////////

	// 场地资源冲突
	/***
	 * 计算场地冲突次数
	 * 
	 * @param group
	 * @param timetables
	 * @return
	 */
	public static int getConstraintSamePlace(Group group,
			TimeTable[] timetables, int day_each_week, int course_each_day) {
		int result = 0;
		int courses = day_each_week * course_each_day;
		Map<Integer, Integer> max_allow = group.getConstraint().getMax_allow();
		Iterator<Integer> key_iter = max_allow.keySet().iterator();
		while (key_iter.hasNext()) {
			int key = key_iter.next();
			int counter = 0;
			for (int a = 0; a < courses; a++) {
				for (int b = 0; b < timetables.length; b++) {
					if (timetables[b].getTimetable()[a] % 10000 == key) {
						counter++;
					}
					if (counter > max_allow.get(key)) {
						result++;
						break;
					}
				}
				counter = 0;
			}
		}
		return result;
	}

	public static int getTeacherIdForConstraint(Group group, int class_id,
			int course_id) {
		for (Class cls : group.getClasses()) {
			for (Course course : cls.getCourses()) {
				if (course_id % 10000 == course.getCourse_id() % 10000) {
                    if (course.getTeacher() == null) return 0;
					return course.getTeacher().getTeacher_id();
				}
			}
		}
		return -1;
	}

	// 课程步长冲突
	/***
	 * 计算步长冲突次数
	 * 
	 * @param group
	 * @param timetables
	 * @return
	 * @throws SchedulExceptions
	 */
	public static int getConstraintCoursePace(Group group,
			TimeTable[] timetables, int day_each_week, int course_each_day)
			throws SchedulExceptions {
		int result = 0;
		/*Map<Integer, ArrayList<Integer>> teachers = new HashMap<Integer, ArrayList<Integer>>();
		Map<Integer, Integer> paces = group.getConstraint().getPaces();
		// initial teacher get lesson time.
		for (int a = 0; a < timetables.length; a++) {
			for (int b = 0; b < timetables[a].getTimetable().length; b++) {
				if (!paces.containsKey(timetables[a].getTimetable()[b] % 10000)) {
					continue;
				}
				int teacher_id = Util.getTeacherIdForConstraint(group,
						timetables[a].getClass_id(),
						timetables[a].getTimetable()[b]);
				if (teacher_id == -1 && timetables[a].getTimetable()[b] > -1) {
                    String errorInfo="";//"{\"class\": " + timetables[a].getClass_id()+ ", \"course_id\":"+ timetables[a].getTimetable()[b] % 10000+ "}";
					if(dataConvent!=null){
                        errorInfo+="班级："+dataConvent.getClassInfo(timetables[a].getClass_id()).values();
                        errorInfo+="课程："+dataConvent.getCourseInfo(timetables[a].getTimetable()[b] % 10000).values();
                    }
                    throw new SchedulExceptions(
							HandleType.RUN_ERROR_AT_NO_TEACHER,
							"Run time error when can't find specific teacher.",
                            errorInfo);
				}
                if (teacher_id == 0) {
                     // Teacher with no name
                    continue;
                } if (!teachers.containsKey(teacher_id)) {
					teachers.put(teacher_id, new ArrayList<Integer>());
				} else {
					int before = teachers.get(teacher_id).get(
							teachers.get(teacher_id).size() - 1);
					if (b / course_each_day == before / course_each_day
							&& b - before > paces.get(timetables[a]
									.getTimetable()[b] % 10000)) {
						result++;
					}
				}
				teachers.get(teacher_id).add(b);
			}
		}
		teachers = null;
		// charge course pace.
		*/
		return result;
	}

	// 上课时间不合适冲突
	/***
	 * 计算上课时间不合适冲突次数
	 * 
	 * @param group
	 * @param timetables
	 * @return
	 */
	public static int getConstraintUnfitTime(Group group, TimeTable[] timetables) {
		int result = 0;
		return result;
	}

	// 无课时间冲突
	/***
	 * 计算无课时间冲突次数
	 * 
	 * @param group
	 * @param timetables
	 * @return
	 * @throws SchedulExceptions 
	 */
	public static int getConstraintNoCourseTime(Group group,
			TimeTable[] timetables) throws SchedulExceptions {
		int result = 0;
		Map<Integer, ArrayList<Integer>> no_course_times = group
				.getConstraint().getUndesire_postion();
		for (int a = 0; a < timetables.length; a++) {
			for (int b = 0; b < timetables[a].getTimetable().length; b++) {
				int teacher_id = Util.getTeacherIdForConstraint(group,
						timetables[a].getClass_id(),
						timetables[a].getTimetable()[b]);
				if (teacher_id == -1 && timetables[a].getTimetable()[b] > -1) {
                    String errorInfo="";//"{\"class\": " + timetables[a].getClass_id()+ ", \"course_id\":"+ timetables[a].getTimetable()[b] % 10000+ "}";
                    if(dataConvent!=null){
                        errorInfo+="班级："+dataConvent.getClassInfo(timetables[a].getClass_id()).values();
                        errorInfo+="课程："+dataConvent.getCourseInfo(timetables[a].getTimetable()[b] % 10000).values();
                    }
					throw new SchedulExceptions(
							HandleType.RUN_ERROR_AT_NO_TEACHER,
							"Run time error when can't find specific teacher.",
                            errorInfo);
				}
				if (no_course_times.containsKey(teacher_id)) {
					if (no_course_times.get(teacher_id).contains(b)) {
						result ++;
					}
				}
			}
		}
		return result;
	}

	// ///////////////////////////start
	// getConstraint Course Uneven/////////////////
	/***
	 * 通过课程id获取课程在链表中的位置
	 * 
	 * @param group
	 * @param course_id
	 * @param class_pos
	 * @return
	 */
	public static int getCoursePosition(Group group, int course_id,
			int class_pos) {
		int position = -1;
		for (Course course : group.getClasses().get(class_pos).getCourses()) {
			position++;
			if (course.getId() == course_id) {
				return position;
			}
		}
		return position;
	}

	// 课程分配不均冲突
	/***
	 * 计算课程分配不合适冲突次数
	 * 
	 * @param group
	 * @param timetables
	 * @param day_each_week
	 * @param course_each_day
	 * @return
	 */
	public static int getConstraintCourseUneven(Group group,
			TimeTable[] timetables, int day_each_week, int course_each_day) {
		int result = 0;
		int week_count = day_each_week * course_each_day;
		for (int i = 0; i < timetables.length; i++) {
			int pre_class = getClassPosition(group, timetables[i].getClass_id());
			int allow = (week_count - group.getClasses().get(pre_class)
					.getCourseCount())
					/ day_each_week;
			for (int j = 0; j < day_each_week; j++) {
				int counter = 0;
				for (int m = 0; m < course_each_day; m++) {
					if (timetables[i].getTimetable()[m + j * course_each_day] == -1
							|| timetables[i].getTimetable()[m + j
									* course_each_day] == -2) {
						counter++;
						if (counter == allow + 2) {
							result++;
						}
					} else {
						for (int n = m + 1; n < course_each_day; n++) {
							if (timetables[i].getTimetable()[n + j
									* course_each_day] % 30000 == timetables[i]
									.getTimetable()[m + j * course_each_day] % 30000) {
								if (n == m + 1
										&& timetables[i].getTimetable()[n + j
												* course_each_day] >= 30000
										&& timetables[i].getTimetable()[m + j
												* course_each_day] >= 30000) {
									continue;
								} else {
									result++;
								}
							}
						}
					}
				}
				if (counter < allow) {
					result++;
				}
			}
		}
		return result;
	}

	public static void check_constraint_data(
			Map<Integer, ConstraintTemplate> constraints) {
	}

	public static void check_class_data(Collection<Class> classes,
			int course_each_day, int day_each_week) throws SchedulExceptions {
		// check day for each week, no more than 7.
		if (day_each_week > Util.WEEK) {
			throw new SchedulExceptions(
					HandleType.CHECK_ERROR_AT_COURSE_A_WEEK_TOO_MANY,
					"check day for each week error, no more than 7.",
					"{\"day_each_week\": " + day_each_week + "}");
		}
		// check course each day, no more than 8.
		if (course_each_day > 9) {
			throw new SchedulExceptions(
					HandleType.CHECK_ERROR_AT_COURSE_A_DAY_TOO_MANY,
					"check course each day error, no more than 8.",
					"{\"course_each_day\": " + course_each_day + "}");
		}
		Map<Integer, Integer> teachers = new HashMap<Integer, Integer>();
		int courses = course_each_day * day_each_week;
		for (Class cls : classes) {
			// check courses for each class, never more than course_each_day *
			// day_each_week.
			if (cls.getCourseCount() > courses) {
                String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\": " + cls.getGrade()+ ", \"course_each_day\": " + course_each_day+ ", \"day_each_week\": " + day_each_week + "}";
                if(dataConvent!=null){
                    errorInfo+="班级："+dataConvent.getClassInfo(cls.getClass_mode()).values();
                    errorInfo+="年级："+dataConvent.getGradeInfo(cls.getGrade()).values();
                }
				throw new SchedulExceptions(
						HandleType.CHECK_ERROR_AT_COURSE_OUT_OF_RANGE,
						"check courses for each class error, never more than "+ courses,
                        errorInfo);
			}
			int courses_m = 0;
			for (Course course : cls.getCourses()) {
                if (course.getTeacher() == null) continue;
				if (teachers.containsKey(course.getTeacher().getTeacher_id())) {
					teachers.put(
							course.getTeacher().getTeacher_id(),
							teachers.get(course.getTeacher().getTeacher_id())
									+ course.getLength()
									* course.getTimes_each_week());
				} else {
					teachers.put(course.getTeacher().getTeacher_id(),
							course.getLength() * course.getTimes_each_week());
				}
				// check courses too many for one teacher.
				if (teachers.get(course.getTeacher().getTeacher_id()) > courses * 3 / 4) {
                    String errorInfo="";//teachers.get(course.getTeacher().getTeacher_id())+"{\"class\": " + cls.getClass_mode()+ ", \"grade\": " + cls.getGrade()+ ", \"course_each_day\": "+ course_each_day + ", \"day_each_week\": "+ day_each_week + ", \"teacher\": "+ course.getTeacher().getTeacher_name()+ "}";
                    if(dataConvent!=null){
                        errorInfo+="班级："+dataConvent.getClassInfo(cls.getClass_mode()).values();
                        errorInfo+="年级："+dataConvent.getGradeInfo(cls.getGrade()).values();
                        errorInfo+="教师："+dataConvent.getTeacherInfo(course.getTeacher().getTeacher_id()).values();
                    }
                    throw new SchedulExceptions(
							HandleType.CHECK_ERROR_AT_TEACHER_COURSE_TOO_MANY,
							"check courses too many for one teacher, more than 1/2 of whole courses each week.",
                            errorInfo);
				}
				if (course.getIs_main_course())
					continue;
				courses_m += course.getLength() * course.getTimes_each_week();
				// check courses too many for one Course.
				if (courses_m > courses * 3 / 5) {
                    String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\": " + cls.getGrade()+ ", \"course_each_day\": "+ course_each_day + ", \"day_each_week\": "+ day_each_week + "}";
                    if(dataConvent!=null){
                        errorInfo+="班级："+dataConvent.getClassInfo(cls.getClass_mode()).values();
                        errorInfo+="年级："+dataConvent.getGradeInfo(cls.getGrade()).values();
                    }
					throw new SchedulExceptions(
							HandleType.CHECK_ERROR_AT_VICE_COURSE_TOO_MANY,
							"check vice lesson too many, more than 3/5 of whole courses each week.",
                            errorInfo);
				}
			}
		}
	}
}
