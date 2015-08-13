package com.algorithm.course.scheduling.automatic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Helper_HR {

	public static int class_count_each_week = 40;
	public static int course_each_day = 8;
	public static int day_each_week = 5;
	public static int coutner = 0;

	@SuppressWarnings("unchecked")
	public static Map<String, String[]>[] genarateDAta(int grad, int class_num) {
		Map<String, String[]>[] aa = new HashMap[class_num];
		File f = new File("src/schedule_arrangement/doc/hr_grade_"
				+ grad);
		if (f.exists()) {
			String s = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				Map<String, Integer> bb = new HashMap<String, Integer>();
				for (int cc = 0; cc < aa.length; cc++) {
					aa[cc] = new HashMap<String, String[]>();
				}
				while ((s = br.readLine()) != null) {
					String[] list_1 = s.split(" ");
					for (int a = 0; a < list_1.length; a++) {
						if (list_1[a].equals("自习") || list_1[a].equals("生涯计划")
								|| list_1[a].equals("课外活动"))
							continue;
						if (!aa[a].containsKey(list_1[a])) {
							aa[a].put(list_1[a], new String[4]);
						}
						if (aa[a].get(list_1[a])[0] == null) {
							aa[a].get(list_1[a])[0] = 1 + "";
						} else {
							aa[a].get(list_1[a])[0] = (Integer.parseInt(aa[a]
									.get(list_1[a])[0]) + 1) + "";
						}
						if (list_1[a].equals("自习") && list_1[a].equals("自习")) {
							continue;
						} else if (list_1[a].equals("语文")
								|| list_1[a].equals("数学")
								|| list_1[a].equals("英语")
								|| list_1[a].equals("物理")
								|| list_1[a].equals("化学")
								|| list_1[a].equals("生物")
								|| list_1[a].equals("政治")
								|| list_1[a].equals("历史")
								|| list_1[a].equals("生物")) {
							aa[a].get(list_1[a])[1] = 1 + "";
						} else {
							aa[a].get(list_1[a])[1] = 0 + "";
						}
					}
					//System.out.println(s);
					s = br.readLine();
					//System.out.println(s);
					String[] list_2 = s.split(" ");
					for (int a = 0; a < list_2.length; a++) {
						//System.out.println("===" + list_1[a]);
						if (list_2[a].equals("自习") || list_2[a].equals("生涯计划")
								|| list_2[a].equals("课外活动"))
							continue;
						aa[a].get(list_1[a])[2] = list_2[a];
						if (!bb.containsKey(list_2[a])) {
							coutner++;
							bb.put(list_2[a], coutner);
						}
						aa[a].get(list_1[a])[3] = bb.get(list_2[a]) + "";
					}
					br.readLine();
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("文件不存在!");
		}
		return aa;
	}

	public static void writeData(int mode, Map<String, String[]>[] aa) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(
					"src/microchild/schedule_arrangement/doc/testData_hr", true);
			for (int a = 0; a < aa.length; a++) {
				Iterator<String> it = aa[a].keySet().iterator();
				int cou = 0;
				while (it.hasNext()) {
					String course = it.next();
					fw.write(mode + " " + (a + 1) + " " + course + " "
							+ (100 * mode + (cou++) - 100) + " "
							+ aa[a].get(course)[0] + " " + aa[a].get(course)[1]
							+ " " + aa[a].get(course)[2] + " "
							+ aa[a].get(course)[3] + "\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File ff = new File(
				"src/microchild/schedule_arrangement/doc/testData_hr");
		if(ff.exists()) ff.delete();
		writeData(1, genarateDAta(1, 14));
		writeData(2, genarateDAta(2, 14));
		writeData(3, genarateDAta(3, 15));
	}
}
