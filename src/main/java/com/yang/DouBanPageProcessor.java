package com.yang;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class DouBanPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        HtmlNode htmlNode = (HtmlNode) page.getHtml().xpath("//*[@id=\"billboard\"]/div[2]/table/tbody//a");
        List<Selectable> htmlNodeList = htmlNode.nodes();
        List<DoubanObject> list = new ArrayList<>();
        for (Selectable selectable: htmlNodeList) {
            String name = selectable.xpath("//a/text()").toString();
            list.add(new DoubanObject(name));
        }
        page.putField("douban", list);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DouBanPageProcessor()).addUrl("https://movie.douban.com/")
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .addPipeline(new ConsolePipeline())
                .thread(5).run();

    }

}

class DoubanObject{
    private String name;

    public DoubanObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DoubanObject{" +
                "name='" + name + '\'' +
                '}';
    }
}