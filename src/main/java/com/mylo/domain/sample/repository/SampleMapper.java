package com.mylo.domain.sample.repository;

import com.mylo.common.core.util.PageUtils;
import com.mylo.domain.sample.model.Sample;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface SampleMapper {
    // 샘플리스트 조회
    List<Sample> selectSampleList(Integer idx, String keyword, PageUtils.Page page);
    //샘플 총 카운트 조회
    int getTotalSampleCnt(Integer idx, String keyword);

    int insertSample(Sample sample);


}
