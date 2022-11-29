package com.mylo.spring.service.sample;


import com.mylo.domain.sample.model.Sample;
import com.mylo.domain.sample.repository.SampleMapper;
import com.mylo.spring.service.sample.validate.SampleValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mylo.common.core.util.PageUtils.getPage;

@Slf4j
@Service
public class SampleBO {
    @Autowired SampleMapper sampleMapper;
    @Autowired SampleValidate sampleValidateBO;
    //샘플 리스트 조회
    public Map<String, Object> getSampleList(Integer idx, String keyword, int pageNum, int pageSize) {
        //넘어온 param 에 대한 유효성 체크
        sampleValidateBO.paramValidation(idx, keyword, pageNum, pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        int totalCnt = sampleMapper.getTotalSampleCnt(idx, keyword);
        if (0 == totalCnt) {
            resultMap.put("itemList", new ArrayList());
            resultMap.put("itemCnt", 0);
        }
        List<Sample> sampleList = sampleMapper.selectSampleList(idx, keyword, getPage(pageNum, pageSize, totalCnt));
        resultMap.put("itemList", sampleList);
        resultMap.put("itemCnt", totalCnt);

        return resultMap;
    }
    public int insertSample(Sample sample) {
        int result = sampleMapper.insertSample(sample);
        sample.getIdx();
        return result;
    }

}
