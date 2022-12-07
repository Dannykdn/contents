package com.mylo.spring.service.contents;

import com.mylo.common.core.util.JsonUtils;
import com.mylo.domain.contents.model.ContentsMeta;
import com.mylo.domain.contents.repository.ContentsMapper;
import com.mylo.spring.service.contents.validate.ContentsValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ContentsMetaBO {

    @Autowired
    ContentsMapper contentsMapper;
    @Autowired
    ContentsValidate contentsValidate;

    /**
     * ContentsMeta 객체를 받아
     * 객체에 대한 유효성 검사 및 DB의 row 유무 검사 후
     * metaJson을 json형식으로 바꾼 후에 DB에 insert
     * @param meta
     * @return boolean
     */
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

    /**
     * contentsIdx를 받아 유효성 검사 후
     * select 결과가 null이면 에러를 반환
     * select 결과가 null이 아니면 Map으로 반환
     * @param contentsIdx
     * @return Map
     */
    public Map<String, Object> getContentsMeta(Integer contentsIdx) {
        // param 유효성 확인
        contentsValidate.paramValidation(contentsIdx);
        Map<String, Object> result = contentsMapper.selectContentsMeta(contentsIdx);
        if(result != null) {
            result.put("metaJson", JsonUtils.toMap((String) result.get("metaJson")));
        }

        return result;
    }

    /**
     * contentsIdx 혹은 item/value 혹은 아무것도 받지 않고
     * 입력값에 맞는 조건으로 검색 후 metaJson을 Map형식으로 변환해 주고
     * select 결과를 Map으로 반환
     * @param search
     * @return Map
     */
    public Map<String, Object> getContentsMetaList(Map<String, Object> search) {

        if(!(search.size() == 0)){
            for (String key : search.keySet()) {
                // 쿼리에서 검색 항목 지정을 위해 item에 '$.'을 추가('${}' 대신 '#{}'을 사용하기 위함)
                search.put("item", "$." + key);
                search.put("value", search.get(key));
                break;
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> contentsList = contentsMapper.selectContentsMetaList(search);
        for (Map<String, Object> contents : contentsList) {
            contents.put("metaJson", JsonUtils.toMap((String) contents.get("metaJson")));
        }
        resultMap.put("itemList", contentsList);

        return resultMap;
    }

    /**
     * ContentsMeta 객체를 받아
     * 객체에 대한 유효성 검사 및 DB의 row 유무 검사 후
     * metaJson을 json형식으로 바꾼 후에 DB에 update
     * @param meta
     * @return
     */
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

    /**
     * contentsIdx를 받아 유효성 검사 및 DB의 row 유무 검사 후
     * contentsIdx에 맞는 row를 delete
     * @param contentsIdx
     * @return boolean
     */
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
