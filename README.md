# CONTENTS 타입에 따른 메타 처리

## ContentsBO &rarr; ContentsMapper / ContentsDAOMapper

- **getContents()** &rarr; selectContents()
  - 기능 : 파일 상세조회
  - 필수 : (Integer) idx
- **getContentsList()** &rarr; selectContentsList()
  - 기능 : 파일 리스트 조회
  - 선택 : (Integer) idx
- **uploadContents()**
  - 기능 : 파일 업로드
  - 필수 : (formData) file
- **insertContents()** &rarr; insertContents()
  - 기능 : 파일 저장 위치를 DB에 저장
  - 필수 : (String) url

## ContentsMetaBO &rarr; ContentsMapper / ContentsDAOMapper
- **getContentsMeta()** &rarr; selectContentsMeta() 
  - 기능 : 메타데이터 상세조회
  - 필수 : (Integer) idx
- **getContentsMetaList()** &rarr; selectContentsMetaList()
  - 기능 : 메타데이터 리스트 조회
  - 선택 : (Integer) idx / (String) item,value
- **insertContentsMeta()** &rarr; insertContentsMeta()
  - 기능 : 메타데이터 삽입
  - 필수 : (ContentsMeta) meta
- **updateContentsMeta()** &rarr; updateContentsMeta()
  - 기능 : 메타데이터 수정
  - 필수 : (ContentsMeta) meta
- **deleteContentsMeta()** &rarr; deleteContentsMeta()
  - 기능 : 메타데이터 삭제
  - 필수 : (Integer) idx

## ContentsValidate

### Param에 대한 유효성 검사에 따른 ERROR
- **paramValidation()**
  - code : 604
  - 필수 : (Integer) idx
    - null, 0 검사
  - 필수 : (Integer) idx, (Object) metaJson 
    - ObjectUtils.isEmpty() 사용하여 빈값 검사

### DB의 ROW 유무에 따른 ERROR
- update, delete 시 메타데이터의 유무를 판단하여 없으면 에러를 반환
  - **existMeta()**
    - code : 650
    - 필수 : (Integer) contentsIdx, (int) 0
- insert 시 메타데이터의 유무를 판단하여 있으면 에러를 반환
  - **existMeta()**
    - code : 651
    - 필수 : (Integer) contentsIdx, (int) 1
- insert 시 파일데이터의 유무를 판단하여 없으면 에러를 반환
  - **existFile()**
    - code : 650
    - 필수 : (Integer) contentsIdx

### SQL 실행 결과에 따른 ERROR
- **insertFail()**
  - insert 실패 시 
  - code : 201
- **updateFail()**
  - update 실패 시
  - code : 203
- **deleteFail()**
  - delete 실패 시
  - code : 204

### UPLOAD 결과에 따른 ERROR
- **uploadFail()**
  - upload 실패 시
  - code : 652