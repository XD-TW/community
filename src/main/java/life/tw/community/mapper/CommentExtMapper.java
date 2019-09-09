package life.tw.community.mapper;


import life.tw.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}