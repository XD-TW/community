package life.tw.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;

    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
        this.page = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }

        }

        // 是否展示上一页
        showPrevious = page != 1;

        // 是否展示最后一页
        showNext = !page.equals(totalPage);

        //是否展示第一页
        showFirstPage = !pages.contains(1);

        //是否展示最后一页
        showEndPage = !pages.contains(totalPage);
    }
}
