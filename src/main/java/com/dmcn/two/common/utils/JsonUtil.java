package com.dmcn.two.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Jackson工具类
 */
public class JsonUtil {
    /**
     * 年月日 yyyy-MM-dd
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 24小时制 yyyy-MM-dd HH:mm:ss
     */
    private static final String DATETIMEPATTERN24H = "yyyy-MM-dd HH:mm:ss";
    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    private static final String ISO_DATE_FORMAT = "yyyyMMdd";
    /**
     * 所有值全部打印，时间以时间戳格式显示
     */
    private static final ObjectMapper mapper;
    /**
     * 空值过滤掉(""和NULL)，时间以时间戳格式显示
     */
    private static final ObjectMapper mapperNotEmpty;
    /**
     * 所有值全部打印，时间以24小时制格式显示
     */
    private static final ObjectMapper mapperWithTime;
    /**
     * 空值过滤掉(""和NULL)，时间以24小时制格式显示
     */
    private static final ObjectMapper mapperNotEmptyWithTime;
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(java.sql.Date.class, new JsonSqlDateDeserializer());
        module.addDeserializer(Timestamp.class, new JsonDateTimestampDeserializer());
        module.addDeserializer(Time.class, new JsonTimeDeserializer());
        module.addDeserializer(Date.class, new JsonDateDeserializer());

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//1
        mapper.registerModule(module);
        /*
        1.反序列化一个Json字符串为指定的POJO，而且字符串中有一个Field在POJO中不存在，应该抛出错误。jackson默认是中止，需关闭。
         */

        mapperNotEmpty = new ObjectMapper();
        mapperNotEmpty.enable(SerializationFeature.INDENT_OUTPUT);
        mapperNotEmpty.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperNotEmpty.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapperNotEmpty.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);

        mapperWithTime = new ObjectMapper();
        mapperWithTime.enable(SerializationFeature.INDENT_OUTPUT);
        mapperWithTime.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapperWithTime.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapperWithTime.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        mapperWithTime.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapperWithTime.setDateFormat(new SimpleDateFormat(DATETIMEPATTERN24H));


        mapperNotEmptyWithTime = new ObjectMapper();
        mapperNotEmptyWithTime.enable(SerializationFeature.INDENT_OUTPUT);
        mapperNotEmptyWithTime.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperNotEmptyWithTime.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapperNotEmptyWithTime.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        mapperNotEmptyWithTime.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapperNotEmptyWithTime.setDateFormat(new SimpleDateFormat(DATETIMEPATTERN24H));
    }

    private static Date string2Date(String dateString) {
        if (dateString == null || dateString.trim().length() == 0) return null;
        dateString = dateString.trim();
        if (dateString.matches("^-?[0-9]+$")) return new Date(Long.parseLong(dateString));
        if (dateString.length() <= 10) dateString = dateString.concat(" 00:00:00");
        try {
            DateFormat df = new SimpleDateFormat(DATETIMEPATTERN24H);
            // df.setLenient(false);
            // df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Object 转 Json
     * 过滤null和空
     */
    public static <T> String obj2Json(T t) {
        if (t == null) return null;
        try {
            return t instanceof String ? (String) t : mapper.writeValueAsString(t);
        } catch (IOException e) {
            logger.error("{} 对象转Json串失败，失败原因：{}", t, e.getMessage());
            return null;
        }
    }

    /**
     * Object 转 Json
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public static <T> String obj2JsonNotEmpty(T t) {
        if (t == null) return null;
        try {
            return t instanceof String ? (String) t : mapperNotEmpty.writeValueAsString(t);
        } catch (IOException e) {
            logger.error("{} 对象转Json串失败，失败原因：{}", t, e.getMessage());
            return null;
        }
    }

    public static <T> String obj2JsonWithTime(T t) {
        if (t == null) return null;
        try {
            return t instanceof String ? (String) t : mapperWithTime.writeValueAsString(t);
        } catch (IOException e) {
            logger.error("{} 对象转Json串失败，失败原因：{}", t, e.getMessage());
            return null;
        }
    }

    public static <T> String obj2JsonNotEmptyWithTime(T t) {
        if (t == null) return null;
        try {
            return t instanceof String ? (String) t : mapperNotEmptyWithTime.writeValueAsString(t);
        } catch (IOException e) {
            logger.error("{} 对象转Json串失败，失败原因：{}", t, e.getMessage());
            return null;
        }
    }

    /**
     * JSON对象转换为 map 或 list
     *
     * @param json      JSON对象
     * @param valueType map 或 list
     * @return 泛型对象
     */
    public static <T> T json2MapOrList(String json, TypeReference<T> valueType) {
        if (json == null || json.length() == 0 || "nil".equals(json))
            return null;
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getType(), e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getType(), e.getMessage());
        } catch (IOException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getType(), e.getMessage());
        }
        return null;
    }

    public static <T> T json2Bean(String json, Class<T> valueType) {
        if (json == null || json.length() == 0 || "nil".equals(json))
            return null;
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getName(), e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getName(), e.getMessage());
        } catch (IOException e) {
            logger.error("{} 字符串转换{}对象失败！失败原因：{}", json, valueType.getName(), e.getMessage());
        }
        return null;
    }

    /**
     * 统一获取返回的 json 格式
     * code 错误编码；msg 错误信息；obj 返回数据。
     */
    public static String getResponseJson(int code, String msg, Object obj) {
        return getResponseJson(code, msg, obj, null);
    }

    /**
     * 统一获取返回的json格式
     * code 错误编码；msg 错误信息；obj 返回数据，callback 回调函数。
     */
    public static String getResponseJson(int code, String msg, Object obj, String callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (obj == null) {
            obj = "null";
        }
        map.put("data", obj);
        String json = obj2Json(map);
        if (callback != null && callback.trim().length() != 0) {
            json = callback.concat("(").concat(json).concat(")");
        }
        return json;
    }

    public static String getResponseJsonWithTime(int code, String msg, Object obj) {
        return getResponseJsonWithTime(code, msg, obj, null);
    }

    /**
     * 统一获取返回的json格式
     * code 错误编码；msg 错误信息；obj 返回数据，callback 回调函数。
     */
    public static String getResponseJsonWithTime(int code, String msg, Object obj, String callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (obj == null) {
            obj = "null";
        }
        map.put("data", obj);
        String json = obj2JsonWithTime(map);
        if (callback != null && callback.trim().length() != 0) {
            json = callback.concat("(").concat(json).concat(")");
        }
        return json;
    }

    public static String getResponseJsonNotEmptyWithTime(int code, String msg, Object obj) {
        return getResponseJsonNotEmptyWithTime(code, msg, obj, null);
    }

    /**
     * 统一获取返回的json格式
     * code 错误编码；msg 错误信息；obj 返回数据，callback 回调函数。
     */
    public static String getResponseJsonNotEmptyWithTime(int code, String msg, Object obj, String callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (obj == null) {
            obj = "null";
        }
        map.put("data", obj);
        String json = obj2JsonNotEmptyWithTime(map);
        if (callback != null && callback.trim().length() != 0) {
            json = callback.concat("(").concat(json).concat(")");
        }
        return json;
    }

    /**
     * 统一获取返回的 json 格式
     * code 错误编码；msg 错误信息；obj 返回数据。
     */
    public static String getResponseJsonNotEmpty(int code, String msg, Object obj) {
        return getResponseJsonNotEmpty(code, msg, obj, null);
    }

    /**
     * 统一获取返回的json格式
     * code 错误编码；msg 错误信息；obj 返回数据，callback 回调函数。
     */
    public static String getResponseJsonNotEmpty(int code, String msg, Object obj, String callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", obj);
        String json = obj2JsonNotEmpty(map);
        if (callback != null && callback.trim().length() != 0) {
            json = callback.concat("(").concat(json).concat(")");
        }
        return json;
    }

    /**
     * 统一返回错误的信息
     */
    public static String getResponseJson(String err, String msg) {
        Map<String, Object> map = new HashMap();
        map.put("err", err);
        map.put("msg", msg);
        String json = obj2Json(map);
        return json;
    }

    private static class JsonDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                String dateString = jsonParser.getText();
                return string2Date(dateString);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
    }

    private static class JsonSqlDateDeserializer extends JsonDeserializer<java.sql.Date> {

        @Override
        public java.sql.Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                String dateString = jsonParser.getText();
                return new java.sql.Date(string2Date(dateString).getTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }

    }

    private static class JsonDateTimestampDeserializer extends JsonDeserializer<Timestamp> {

        @Override
        public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                String dateString = jsonParser.getText();
                return new Timestamp(string2Date(dateString).getTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }

    }

    private static class JsonTimeDeserializer extends JsonDeserializer<Time> {

        @Override
        public Time deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                String dateString = jsonParser.getText();
                return new Time(string2Date(dateString).getTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }

    }

}
