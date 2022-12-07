package com.mylo.spring.service.contents;

import com.mylo.domain.contents.model.Contents;
import com.mylo.domain.contents.repository.ContentsMapper;
import com.mylo.spring.service.contents.validate.ContentsValidate;
import com.mylo.util.aws.DefaultMyloFileUtils;
import com.mylo.util.aws.MyloFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mylo.domain.type.CvMetaType.CONTENTS;


@Slf4j
@Service
public class ContentsBO {

    @Autowired ContentsMapper contentsMapper;
    @Autowired ContentsValidate contentsValidate;

    private final MyloFileUtils myloFileUtils = DefaultMyloFileUtils.getInstance();

    /**
     * Contents 파일 업로드 메소드
     * 파일 업로드 실패 -> 에러
     * 파일 업로드 성공 -> DB insert 실패 -> 파일 삭제 및 에러
     * 파일 업로드 성공 -> DB insert 성공 -> true 반환
     * @param file
     * @return boolean
     */
    public boolean uploadContents(MultipartFile file) {
        String url = "";

        try{
            // 파일 업로드
            myloFileUtils.uploadMyloFile(CONTENTS, file, false);

            // 해당 파일이 등록된 url
            url = myloFileUtils.getMyloFileURL(CONTENTS, file);
            log.info("url : {}", url);

        } catch(Exception e){ // 업로드 실패 시 에러 발생
            contentsValidate.uploadFail();
        }

        if(!(this.insertContents(url))){ // url DB 저장 -> 실패 시 파일 삭제
            myloFileUtils.removeMyloFile(CONTENTS, file);
            contentsValidate.insertFail();
        }

        return true;
    }

    /**
     * 파일 업로드 성공 시 저장위치(url)를 받아서 DB에 insert하는 메소드
     * @param url
     * @return boolean
     */
    public boolean insertContents(String url) {
        int result = contentsMapper.insertContents(url);
        if(!(result == 1)){
            contentsValidate.insertFail();
            return false;
        }
        return true;
    }

    /**
     * idx로 url 조회
     * idx 유효성 검사 후 select 결과를 Map형식으로 반환
     * @param idx
     * @return Map
     */
    public Map<String, Object> getContents(Integer idx) {
        // param 유효성 확인
        contentsValidate.paramValidation(idx);
        return contentsMapper.selectContents(idx);
    }

    /**
     * 전체 리스트 조회 또는 idx로 조회
     * select 결과를 Map형식으로 반환
     * @param idx
     * @return Map
     */
    public Map<String, Object> getContentsList(Integer idx) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Contents> sampleList = contentsMapper.selectContentsList(idx);
        resultMap.put("itemList", sampleList);

        return resultMap;
    }
}
