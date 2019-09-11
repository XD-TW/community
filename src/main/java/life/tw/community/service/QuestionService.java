package life.tw.community.service;


import life.tw.community.dto.PaginationDTO;
import life.tw.community.dto.QuestionDTO;
import life.tw.community.exception.CustomizeErrorCode;
import life.tw.community.exception.CustomizeException;
import life.tw.community.mapper.QuestionExtMapper;
import life.tw.community.mapper.QuestionMapper;
import life.tw.community.mapper.UserMapper;
import life.tw.community.model.Question;
import life.tw.community.model.QuestionExample;
import life.tw.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final UserMapper userMapper;

    private final QuestionMapper questionMapper;

    private final QuestionExtMapper questionExtMapper;

    private Integer totalPage;

    @Autowired
    public QuestionService(UserMapper userMapper, QuestionMapper questionMapper, QuestionExtMapper questionExtMapper) {
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
        this.questionExtMapper = questionExtMapper;
    }


    /**
     * 将question表数据和user表数据存入questionDTOList并返回
     *
     * @param page
     * @param size
     * @return questionDTOList 里面存放所有question和user的数据
     */
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //查出所有问题数
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //通过总数，页数和每页显示数进行逻辑判断
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        //遍历从数据库中拿到的数据
        for (Question question : questionList) {
            //通过id 查找user数据
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question属性拷贝到questionDTO上
            BeanUtils.copyProperties(question, questionDTO);
            //将 通过id 查到的user放进questionDTO里
            questionDTO.setUser(user);
            //将questionDTO放入list集合里
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);
//        List<Question> questionList = questionMapper.listByUserId(userId, offset, size);


        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {

            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question =questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //问题不存在,则插入问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else {
            //问题存在,则进行修改
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updateId = questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            if(updateId != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(questionDTO.getTag() == null){
            new ArrayList<>();
        }

        return null;
    }
}
