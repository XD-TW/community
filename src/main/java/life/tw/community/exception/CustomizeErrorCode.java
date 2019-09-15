package life.tw.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你要找的问题不存在哦,换一个试试吧~"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题进行回复"),
    NO_LOGIN(2003, "未登录不能进行评论,请登录后再来尝试~"),
    SYS_ERROR(2004, "服务器冒烟啦，要不稍后试试~"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "你操作的评论不在了,换一个试试吧~"),

    ;


    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
