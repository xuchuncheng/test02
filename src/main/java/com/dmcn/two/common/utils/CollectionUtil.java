package com.dmcn.two.common.utils;

import java.util.*;

/**
 * 集合工具类
 *
 */
public class CollectionUtil {
	
	/**
	 * 用空List对象代替null
	 * @param list
	 * @return
	 */
	public static List toEmpty(List list){
		if(list == null){
			list = Collections.emptyList();
		}
		return list;
	}
	
	/**
	 * 用空Set对象代替null
	 * @param set
	 * @return
	 */
	public static Set toEmpty(Set set){
		if(set == null){
			set = Collections.emptySet();
		}
		return set;
	}
	
	/**
	 * 获取两个list不同的值
	 * @author yGe
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static Map<String,List<Long>> getDiff(List<Long> list1, List<Long> list2){
		List<Long> diff1 = new ArrayList<Long>();
		List<Long> diff2 = new ArrayList<Long>();
        Map<Long,Integer> map = new HashMap<Long,Integer>();
        for (Long string : list1) {
            map.put(string, 1);
        }
        for (Long l : list2){
            if(map.get(l)!=null){
                map.put(l, 2);
                continue;
            }
            diff2.add(l);
        }
        for(Map.Entry<Long, Integer> entry:map.entrySet()){
            if(entry.getValue()==1){
                diff1.add(entry.getKey());
            }
        }
        Map<String,List<Long>> res = new HashMap<String,List<Long>>();
        res.put("diff1", diff1);
        res.put("diff2", diff2);
		return res;
	}
	
}
