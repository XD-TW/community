package life.tw.community.cache;

import life.tw.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {

    public static List<TagDTO> get(){
        List<TagDTO> tagDTOs = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("JavaScript","PHP","CSS","Java","C++","C","C#","Python","HTML","ASP.NET","shell"));
        tagDTOs.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("Spring","JFinal","Koa","MyBatis","Bootstrap","Struts","Maven","Dubbo","Hibernate"));
        tagDTOs.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux","Nginx","Tomcat","负载均衡","Unix","Docker","Apache","Hadoop"));
        tagDTOs.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库和缓存");
        db.setTags(Arrays.asList("MySQL","Redis","Oracle","MongoDB","SQL","SQLServer"));
        tagDTOs.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("Git","GitHub","Vim","SVN","Eclipse","IDEA"));
        tagDTOs.add(tool);
        return tagDTOs;
    }


    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTO = get();
        List<String> tagList = tagDTO.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        return Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
    }

}
