package life.tw.community.dto;


import lombok.Data;

@Data
public class GithubUser {

    private Long id;
    private String name;
    private String bio;//用户简介
    private String avatarUrl;

}
