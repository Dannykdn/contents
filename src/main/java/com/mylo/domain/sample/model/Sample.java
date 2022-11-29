package com.mylo.domain.sample.model;

import com.mylo.common.core.util.MyloDateTime;
import com.mylo.domain.type.CvMetaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sample {
    private int idx; //샘플 idx
    private String sampleUrl; //샘플 url
    private CvMetaType cvMetaType;
    private MyloDateTime regDate;
    private MyloDateTime updateDate;
}
