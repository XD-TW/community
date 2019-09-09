package life.tw.community.service;

import life.tw.community.dto.CommentDTO;
import life.tw.community.enums.CommentTypeEnum;
import life.tw.community.exception.CustomizeErrorCode;
import life.tw.community.exception.CustomizeException;
import life.tw.community.mapper.*;
import life.tw.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentMapper commentMapper;

    private final QuestionMapper questionMapper;

    private final QuestionExtMapper questionExtMapper;

    private final UserMapper userMapper;

    private final CommentExtMapper commentExtMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper, QuestionMapper questionMapper, QuestionExtMapper questionExtMapper, UserMapper userMapper, CommentExtMapper commentExtMapper) {
        this.commentMapper = commentMapper;
        this.questionMapper = questionMapper;
        this.questionExtMapper = questionExtMapper;
        this.userMapper = userMapper;
        this.commentExtMapper = commentExtMapper;
    }


    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        } else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

        }

    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        //依据id,type类型 去数据库中查询回答
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create  desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        //是否有回答
        if (commentList.size() == 0) {
            //没有回答
            return new ArrayList<>();
        }
        //从comment里获取所有的commentator，并以Set集合形式返回
        //获取去重的评论人
        //将commentator放入list集合中
        List<Long> userIds = commentList.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        UserExample userExample = new UserExample();
        //依据list集合查询所有的符合标准的用户
        userExample.createCriteria().andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        //将userList转换成map，其中key为id value为user对象
        //获取评论人并转换成Map
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));

        //将comment转换成commentDTO,并将值复制到commentDTO,然后依据Commentator去userMap里寻找对应的user对象，将其set进DTO
        return commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
    }
}
