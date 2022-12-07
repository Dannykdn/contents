package com.mylo.domain.contents.repository;

import com.mylo.domain.contents.model.Contents;
import com.mylo.domain.contents.model.ContentsMeta;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ContentsMapper {

    int insertContents(String url);
    Map<String, Object> selectContents(Integer idx);

    List<Contents> selectContentsList(Integer idx);

    int insertContentsMeta(ContentsMeta meta);

    Map<String, Object> selectContentsMeta(Integer contentsIdx);

    List<Map<String, Object>> selectContentsMetaList(Map<String, Object> search);

    int updateContentsMeta(ContentsMeta meta);

    int deleteContentsMeta(Integer contentsIdx);

    int existMeta(Integer contentsIdx);

    int existFile(Integer contentsIdx);
}
