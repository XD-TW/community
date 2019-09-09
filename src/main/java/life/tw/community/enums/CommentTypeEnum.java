package life.tw.community.enums;

public enum CommentTypeEnum {

    QUESTION(1),COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType().equals(type)){
                return true;
            }
        }
        return false;
    }


    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}

