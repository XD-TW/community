package life.tw.community.controller;

import life.tw.community.dto.CommentDTO;
import life.tw.community.dto.QuestionDTO;
import life.tw.community.enums.CommentTypeEnum;
import life.tw.community.service.CommentService;
import life.tw.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    private final CommentService commentService;

    @Autowired
    public QuestionController(QuestionService questionService, CommentService commentService) {
        this.questionService = questionService;
        this.commentService = commentService;
    }


    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //增加阅读数
        questionService.incView(id);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        return "question";

    }
}
