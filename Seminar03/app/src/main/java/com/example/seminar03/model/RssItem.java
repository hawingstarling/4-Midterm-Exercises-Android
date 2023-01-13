package com.example.seminar03.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class RssItem {
    @Element(name = "content", required = false)
    public RssMediaContent mediaContent;

    @Element
    public String title;


}
