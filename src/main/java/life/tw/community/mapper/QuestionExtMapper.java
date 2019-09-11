package life.tw.community.mapper;

import life.tw.community.model.Question;

import java.util.List;


public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

}