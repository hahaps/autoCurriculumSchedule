package com.algorithm.course.scheduling.automatic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Helper {
	public static int course_start = 0;

	public static ArrayList<Template> read_data(String mode) {
		ArrayList<Template> templates = new ArrayList<Template>();
		File f = new File("../src/schedule_arrangement/doc/" + mode);
		if (f.exists()) {
			String s = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				while ((s = br.readLine()) != null) {
					String[] list = s.split(" ");
					int grade = Integer.parseInt(list[0]);
					int class_id = (Integer.parseInt(list[0]) - 1) * 14
							+ Integer.parseInt(list[1]);
					String course_name = list[2];
					int course_id = Integer.parseInt(list[3]);
					int week_count = Integer.parseInt(list[4]);
					boolean is_main = Integer.parseInt(list[5]) == 1;
					if (list.length > 6) {
						String teacher_name = list[6];
						int teacher_id = Integer.parseInt(list[7]);
						templates.add(new Template(grade, class_id,
								course_name, course_id, week_count, is_main,
								teacher_name, teacher_id));
					} else {
						templates.add(new Template(grade, class_id,
								course_name, course_id, week_count, is_main,
								null, 0));
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在!");
		}
		return templates;
	}

	public static Map<Integer, ConstraintTemplate> read_constraint(String path) {
		Map<Integer, ConstraintTemplate> templates = new HashMap<Integer, ConstraintTemplate>();
		File f = new File("src/schedule_arrangement/doc/" + path);
		if (f.exists()) {
			String s = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				while ((s = br.readLine()) != null) {
					int type = Integer.parseInt(s);
					templates.put(type, new ConstraintTemplate(type));
					while ((s = br.readLine()) != null) {
						if (s.equals("")) {
							break;
						}
						templates.get(type).getMeta_data().add(s);
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在!");
		}
		return templates;
	}
}
