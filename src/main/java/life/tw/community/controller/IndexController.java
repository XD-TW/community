package life.tw.community.controller;

import life.tw.community.dto.PaginationDTO;
import life.tw.community.dto.TodayDTO;
import life.tw.community.provider.TodayProvider;
import life.tw.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    private final QuestionService questionService;

    @Autowired
    public IndexController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size,
                        HttpServletRequest request) {

        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
