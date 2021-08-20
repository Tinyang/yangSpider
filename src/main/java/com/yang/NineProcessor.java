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

public class NineProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private String basicUrl = "https://91porn.com/v.php?category=mf&viewtype=basic&page=";
    private int index = 1;
    @Override
    public void process(Page page) {
        HtmlNode htmlNode = (HtmlNode) page.getHtml().xpath("//*[@id=\"wrapper\"]/div[1]/div[3]/div/div");
        List<Selectable> htmlNodeList = htmlNode.nodes();
        List<DoubanObject> list = new ArrayList<>();
        for (Selectable selectable: htmlNodeList) {
            String name = selectable.xpath("//a/text()").toString();
            list.add(new DoubanObject(name));
        }
        page.putField("douban", list);
        ++index;
        page.addTargetRequest(basicUrl + index);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DouBanPageProcessor()).addUrl("https://91porn.com/v.php?category=mf&viewtype=basic&page=1")
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .addPipeline(new ConsolePipeline())
                .thread(5).run();

    }
}
