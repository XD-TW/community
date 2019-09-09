package life.tw.community.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;




}

