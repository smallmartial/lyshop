package cn.smallmartial.search.web;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.search.pojo.Goods;
import cn.smallmartial.search.pojo.SearchRequest;
import cn.smallmartial.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author smallmartial
 * @Date 2019/4/19
 * @Email smallmarital@qq.com
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索功能实现
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){
        return ResponseEntity.ok(searchService.search(request));
    }
}
