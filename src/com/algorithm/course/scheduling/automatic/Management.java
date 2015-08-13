package com.algorithm.course.scheduling.automatic;

import java.util.*;

/***
 * 算法主体
 * 
 * @author 李细鹏
 * @version 0.1
 */
public class Management {

	// 随机数产生器
	public static Random rand = new Random(System.currentTimeMillis());

	/***
	 * 对所有的班级集合进行排课
	 * 
	 * @param templates
	 * @param constraints
	 * @param course_each_day
	 * @param day_each_week
	 * @throws SchedulExceptions
	 */
	public static ArrayList<TimeTable> scheduleAll(
			ArrayList<Template> templates,
			Map<Integer, ConstraintTemplate> constraints, int course_each_day,
			int day_each_week, int moring_course_count) throws SchedulExceptions {
		long starts = System.currentTimeMillis();
		int class_count_each_week = course_each_day * day_each_week;
		ArrayList<TimeTable> timetables = new ArrayList<TimeTable>();
		// 初始化所有班级
		Collection<Class> classes = Util.init_all_classes(templates,
                class_count_each_week, moring_course_count);
		// 初步检测数据的正确性
		Util.check_class_data(classes, course_each_day, day_each_week);
		Util.check_constraint_data(constraints);
		// 初始化聚类分组
		Group[] groups = initGroup(classes, course_each_day, day_each_week, moring_course_count);
		// 排课
		for (int i = 0; i < groups.length; i++) {
			initGroupConstraint(groups, constraints, i);
			//management(groups[i], course_each_day, day_each_week, 0, true);
		}
		attemptInitialTimeTable(groups, course_each_day, day_each_week, moring_course_count);
		for (int i = 0;i < groups.length; i ++) {
			Util.loadGetClassTogether(groups, i);
			Util.loadSameTeacher(groups, i);
			management(groups[i], course_each_day, day_each_week, 0, true);
		}
		// 分离出最终的课表
		for (int i = 0; i < groups.length; i++) {
			for (int j = 0; j < groups[i].getBestRecord().length; j++) {
				TimeTable table = new TimeTable(class_count_each_week);
				table.setClass_id(groups[i].getBestRecord()[j].getClass_id());
				for (int t = 0; t < class_count_each_week; t++) {
					table.getTimetable()[t] = groups[i].getBestRecord()[j]
							.getTimetable()[t] % 10000;
				}
				timetables.add(table);
			}
		}
		System.out.println((System.currentTimeMillis() - starts) / 1000.0
				+ " seconds");
		return timetables;
	}

	public static TimeTable schedulePer(ArrayList<Template> templates,
			ArrayList<TimeTable> tables, int remanage_class_id,
			Map<Integer, ConstraintTemplate> constraints, int course_each_day,
			int day_each_week, int moring_course_count) throws SchedulExceptions {
		long starts = System.currentTimeMillis();
		int class_count_each_week = course_each_day * day_each_week;
		ArrayList<TimeTable> timetables = new ArrayList<TimeTable>();
		Group[] groups = new Group[2];
		Collection<Class> classes = Util.init_all_classes(templates,
				class_count_each_week, moring_course_count);
		Util.initGroupForPer(classes, groups, tables, remanage_class_id,
				class_count_each_week);
		// 排课
		initGroupConstraint(groups, constraints, 1);
		groups[1].initTimeTable(course_each_day, day_each_week, moring_course_count);
		management(groups[1], course_each_day, day_each_week, 1, false);
		// 分离出最终的课表
		for (int j = 0; j < groups[1].getBestRecord().length; j++) {
			TimeTable table = new TimeTable(class_count_each_week);
			table.setClass_id(groups[1].getBestRecord()[j].getClass_id());
			for (int t = 0; t < class_count_each_week; t++) {
				table.getTimetable()[t] = groups[1].getBestRecord()[j]
						.getTimetable()[t] % 10000;
			}
			timetables.add(table);
		}
		System.out.println((System.currentTimeMillis() - starts) / 1000.0
				+ " seconds");
		return timetables.get(0);
	}

	/***
	 * 班级聚类分组
	 * 
	 * @param classes
	 * @param class_count_each_week
	 * @return
	 */
	public static Group[] initGroup(Collection<Class> classes,
			int course_each_day, int day_each_week, int moring_course_count) {
		ArrayList<Group> groups = new ArrayList<Group>();
		// 某组内任何一个班级的课程不会与其他的班级课程课程数超过
		// Dfine.THRESHOLD * class_count_each_week
		for (Class cls : classes) {
			int max_position = 0;
			int max = 0;
			int i = 0;
			for (Group group : groups) {
				for (Class g_cls : group.getClasses()) {
					int count = Util.getConstraintCourseCount(g_cls, cls);
					if (count > max) {
						max = count;
						max_position = i;
					}
				}
				i++;
			}
			if (max <= Define.THRESHOLD * class_count_each_week) {
				Group new_group = new Group();
				new_group.PMULTATION = Define.PMULTATION;
				new_group.PXOVER = Define.PXOVER;
				new_group.getClasses().add(cls);
				groups.add(new_group);
			} else {
				groups.get(max_position).getClasses().add(cls);
			}
		}
		Group[] new_groups = groups.toArray(new Group[0]);
		// 初始话课程表
		Util.initialTimeTable(new_groups, course_each_day, day_each_week, moring_course_count);
		return new_groups;
	}

	/***
	 * 遗传算法完成排课
	 * 
	 * @param group
	 * @param course_each_day
	 * @param day_each_week
	 * @throws SchedulExceptions
	 */
	public static void management(Group group, int course_each_day,
			int day_each_week, int study_choose, boolean allow_count)
			throws SchedulExceptions {
		TimeTable[][] timetables = new TimeTable[Define.GROUP_SIZE][group
				.getClasses().size()];
		// 初始化课表。
		init(timetables, group, course_each_day, day_each_week);
		// 计算违约值。
		int week_course_count = day_each_week * course_each_day;
		double[] fitness = caculateConstraintValue(timetables, group,
				course_each_day, day_each_week);
		getBestRecord(timetables, group, fitness, week_course_count);

		long counter = 0;
		long count = 0;
		SelfStudy ss_PMULTATION = new SelfStudy(
				new Point(1, Define.PMULTATION), new Point(Define.MAX_UNM,
						Define.PMULTATION_MIN), study_choose);
		SelfStudy ss_PXOVER = new SelfStudy(new Point(0, Define.PXOVER),
				new Point(Define.MAX_UNM, Define.PXOVER_MIN), study_choose);
		while (counter < Define.MAX_UNM) {
			group.PMULTATION = ss_PMULTATION.run(counter);
			group.PXOVER = ss_PXOVER.run(counter);
			// 选择遗传母体
			TimeTable[][] best_m = selectBestMum(group, fitness, timetables,
					week_course_count);
			// 交叉
			crossover(group, best_m, timetables, week_course_count);
			// 变异
			variation(group, timetables, course_each_day, day_each_week);
			// 计算适应度
			fitness = caculateConstraintValue(timetables, group,
					course_each_day, day_each_week);
			if (group.getMinimux_fitness() <= 0) {
				break;
			}
			double tmp = group.getMinimux_fitness();
			getBestRecord(timetables, group, fitness, week_course_count);
			if (allow_count) {
				if (tmp <= group.getMinimux_fitness()) {
					count++;
				} else {
					count = 0;
				}
				if (count >= Define.MAX_UNM * Define.ALLOW_COUNT) {
					break;
				}
			}
			counter++;
		}
		Util.formatAuto(group);
		Util.printTimeTable(group, course_each_day);
		timetables = null;
		fitness = null;
		ss_PMULTATION = null;
		ss_PXOVER = null;
	}

	/***
	 * 初始化聚类组排课限制条件
	 * 
	 * @param groups
	 * @param constraints
	 * @param i
	 * @throws SchedulExceptions
	 */
	public static void initGroupConstraint(Group[] groups,
			Map<Integer, ConstraintTemplate> constraints, int i)
			throws SchedulExceptions {
		Iterator<Integer> iter = constraints.keySet().iterator();
		while (iter.hasNext()) {
			int key = iter.next();
			switch (key) {
			case 0: {
				JsonUtil.LoadConstraintMaxAllow(constraints.get(key), groups[i]);
				break;
			}
			case 1: {
				JsonUtil.LoadConstraintBestPosition(constraints.get(key),
						groups[i]);
				break;
			}
			case 2: {
				JsonUtil.LoadConstraintStep(constraints.get(key), groups[i]);
				break;
			}
			case 3: {
				JsonUtil.LoadConstraintGetClassTogether(constraints.get(key),
						groups[i]);
				break;
			}
			case 4: {
				JsonUtil.LoadConstraintStaticCourse(constraints.get(key),
						groups[i]);
				break;
			}
			case 5: {
				JsonUtil.LoadConstraintUndesireCourseTime(constraints.get(key),
						groups[i]);
				break;
			}
			case 6: {
				JsonUtil.LoadConstraintTogetherCourse(constraints.get(key),
						groups[i]);
				break;
			}
			default:
				break;
			}
		}
	}

	/***
	 * 初始化课程表
	 * 
	 * @param timetables
	 * @param group
	 * @param course_each_day
	 * @param day_each_week
	 * @throws SchedulExceptions
	 */
	public static void init(TimeTable[][] timetables, Group group,
			int course_each_day, int day_each_week) throws SchedulExceptions {
		for (int i = 0; i < timetables.length; i++) {
			timetables[i] = new TimeTable[group.getClasses().size()];
			initTimeTable(timetables[i], group, course_each_day, day_each_week);
		}
	}
	
	public static void attemptInitialTimeTable(Group[] groups,
			int course_each_day, int day_each_week) {
		Map<Integer, TeacherWithCourse> tm = new HashMap<Integer, TeacherWithCourse>();
		for (int i = 0;i < groups.length; i ++) {
			for(Class cls: groups[i].getClasses()) {
				if(groups[i].getConstraint().getStatic_position().containsKey(cls.getGrade())) {
					ArrayList<FixPosition> fixList = groups[i].getConstraint().getStatic_position().get(cls.getGrade());
					for(FixPosition f: fixList) {
						for(int j = 0; j < groups[i].getBestRecord().length; j ++) {
							if(groups[i].getBestRecord()[j].getClass_id().equals(cls.getClass_mode())) {
								groups[i].getBestRecord()[j].getTimetable()[f.positons] = f.course_id;
								break;
							}
						}
					}
				}
				for(Course cr: cls.getCourses()) {
					int priority = cr.getPriority();
					if(priority == Util.PRIORITY_STATIC_COURSE) {
						continue;
					}
					Teacher t = cr.getTeacher();
					if(t == null) {
						continue;
					}
					if(!tm.containsKey(t.getTeacher_id())) {
						tm.put(t.getTeacher_id(), new TeacherWithCourse());
					}
					// not finished.
					TeacherWithCourse tw = tm.get(t.getTeacher_id());
					if(!tw.map.containsKey(cr.getCourse_id())) {
						ArrayList<ClassWithCourse> cws = new ArrayList<ClassWithCourse>();
						tw.map.put(cr.getCourse_id(), cws);
					}
					ClassWithCourse cw = new ClassWithCourse();
					cw.classId = cls.getClass_mode();
					cw.len = cr.getLength();
					cw.priority = cr.getPriority();
					cw.len_each_time = cr.getTimes_each_week();
					tw.map.get(cr.getCourse_id()).add(cw);
				}
			}
		}
		Iterator<ArrayList<TeacherWithCourse>> keys = tm.keys().iterator();
		while(keys.hasNext()) {
			int tcId = vals.next();
            TeacherWithCourse tw = tm.get(tcId);
			
		}
	}

	/***
	 * 将固定课程，副课，连课全部按禁忌搜索策略固定分配，其它课程随机分配
	 * 
	 * @param tables
	 * @param group
	 * @param course_each_day
	 * @param day_each_week
	 * @throws SchedulExceptions
	 */
	public static void initTimeTable(TimeTable[] tables, Group group,
			int course_each_day, int day_each_week) throws SchedulExceptions {
		int course_counter = 0;
		int cls_counter = 0;
		for (Class cls : group.getClasses()) {
			tables[course_counter] = new TimeTable(course_each_day
					* day_each_week, group.getBestRecord()[cls_counter]);
			cls_counter ++;
			tables[course_counter].setClass_id(cls.getClass_mode());
			// 固定部分自习课
			int couter = (course_each_day * day_each_week - cls
					.getCourseCount()) / day_each_week;
			for (int q = 0; q < couter; q++) {
				for (int p = 0; p < day_each_week; p++) {
					tables[course_counter].getTimetable()[course_each_day * p
							+ course_each_day - 1 - q] = -2;
				}
			}

            Map<Integer, ArrayList<Integer>> none_pos = group.getConstraint().getUndesire_postion();
			// ////////////////////////////////////////////////////////////////////////////
			// 合班课
			Map<Integer, Integer> courseCount_0 = Util.initCourseCount(
					cls.getCourses(), 0);
			if (group.getConstraint().getFixed_position()
					.containsKey(cls.getClass_mode())) {
				ArrayList<FixPosition> fps = group.getConstraint()
						.getFixed_position().get(cls.getClass_mode());
				for (FixPosition fp : fps) {
					tables[course_counter].getTimetable()[fp.positons] = fp.course_id + 30000;
					Iterator<Integer> iter = courseCount_0.keySet().iterator();
					while (iter.hasNext()) {
						int pos = iter.next();
						if (cls.getCourses().get(pos).getCourse_id()
								.equals(fp.course_id)) {
							int left = courseCount_0.get(pos) - 1;
							if (left == 0) {
								courseCount_0.remove(pos);
							} else {
								courseCount_0.put(pos, left);
							}
						}
					}
				}
			}

			// /////////////////////////////////////////////////////////
            ArrayList<UnfitPosition> ups = new ArrayList<UnfitPosition>();
			// 副课
			Map<Integer, Integer> courseCount_z = Util.initCourseCount(
					cls.getCourses(), -2);

			if (group.getConstraint().getNone_position()
					.containsKey(cls.getClass_mode())) {
				ups = group.getConstraint().getNone_position()
						.get(cls.getClass_mode());
			}
			Iterator<Integer> iter_z_2 = courseCount_0.keySet().iterator();
			while (iter_z_2.hasNext()) {
				int pos = iter_z_2.next();
				if (!cls.getCourses().get(pos).getIs_main_course()) {
					courseCount_z.put(pos, courseCount_0.get(pos));
					courseCount_0.remove(pos);
				}
			}
			Iterator<Integer> iter_z = courseCount_z.keySet().iterator();
			while (iter_z.hasNext()) {
				int i = course_each_day;
				int pos = iter_z.next();
				int row = rand.nextInt(day_each_week);
                if (courseCount_z.get(pos) == 0) continue;
				for (int s = 0; s < courseCount_z.get(pos); s++) {
                    int pace = day_each_week / courseCount_z.get(pos);
                    if (courseCount_z.get(pos) == 1) pace = 1;
					if (cls.getCourses().get(pos).getLength() == 1) {
                        int counter = 0;
                        while (true) {//TODO
                            i --;
                            if (i < 0) {
                                i = course_each_day - 1;
                                if (row >= day_each_week) {
                                    pace --;
                                }
                            }
                            row += 1;
                            if (counter > course_each_day * day_each_week * course_each_day) {
                                String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                                if(Util.dataConvent!=null){
                                    errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                                    errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                                }
                                throw new SchedulExceptions(
                                        HandleType.RUN_ERROR_AT_INIT_TIME_TABLE,
                                        "Run time error when initial time table.",
                                        errorInfo);
                            }
                            if (row >= day_each_week) {
                                row = row % day_each_week;
                            }
							if (tables[course_counter].getTimetable()[i + row* course_each_day] == -1) {
								int a = 0;
								for (UnfitPosition up : ups) {
									if (up.course_id.equals(cls.getCourses()
											.get(pos).getCourse_id())
											&& up.positons == (i + row* course_each_day)) {
										break;
									}
									a++;
								}

								if (a >= ups.size()) {
									break;
								}
							}
                            counter ++;
						}
						tables[course_counter].getTimetable()[i + row
								* course_each_day] = cls.getCourses().get(pos)
								.getCourse_id() + 30000;
					} else if (cls.getCourses().get(pos).getLength() == 2) {
                        int counter = 0;
						while (true) {
							i --;
                            if (i < 0) {
                                i = course_each_day - 1;
                                if (row >= day_each_week) {
                                    pace --;
                                }
                            }
                            row += 1;
                            if (counter > course_each_day * day_each_week) {
                                String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                                if(Util.dataConvent!=null){
                                    errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                                    errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                                }
								throw new SchedulExceptions(
										HandleType.RUN_ERROR_AT_INIT_TIME_TABLE,
										"Run time error when initial time table.",
                                        errorInfo);
							}
                            if (row >= day_each_week) {
                                row = row % day_each_week;
                            }
							if (tables[course_counter].getTimetable()[i + row
									* course_each_day] == -1
									&& tables[course_counter].getTimetable()[i
											- 1 + row * course_each_day] == -1) {
								int a = 0;
								for (UnfitPosition up : ups) {
									if (up.course_id.equals(cls.getCourses()
											.get(pos).getCourse_id())
											&& up.positons == i + row
													* course_each_day) {
										break;
									}
									if (up.course_id.equals(cls.getCourses()
											.get(pos).getCourse_id())
											&& up.positons == i - 1 + row
													* course_each_day) {
										break;
									}
									a++;
								}
								if (a >= ups.size()) {
									break;
								}
							}
                            counter ++;
						}
						tables[course_counter].getTimetable()[i + row
								* course_each_day] = cls.getCourses().get(pos)
								.getCourse_id() + 30000;
						tables[course_counter].getTimetable()[i - 1 + row
								* course_each_day] = cls.getCourses().get(pos)
								.getCourse_id() + 30000;
					} else {
						if (i < 0 && row == 0) {
                            String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                            if(Util.dataConvent!=null){
                                errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                                errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                            }
							throw new SchedulExceptions(
									HandleType.RUN_ERROR_AT_INIT_TIME_TABLE,
									"Run time error when initial time table.",
                                    errorInfo);
						}
					}
				}
			}

			// /////////////////////////////////////////////////////////
			// 连课
			Map<Integer, Integer> courseCount_2 = Util.initCourseCount(
					cls.getCourses(), 2);
			Iterator<Integer> iter = courseCount_0.keySet().iterator();
			while (iter.hasNext()) {
				int pos = iter.next();
				if (cls.getCourses().get(pos).getLength() > 1) {
					courseCount_2.put(pos, courseCount_0.get(pos));
					courseCount_0.remove(pos);
				}
			}
			int cf = 0;
			while (courseCount_2.size() > 0) {
				int select = rand.nextInt(courseCount_2.size());
				int position = courseCount_2.keySet().toArray(new Integer[0])[select];
				int i = 0;
				cf = cf % day_each_week;
				int counter = 0;
				while (true) {
					if (tables[course_counter].getTimetable()[cf
							* course_each_day + i] == -1
							&& tables[course_counter].getTimetable()[cf
									* course_each_day + i + 1] == -1) {
 						int a = 0;
						for (UnfitPosition up : ups) {
							if (up.course_id.equals(cls.getCourses()
									.get(position).getCourse_id())
									&& up.positons == i + cf * course_each_day) {
								break;
							}
							if (up.course_id.equals(cls.getCourses()
									.get(position).getCourse_id())
									&& up.positons == i + 1 + cf
											* course_each_day) {
								break;
							}
							a++;
						}
						if (a >= ups.size()) {
                            int teacher_id = cls.getCourses().get(position).getTeacher().getTeacher_id();
                            if (none_pos.containsKey(teacher_id)) {
                                if (!none_pos.get(teacher_id).contains(cf * course_each_day + i) &&
                                        !none_pos.get(teacher_id).contains(cf * course_each_day + i + 1)) {
                                    break;
                                }
                            } else {
                                break;
                            }
						}
					}
					i++;
					cf++;
					if (i >= course_each_day - 2) {
						i = 0;
					}
					cf = (cf + 1) % day_each_week;
					counter++;
					if (counter >= course_each_day * day_each_week) {
                        String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                        if(Util.dataConvent!=null){
                            errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                            errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                        }
						throw new SchedulExceptions(
								HandleType.CHECK_ERROR_AT_COURSE_CONTINUE_COURSE,
								"Run time error when initial time table for together class.",
                                errorInfo);
					}
				}
				tables[course_counter].getTimetable()[cf * course_each_day + i] = cls
						.getCourses().get(position).getCourse_id() + 30000;
				tables[course_counter].getTimetable()[cf * course_each_day + i
						+ 1] = cls.getCourses().get(position).getCourse_id() + 30000;
				int left = courseCount_2.get(position) - 1;

				if (left == 0)
					courseCount_2.remove(position);
				else {
                    courseCount_2.put(position, left);
                }
			}
			// /////////////////////////////////////////////////////////
			// 其它
			Map<Integer, Integer> courseCount = Util.initCourseCount(
					cls.getCourses(), 1);
			Iterator<Integer> iter_2 = courseCount_0.keySet().iterator();
			while (iter_2.hasNext()) {
				int pos = iter_2.next();
				courseCount.put(pos, courseCount_0.get(pos));
				courseCount_0.remove(pos);
			}
			while (courseCount.size() > 0) {
				int select = rand.nextInt(courseCount.size());
				int position = courseCount.keySet().toArray(new Integer[0])[select];
				int i = 0;
				while (tables[course_counter].getTimetable()[i] != -1) {
					i++;
					if (i >= tables[course_counter].getTimetable().length) {
                        String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                        if(Util.dataConvent!=null){
                            errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                            errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                        }
						throw new SchedulExceptions(
								HandleType.RUN_ERROR_AT_INIT_TIME_TABLE,
								"Run time error when initial time table.",
                                errorInfo);
					}
				}
				if (i >= tables[course_counter].getTimetable().length) {
                    String errorInfo="";//"{\"class\": " + cls.getClass_mode()+ ", \"grade\":" + cls.getGrade()+ "}";
                    if(Util.dataConvent!=null){
                        errorInfo+="班级："+Util.dataConvent.getClassInfo(cls.getClass_mode()).values();
                        errorInfo+="年级："+Util.dataConvent.getGradeInfo(cls.getGrade()).values();
                    }
					throw new SchedulExceptions(
							HandleType.RUN_ERROR_AT_INIT_TIME_TABLE,
							"Run time error when initial time table.",
                            errorInfo);
				}
				int left = courseCount.get(position) - 1;
				if (left == 0)
					courseCount.remove(position);
				else
					courseCount.put(position, left);
				tables[course_counter].getTimetable()[i] = cls.getCourses()
						.get(position).getCourse_id();
			}
			course_counter++;
		}
	}

	/***
	 * 计算违约分
	 * 
	 * @param timetables
	 * @param group
	 * @param course_each_day
	 * @param day_each_week
	 * @return
	 * @throws SchedulExceptions 
	 */
	public static double[] caculateConstraintValue(TimeTable[][] timetables,
			Group group, int course_each_day, int day_each_week) throws SchedulExceptions {
		double[] values = new double[timetables.length];
		for (int i = 0; i < timetables.length; i++) {
			int[] constraint_value = new int[6];
			constraint_value[4] = Util.getConstraintSameTeacher(group,
					timetables[i]);
			constraint_value[5] = Util.getConstraintSamePlace(group,
					timetables[i], day_each_week, course_each_day);
			constraint_value[1] = Util.getConstraintCoursePace(group,
					timetables[i], day_each_week, course_each_day);
			constraint_value[0] = Util.getConstraintCourseUneven(group,
					timetables[i], day_each_week, course_each_day);
			constraint_value[2] = Util.getConstraintNoCourseTime(group,
					timetables[i]);
			constraint_value[3] = Util.getConstraintUnfitTime(group,
					timetables[i]);
			values[i] = constraint_value[4]
					* Define.CONSTRAINT_VALUE[4]
					+ constraint_value[5]
					* Define.CONSTRAINT_VALUE[5]
					+ (constraint_value[0] * Define.CONSTRAINT_VALUE[0]
							+ constraint_value[1] * Define.CONSTRAINT_VALUE[1]
							+ constraint_value[2] * Define.CONSTRAINT_VALUE[2]
							+ constraint_value[3] * Define.CONSTRAINT_VALUE[3] + constraint_value[4]
							* Define.CONSTRAINT_VALUE[4])
					/ (course_each_day * day_each_week);
			constraint_value = null;
		}
		return values;
	}

	/***
	 * 寻找最好记录，如果比历史记录好，则替换
	 * 
	 * @param timtables
	 * @param group
	 * @param constraint_caculate
	 * @param week_course_count
	 */
	public static void getBestRecord(TimeTable[][] timtables, Group group,
			double[] constraint_caculate, int week_course_count) {
		int position = -1;
		for (int i = 0; i < constraint_caculate.length; i++) {
			if (constraint_caculate[i] < group.getMinimux_fitness()) {
				position = i;
				group.setMinimux_fitness(constraint_caculate[i]);
			}
		}
		if (position != -1) {
			Util.cloneClassCourse(timtables[position], group.getBestRecord(),
					week_course_count);
		}
	}

	/***
	 * 利用轮盘赌算法，选择遗传母体
	 * 
	 * @param group
	 * @param fitness
	 * @param timetables
	 * @param week_course_count
	 * @return
	 */
	public static TimeTable[][] selectBestMum(Group group, double[] fitness,
			TimeTable[][] timetables, int week_course_count) {
		int size = (int) Math.floor(Define.GROUP_SIZE * group.PXOVER);
		TimeTable[][] best_ma = new TimeTable[size][group.getClasses().size()];
		double all = 0;
		ArrayList<Double> P = new ArrayList<Double>();
		for (int i = 0; i < fitness.length; i++) {
			all += fitness[i];
		}
		for (int i = 0; i < fitness.length; i++) {
			P.add(fitness[i] / all);
		}
		for (int i = 0; i < Define.GROUP_SIZE - size; i++) {
			double out = rand.nextDouble();
			double area = 0;
			for (int j = 0; j < P.size(); j++) {
				area += P.get(j);
				if (area >= out) {
					all -= fitness[j];
					fitness[j] = 0;
					for (int t = 0; t < P.size(); t++) {
						P.set(t, fitness[t] / all);
					}
					timetables[j] = null;
					break;
				}
			}
		}
		int t = 0;
		for (int i = 0; i < Define.GROUP_SIZE; i++) {
			if (timetables[i] == null) {
				continue;
			}
			if (t >= size)
				break;
			Util.cloneClassCourse(timetables[i], best_ma[t++],
					week_course_count);
		}
		return best_ma;
	}

	/***
	 * 实行基因交叉运算
	 * 
	 * @param group
	 * @param best_m
	 * @param timetables
	 * @param week_course_count
	 */
	public static void crossover(Group group, TimeTable[][] best_m,
			TimeTable[][] timetables, int week_course_count) {

		int length = group.getClasses().size();
		for (int i = 0; i < Define.GROUP_SIZE / 2; i++) {
			// 选择一对母体
			int x = rand.nextInt(best_m.length);
			int y = rand.nextInt(best_m.length);
			// 选择基因交换点
			int point = rand.nextInt(length);
			// 基因交换
			timetables[2 * i] = new TimeTable[length];
			timetables[2 * i + 1] = new TimeTable[length];
			for (int m = 0; m < length; m++) {
				timetables[2 * i][m] = new TimeTable(week_course_count);
				timetables[2 * i][m].setClass_id(best_m[x][m].getClass_id());
				timetables[2 * i + 1][m] = new TimeTable(week_course_count);
				timetables[2 * i + 1][m]
						.setClass_id(best_m[y][m].getClass_id());
				if (m < point) {
					for (int j = 0; j < week_course_count; j++) {
						timetables[2 * i][m].getTimetable()[j] = best_m[x][m]
								.getTimetable()[j];
						timetables[2 * i + 1][m].getTimetable()[j] = best_m[y][m]
								.getTimetable()[j];
					}
				} else {
					for (int j = 0; j < week_course_count; j++) {
						timetables[2 * i + 1][m].getTimetable()[j] = best_m[x][m]
								.getTimetable()[j];
						timetables[2 * i][m].getTimetable()[j] = best_m[y][m]
								.getTimetable()[j];
					}
				}
			}
		}
		best_m = null;
	}

	/***
	 * 实行基因变异运算
	 * 
	 * @param group
	 * @param timetables
	 * @param course_each_day
	 * @param day_each_week
	 */
	public static void variation(Group group, TimeTable[][] timetables,
			int course_each_day, int day_each_week) {
		int size = (int) Math.floor(Define.GROUP_SIZE * Define.PMULTATION);
		int length = course_each_day * day_each_week;
		for (int i = 0; i < size; i++) {
			// 选择变异个体
			int x = rand.nextInt(timetables.length);
			// 选择变异的基因
			for (int m = 0; m < group.getClasses().size(); m++) {
				int ex_x = rand.nextInt(length);
				int ex_y = rand.nextInt(length);
				// 变异时，变异点上的信息互换
				for (int j = 0; j < timetables[x].length; j++) {
					int tmp_1 = timetables[x][j].getTimetable()[ex_x];
					int tmp_2 = timetables[x][j].getTimetable()[ex_y];
					if (tmp_1 < 30000 && tmp_2 < 30000 && tmp_1 != -2
							&& tmp_2 != -2) {
						timetables[x][j].getTimetable()[ex_y] = tmp_1;
						timetables[x][j].getTimetable()[ex_x] = tmp_2;
					}
				}
			}
		}
	}
}
