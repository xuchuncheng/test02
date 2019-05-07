package com.dmcn.two.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Map工具类
 */
public class MapUtil
{

    /**
     * 记录日志
     */
    private static Logger logger = LoggerFactory.getLogger(MapUtil.class);

    /**
     * 页面form序列化提交过来的数据转换为Map
     *
     * @param args
     * @return
     */
    public static Map<String, Object> formSerializeToMap(String args)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (args == null || args.length() == 0)
        {
            return map;
        }
        String[] kvs = args.split("&"); //先尝试用&分割
        if (kvs.length <= 1)
        {
            kvs = args.split("#"); //再尝试用#分割
        }
        for (String kv : kvs)
        {
            if (kv == null || kv.length() == 0)
            {
                continue;
            }
            String[] kvAry = kv.split("=");
            if (kvAry.length == 2)
            {
                Object temp = map.get(kvAry[0]);
                if (temp == null)
                {
                    map.put(kvAry[0], kvAry[1]);
                } else if (temp instanceof String[])
                {
                    map.put(kvAry[0], ArrayUtils.add((String[]) temp, kvAry[1]));
                } else if (temp instanceof String)
                {
                    map.put(kvAry[0], new String[]{(String) temp, kvAry[1]});
                }
            }
        }
        return map;
    }

    /**
     * 功能描述: 将Form提交过来的的值全部连接为字符串,不包含URL后面的参数<br>
     *
     * @param parameterMap 提交过来的全部数据
     * @param queryString  URL后面的参数
     * @return String
     */
    public static String getFormAllValues(Map<String, String[]> parameterMap, String queryString)
    {
        if (parameterMap == null || parameterMap.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String[] qs = getQueryStringKey(queryString);
        boolean hasKey;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet())
        {
            hasKey = false;
            for (String s : qs)
            {
                if (s.equalsIgnoreCase(entry.getKey()))
                {
                    hasKey = true;
                    break;
                }
            }
            if (hasKey)
            {
                continue;
            }
            if (entry.getValue() != null && entry.getValue().length > 0)
            {
                sb.append(entry.getValue()[0]);
            }
        }
        return sb.toString();
    }

    /**
     * 功能描述: 将queryString中的key取出<br>
     *
     * @param queryString
     * @return
     */
    private static String[] getQueryStringKey(String queryString)
    {
        if (queryString == null || queryString.trim().length() == 0)
        {
            return new String[0];
        }
        String[] qs = queryString.split("&");
        for (int i = 0; i < qs.length; i++)
        {
            qs[i] = qs[i].substring(0, qs[i].indexOf("="));
        }
        return qs;
    }


}
