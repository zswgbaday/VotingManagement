package com.zsw.pojo.vote;

public class VoteOption {
    
    private String optionDescribe;
    private Integer id;
    /**  每个选项的图片 */
    private String image; 
    /** 投这个选项的数量 */
    private Integer count;

/*******************************************************************************/
    public String getOptionDescribe() {
        return optionDescribe;
    }

    public void setOptionDescribe(String optionDescribe) {
        this.optionDescribe = optionDescribe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
