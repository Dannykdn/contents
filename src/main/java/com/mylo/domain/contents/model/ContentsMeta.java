package com.mylo.domain.contents.model;

import com.mylo.domain.type.CvMetaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsMeta {
    private Object metaJson;
    private CvMetaType typeIdx;
    private Integer contentsIdx;
}