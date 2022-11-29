package com.mylo.spring.service.contents;

import com.mylo.common.core.util.JsonUtils;
import com.mylo.domain.contents.model.Contents;
import com.mylo.domain.contents.model.ContentsMeta;
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

    public boolean insertContents(String url) {
        int result = contentsMapper.insertContents(url);
        if(!(result == 1)){
            contentsValidate.insertFail();
            return false;
        }
        return true;
    }

    public Map<String, Object> selectContents(Integer idx) {
        // param 유효성 확인
        contentsValidate.paramValidation(idx);
        return contentsMapper.selectContents(idx);
    }

    public Map<String, Object> selectContentsList(Integer idx) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Contents> sampleList = contentsMapper.selectContentsList(idx);
        resultMap.put("itemList", sampleList);

        return resultMap;
    }

    public boolean insertContentsMeta(ContentsMeta meta) {
        // param 유효성 확인
        contentsValidate.paramValidation(meta.getContentsIdx(), meta.getMetaJson());
        // contentsIdx가 DB에 있을 경우
        contentsValidate.existMeta(meta.getContentsIdx(), 1);
        contentsValidate.existFile(meta.getContentsIdx());

        // 입력 받은 metaJson을 json형식으로 바꾼 후 meta에 set
        meta.setMetaJson(JsonUtils.toJson(meta.getMetaJson()));
        int result = contentsMapper.insertContentsMeta(meta);

        if(!(result == 1)){ // DB insert 성공 여부
            contentsValidate.insertFail();
            return false;
        }
        return true;
    }

    public Map<String, Object> selectContentsMeta(Integer contentsIdx) {
        // param 유효성 확인
        contentsValidate.paramValidation(contentsIdx);
        Map<String, Object> result = contentsMapper.selectContentsMeta(contentsIdx);
        if(result != null) {
            result.put("metaJson", JsonUtils.toMap((String) result.get("metaJson")));
        }

        return result;
    }

    public Map<String, Object> selectContentsMetaList(Integer contentsIdx, String item, String value) {
        Map<String, Object> resultMap = new HashMap<>();

        // 쿼리에서 검색 항목 지정을 위해 item에 '$.'을 추가('${}' 대신 '#{}'을 사용하기 위함)
        item = "$." + item;
        List<Map<String, Object>> sampleList = contentsMapper.selectContentsMetaList(contentsIdx, item, value);
        for (Map<String, Object> o : sampleList) {
            o.put("metaJson", JsonUtils.toMap((String) o.get("metaJson")));
        }
        resultMap.put("itemList", sampleList);

        return resultMap;
    }

    public boolean updateContentsMeta(ContentsMeta meta) {
        // param 유효성 확인
        contentsValidate.paramValidation(meta.getContentsIdx(), meta.getMetaJson());
        // contentsIdx가 DB에 없을 경우
        contentsValidate.existMeta(meta.getContentsIdx(), 0);

        // 입력 받은 metaJson을 json형식으로 바꾼 후 meta에 set
        meta.setMetaJson(JsonUtils.toJson(meta.getMetaJson()));
        int result = contentsMapper.updateContentsMeta(meta);

        if(!(result == 1)){ // DB update 성공 여부
            contentsValidate.updateFail();
            return false;
        }
        return true;
    }

    public boolean deleteContentsMeta(Integer contentsIdx) {
        // param 유효성 확인
        contentsValidate.paramValidation(contentsIdx);
        // contentsIdx가 DB에 없을 경우
        contentsValidate.existMeta(contentsIdx, 0);

        int result = contentsMapper.deleteContentsMeta(contentsIdx);

        if(!(result == 1)){ // DB delete 성공 여부
            contentsValidate.deleteFail();
            return false;
        }
        return true;
    }
}
