package cn.smallmartial.item.api;

import cn.smallmartial.item.pojo.SpecGroup;
import cn.smallmartial.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
public interface SpecificationApi {
    @GetMapping("spec/params")
    List<SpecParam> querySpecSpecParam(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic);

    @GetMapping("spec/group")
    List<SpecGroup> queryGroupByCid(@RequestParam("cid") Long cid);
}
