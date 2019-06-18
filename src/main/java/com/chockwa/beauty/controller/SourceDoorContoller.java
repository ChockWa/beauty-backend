package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.entity.Source;
import com.chockwa.beauty.service.SourceDetailService;
import com.chockwa.beauty.service.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/3 18:31
 * @description:
 */
@RestController
@RequestMapping("door")
@Slf4j
public class SourceDoorContoller extends BaseController{

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SourceDetailService sourceDetailService;


    @RateLimit(fallback = "fallBack")
    @GetMapping("index")
    public Result getIndexData() throws InterruptedException {
        CompletableFuture<List<Source>> newerFuture = CompletableFuture.supplyAsync(() -> sourceService.getIndexSource(1,8));
        CompletableFuture<List<Source>> olderFuture = CompletableFuture.supplyAsync(() -> sourceService.getIndexSource(20,8));
        CompletableFuture<List<Source>> hotestFuture = CompletableFuture.supplyAsync(() -> sourceService.getHotestSourceList(1,10));
        CompletableFuture.allOf(newerFuture, olderFuture, hotestFuture).join();
        try {
            List<Source> hotest = hotestFuture.get();
            return Result.SUCCESS().setData("newers", newerFuture.get())
                    .setData("olders", olderFuture.get())
                    .setData("hotests1", hotest.size() < 5 ? hotest.subList(0, hotest.size()) : hotest.subList(0, 5))
                    .setData("hotests2", hotest.size() < 5 ? hotest.subList(0, hotest.size()) : hotest.subList(5, hotest.size()));
        } catch (InterruptedException e) {
            log.error("thread was interrupted", e);
            throw e;
        } catch (ExecutionException e) {
            log.error("get source error!", e);
        }
        return Result.FAIL(9999, "get source error");
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("download")
    public Result getZipDownloadLink(String sourceId){
        return Result.SUCCESS().setData("downloadLink", sourceService.getZipDownloadLink(sourceId));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("search")
    public Result searchSources(String content, PageParam pageParam){
        return Result.SUCCESS().setData("data", sourceService.searchSources(content, pageParam));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("thumbs")
    public Result getSourceThumbs(String sourceId, PageParam pageParam){
        return Result.SUCCESS().setData("data", sourceDetailService.getSourceThumbs(sourceId, pageParam));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("details")
    public Result getSourceDetailListPage(String sourceId, PageParam pageParam){
        return Result.SUCCESS().setData("data", sourceDetailService.getListPage(sourceId, pageParam));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("max")
    public Result getMax(String sourceDetailId){
        return Result.SUCCESS().setData("url", sourceDetailService.getMaxImageById(sourceDetailId));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("sources")
    public Result getSourceListPage(PageParam pageParam){
        return Result.SUCCESS().setData("data", sourceService.getListPage(pageParam));
    }

}
