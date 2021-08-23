package com.yang;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class NineProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private String basicUrl = "https://91porn.com/v.php?category=mf&viewtype=basic&page=";
    private int index = 1;
    @Override
    public void process(Page page) {
        HtmlNode htmlNode = (HtmlNode) page.getHtml().xpath("//*[@id=\"wrapper\"]/div[1]/div[3]/div/div/div");
        List<Selectable> htmlNodeList = htmlNode.nodes();
        for (Selectable selectable: htmlNodeList) {
            String href = selectable.links().toString();
            String value = selectable.xpath("//div/div/text()").toString();
            String key = selectable.xpath("//div/div/span/text()").all().toString();
            String[] strs = value.split("\\s+");
            String added = strs[1];
            String addedUnit = strs[2];
            String from = strs[3];
            String favorites = strs[5];
            NineInfo nineInfo = new NineInfo(added,addedUnit,from,favorites);
            page.putField(href, nineInfo);
        }
        index++;
        if (index <= 300) {
            page.addTargetRequest(basicUrl + index);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new NineProcessor()).addUrl("https://91porn.com/v.php?category=mf&viewtype=basic&page=1")
                .addPipeline(new FilePipeline("D:\\webmagic\\"))
                .addPipeline(new ConsolePipeline())
                .thread(5).run();

    }
}

class NineInfo {
    private String href;
    private String added;
    private String addedUnit;
    private String from;
    private String favorites;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public String getAddedUnit() {
        return addedUnit;
    }

    public void setAddedUnit(String addedUnit) {
        this.addedUnit = addedUnit;
    }

    @Override
    public String toString() {
        return
                "added='" + added + addedUnit + '\'' +
                ", from='" + from + '\'' +
                ", favorites='" + favorites + '\'' ;
    }

    public NineInfo(String added, String addedUnit, String from, String favorites) {
        this.added = added;
        this.addedUnit = addedUnit;
        this.from = from;
        this.favorites = favorites;
    }
}