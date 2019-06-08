package com.zsw.pojo.vote.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class VoteContentUtil {
    /**
     * 解析投票content的内容, 这个要和前端保持一致
     */
    
    private static Gson gson = new Gson();
    
    public static HashMap<String, Object> analysisContent(String content) {
        if (StringUtils.isBlank(content)) {
            return  null;
        }
        return null;
    }
    
    public static String generateContent(Map<String, Object> contentMap) {
        if (contentMap.size() == 0) {
            return null;
        }
        return null;
    }
}
