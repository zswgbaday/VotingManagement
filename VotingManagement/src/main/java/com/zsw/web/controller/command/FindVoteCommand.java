package com.zsw.web.controller.command;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FindVoteCommand {
    
    private Integer page;
    private Integer size;
    private String keyWord;
    private Date createTime;
    

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * offset 偏移量
     */
    public Integer getOffset() {
        if (page == 0) {
            page = 1;
        }
        return (page - 1) * size;
    }

    public String getKeyWord() {
        return keyWord;
    }

    /**
     * 分割好的keyWord
     * @return
     */
    public List<String> getKeyWords() {
        if (StringUtils.isBlank(keyWord)) {
            return new ArrayList<>();
        }
        List<String> list = Arrays.asList(keyWord.split(" "));
        return list;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
