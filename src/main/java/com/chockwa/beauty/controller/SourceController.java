package com.chockwa.beauty.controller;

import com.chockwa.beauty.dto.AddSourceDto;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.service.SourceDetailService;
import com.chockwa.beauty.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/29 09:58
 * @description:
 */
@RestController
@RequestMapping("source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SourceDetailService sourceDetailService;

    @GetMapping("source")
    public Result getSource(String sourceId){
        Source source = sourceService.getSource(sourceId);
        source.setZipDownloadLink(null);
        return Result.SUCCESS().setData("data", source);
    }

    @GetMapping("getSourceDetail")
    public Result getSourceDetail(String sourceId){
        return Result.SUCCESS().setData("data", sourceService.getSourceDetail(sourceId));
    }

    @PostMapping("save")
    public Result saveSource(@RequestBody AddSourceDto addSourceDto){
        sourceService.saveSource(addSourceDto);
        return Result.SUCCESS();
    }

    @GetMapping("delete")
    public Result deleteSource(String sourceId){
        sourceService.delete(sourceId);
        return Result.SUCCESS();
    }

    @GetMapping("deleteDetail")
    public Result deleteDetail(String sourceDetailId){
        sourceDetailService.delete(sourceDetailId);
        return Result.SUCCESS();
    }

    @GetMapping("sources")
    public Result getSourceListPage(PageParam pageParam){
        return Result.SUCCESS().setData("data", sourceService.getListPage(pageParam, null));
    }
}
