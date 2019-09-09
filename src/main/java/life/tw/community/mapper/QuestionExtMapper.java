package life.tw.community.mapper;

import life.tw.community.model.Question;


public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

}