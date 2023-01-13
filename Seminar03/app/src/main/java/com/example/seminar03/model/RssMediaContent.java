package com.example.seminar03.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "content", strict = false)
public class RssMediaContent {
    @Attribute(name = "url")
    public String url;
}
