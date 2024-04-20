<h1>JAVA 콘솔로 구현한 도서관 도서 대출/ 대출예약/ 연장 / 반납과 자료실 예약시스템</h1>

<h2>0. DB설계</h2>
일단 도서관의 대출의 현황에 따른 순위를 표시한다거나, 분류별로 보기 편한 정보를 받고 싶었기 떄문에 대출count가 있고 분류번호가 있는 공공데이터를 채택했다.
도서 번호는 첫숫자는 분류번호를 받고 뒤의 숫자는 그 순서대로 번호를 받게 재설정하였으며,
도서관에 있는 보유 도서는 다른 데이터를 찾을 까 싶긴 했지만 너무 많은데이터로 인해서 기존 데이터를 임의로 넣기로 판단해 도서는 그렇게 진행되었다.

처음엔 간단히 도서 대출만 넣으려 했었지만 이대로는 쉽게 끝나지 않을까 싶다는 판단아래 도서 대출 예약 시스템도 추가로 넣기로하였다.
생각보다 마지막에 넣었던 도서 대출 예약 시스템에서 업데이트할 것들을 놓쳐서 재수정하는 시도들과 테이블에서 넣을 정보들이 부족해
java 코드를 작성하다가 다시 db로 돌아가 데이터를 옮기거나 추가하는 등 일이 있었지만 무사히 마칠 수 있었다. 

<h2>1. MAIN</h2>
![image](https://github.com/zamman96/LibraryService/assets/123943954/9574912c-b2be-4892-9eb6-793c36428d83)
메인 창은 상황에 따라 구분을 해놓았다
  1) 회원 로그인 X, 도서관 선택X
  2) 회원 로그인 O, 도서관 선택X
  3) 회원 로그인 X, 도서관 선택O
  4) 회원 로그인 O, 도서관 선택O
  5) 관리자가 로그인 했을 때,
선택 메뉴가 다르므로 5개의 view를 따로 만들어 세션에 저장된 정보에 따라 홈에 갈 경우 해당하는 곳에 향하도록 하였다.

<h2>2. 도서관 선택</h2>
![image](https://github.com/zamman96/LibraryService/assets/123943954/2aefa5d0-64ef-4a00-9b49-13eced010bb6)
도서관은 전국으로 정할까 싶었지만 초급 프로젝트치고 너무 주제가 넓어질 것 같아 대전한정으로 결정하였다.
대전의 20개의 도서관의 이름을 따서 사용하는 것으로 가정을 하였고 회원이 지역구를 통해 검색하기 용이하게
'1 : 중구		2 : 서구		3 : 동구		4 : 대덕구		5 : 유성구' 로 정해 번호를 받으면 원하는 도서관이 뜨도록 설정하였고, 그 외에도 이름 키워드로 검색할 수 있고,
전체의 리스트를 통해 번호를 입력해 들어가도록 설계하였다.

<h2>3. 도서 조회</h2>
![image](https://github.com/zamman96/LibraryService/assets/123943954/c9a125b6-4d2e-42f9-97b8-7b09bffe1297)
도서 대출은 도서번호를 통해 받기에 도서 번호를 보기 편한 조건으로 설계를 하였다.
처음 도서 조회를 들어오면 도서관을 선택하지 않았을 때는 전체의 도서관 대출 순위에 따른 도서 대출 순위를 보여주며, 선택했을 때는 해당하는 도서관의 순위가 뜨도록 하였다.
그리고 대출하기 용이하도록 도서가 대출이 가능한지 여부를 앞에 색으로 표시해두어 명확하게 파악할 수 있도록 했다.

<h3>  1) 모든 도서 조회</h3> 
![image](https://github.com/zamman96/LibraryService/assets/123943954/69000b7d-3add-4ef0-99a7-f3596b2c4b45)
모든 도서는 보기 좋게 페이징 처리를 하였으며 5페이지씩 끊어서 볼 수 있도록 처리하였다.
처음엔 >> 으로 5페이지나 10페이지씩 넘기게 설정할까 싶었지만 더욱 보기 쉽게 직접 페이지를 입력해 바꾸는 것으로 수정되어 <, >, 이나 숫자로 쉽게 페이지를 왔다갔다 할 수 있게 설계하였다.
(여기서 만약에 도서관을 선택했다면 해당 도서관의 도서관만 나온다)

<h3>  2) 분류별 도서 조회</h3>
공공데이터에서 가져온 바탕으로 ' 0 : 총류 	1 : 철학 	2 : 종교 	3 : 사회과학 	4 : 자연과학  5 : 기술과학 	6 : 예술 	7 : 언어 	8 : 문학 	9 : 역사 ' 를 출력하여
번호를 받아 해당 번호에 해당하는 분류별을 띄우도록하였다.
 
<h3>  3) 도서 검색</h3>
도서 검색은 초반에 작업한거라 간단하게 제목, 작가, 발행처만 검색이 가능하도록 하였다. 이후 기술할 내용에 있지만 관리자가 검색한 것처럼 더욱 자세히 검색할 수 있게 하면
더 좋지 않았을까 하는 아쉬움이 남는다.

(이후의 도서관리는 모두 로그인 상황이어야만 가능하다)
<h2>4. 도서 대출</h2>
도서 대출에서는 파악할 상황이 꽤 많다.
- 반납기한을 넘겼을 시 연체한 일만큼 2일의 텀을 두기 때문에 그 시간을 지났는 지 확인 
- 연체한 책을 반납하지않았는지에 대한 파악
- '대출'가능한 최대 권수가 5권이기 때문에 넘지 않았는 지
- 현재 도서가 대출 가능한 상태인 지(아직 대출중이거나 예약이 걸린 상태인경우)
- 도서관을 방문에 책을 빌린다는 가정하이므로 도서관이 일치하는지
- 도서번호가 유효한 번호인지 (없는 번호일 경우)
모든 제약사항을 파악한 뒤 책의 정보를 띄워 정말 대출할 책이 맞는지 확인하고 대출을 할 수 있게된다.
대출에 완료했을 때는 꼭 순위를 나타내기 위함의 대출count값도 +1 해줘야하며,
대출일을 남김과 동시에 반납일을 대출일의+14일만큼 더해준 상태로 저장하였다
 
<h2>5. 도서 대출 예약</h2>
도서 예약도 대출과 함께 제약사항들이 많았다
- 반납기한을 넘겼을 시 연체한 일만큼 2일의 텀을 두기 때문에 그 시간을 지났는 지 확인 
- 연체한 책을 반납하지않았는지에 대한 파악
- '대출예약'가능한 최대 권수가 3권이기 때문에 넘지 않았는 지
- 현재 도서가 대출 가능한 상태인 지(대출이 가능하다면 대출로 넘어감)
- 이미 자신이 예약한 도서인지
- 도서번호가 유효한 번호인지 (없는 번호일 경우)
파악한 걸로 예약은 끝나지 않았다. 예약은 다른 사람도 중복으로 예약이 가능하기 때문에 순서를 매기기 위해 따로 DB테이블을 만들었고
예약을 완료한 사람들이나 취소한 사람들의 순번의 최소값을파악하여 그 사람의 순번을 띄우도록 설정하였고,
그 사람이 대출 예약을 해놓고 그대로 방치해둘 수도 있기 때문에 예약일 기간을 7일이라는 제한을 걸기 위해서
업데이트문을 하나 더 작성해야만 했다. 이것은 반납쪽에서 기술하겠다.
  
<h2>6. 도서 연장</h2>
도서 연장의 제약조건은 이와 같다
- 반납기한을 넘겼을 시 연체한 일만큼 2일의 텀을 두기 때문에 그 시간을 지났는 지 확인 
- 연체한 책을 반납하지않았는지에 대한 파악
- 자신이 대출한 도서 중에 반납일이 7일 이하로 남았는 지
- 다른 사람이 대출예약을 걸지 않았는 지
- 이미 연장을 하지않았는 지
연장은 간단히 파악해주는 것으로 단순한 업데이트 작업이었다.

 <h2>7. 도서 반납</h2>
도서 반납의 조건은 이와 같다
- 그 책을 대출 중인 상태인지
- 도서관에 방문해 반납한다는 가정하임으로 현재 그 도서관의 소유 도서인지
- 연체일이 넘겼는 지
- 이 도서의 예약건이 있는지
여기서 연체일이 넘겼으면 넘긴 날짜를 계산해 회원 테이블에 그 넘긴 날짜 만큼 더해줘 저장하였다.
그리고 이 도서의 예약 건이 있다면 그 사람에게 예약 가능 날짜가 부여되도록 하였다.
그리고 따로 프로그램이 실행할 때 그 부여된 날짜에서 7일이 지났으면 다음 사람에게 순번이 넘어가도록 하였다.

<h2>8. 자료실 예약</h2>
자료실 예약은 도서관별로 지정하였으며 같은 시간대에 예약이 불가능하도록 설정했다
대신 대른 도서관에서는 같은 시간대 예약이 가능하게 될텐데 그것은 시간이 부족해 미처 하지 못했다.
자료실 예약은 아무래도 1시간만 예약을 받는 것이 아니기 떄문에 범위를 지정해 편히 예약이 가능하게 끔 설정하도록 하였고
좌석 지정을 받고 시작시간과 마지막시간을 써서 그 시간 만큼 전부 예약을 할 수 있도록 설정하였다.
이때 현재 시간을 받아 만약 현재 시간 이전의 시간을 예약하려고 하면 불가능 하도록 설정했다

<h2>9. 자료실 예약 취소</h2>
예약 취소는 3가지 별로 설정했다.
(도서관 별) 전체 취소, 좌석별 전체 취소, 시간별 취소
취소 역시 이미 사용한 내역을 삭제할 수는 없기에 현재 시간 이후의 내역만 삭제가 가능하도록 설정하였다.

<h2>10. 회원가입</h2>
회원가입은 단순하게 아이디, 비밀번호, 이름, 전화번호만 받도록 하였다.
아이디는 5~15자이상의 유효성검사와 중복검사를 설정하였고, 비밀번호도 5~15자로 유효성 검사를 하였다.
전화번호는 사람이 입력에 따라 다르게 정보를 받을 수 있기 때문에
DB에서는 '-'도 포함하여 저장하였지만 java에서는 숫자 11자리만 받아서 직접 '-'을 넣어 db에 저장하도록 하였다.

<h2>11. 로그인</h2>
로그인은 간단하게 아이디와 비밀번호가 같으면 로그인이 되도록 설정하였으며
그 사람의 관리자의 번호에 따라 회원 세션만 저장하던지 관리자 세션도 저장하게 하여 관리자와 회원을 구별하도록 하였다.
시간이 많다면 로그인 실패로 간단히 끝나기 보다는 아이디가 유효하지 않다라던가 비밀번호가 일치하지 않다라는 형식으로 좀더 자세하게 했으면 하는 생각이 있다.
추가적으로 로그인 시에 대출 예약된 책이 대출이 예약가능목록과 대출 예약 시간이 지나 취소된 목록을 출력하도록 하였다.

<h2>12. 아이디와 비밀번호 찾기</h2>
아이디 찾기는 여타 사이트 처럼 이름과 전화번호를 받고 인증을 받았다는 가정을 통해서 아이디를 출력하며 마지막 3자리는 ***로 표현하도록 하였다.
비밀번호는 이름, 전화번호에 추가로 아이디를 입력받아 그 결과가 일치한다면 비밀번호 재설정으로 하도록 하였고, 이 것은 이후에 회원수정에서 써먹는 코드를 재활용하였다.

<h2>13. 마이페이지</h2>
<h3>- 회원정보수정</h3>
비밀번호와 전화번호만 수정을 가능하게 하여 숫자를 입력받아 하나만 할지 전부다 바꿀 지 정할 수 있게 하였다
<h3>- 전체 대출 목록</h3>
기존에 빌렸던 대출목록이 보였으면 좋겠다고 생각하여 과거에 빌렸던 대출목록을 띄우도록 하였으며 같은 책을 여러 번 빌릴 수 있으니 구별차 반납일을 넣었고
현재 대출 중인 책은 현재 대출 중이라는 메세지를 통해 구별할 수 있도록 하였다
<h3>- 현재 대출/예약 내역</h3>
원활한 대출/예약을 파악하기 위해 넣었으며 대출 예약 취소도 이 내역을 확인 한 뒤에 이루어지도록 하였다.
이떄 예약을 취소할 때는 꼭 다음 예약순번에게 순서가 가도록 설정했다. 
<h3>- 탈퇴하기</h3>
탈퇴시엔 일단 도서가 이미 대출되어있는 지 확인하였다. 도서는 꼭 반납이 되야하기 떄문이다
대출하지 않은 상태이면 정보들이 (탈퇴) 상태로 변경되어 정보가 삭제되고
현재 도서 대출예약이나 자료실 예약같은 상황도 삭제하기 위해서
대출예약을 삭제하고 다음 예약자에게 넘겨주었으며, 자료실 예약 취소는 옛 좌석까지 취소하는 것이 아니라 오늘예약한 오늘 이후 시간의 예약을 취소하였다

<h2> 14. 관리자 도서 관리</h2>
<h3>- 도서 추가</h3>
간단히 정보를 받고 이후 변경은 되지 않으므로 확인한 내용이 맞는지 재확인만 시키도록 하였다.
<h3>- 도서 관리</h3>
도서를 다른 도서관으로 이관하거나 폐기 또는 사용가능한지 기록하는 것으로 꼭 상태 변경 시에는 도서가 현재 대출중은 아닌지 파악하도록 하였다.
<h3>- 도서 조회</h3>
도서관리는 도서상태에 따라 폐기/이관이나 도서추가 같은 것을 관리하기 편하게 일단 도서 조회를 꼼꼼하게 파악해야된다고 생각하여 단순히 전엔
제목과 작가 출판사 검색 3개만 추가했던 것과 다르게 더 꼼꼼하게 넣도록 하였다
![image](https://github.com/zamman96/LibraryService/assets/123943954/0d0dab8f-d353-4fae-a661-b4a34a111b79)
contains 메서드를 써서 더욱더 세세하게 검색할 수 있게하도록 하였다.

<h2>15. 관리자 회원 관리</h2>
<h3>- 회원 조회</h3>
![image](https://github.com/zamman96/LibraryService/assets/123943954/850c7a6c-d668-4340-a12b-2ed89571333e)  <br>
회원 조회도 여러 정보를 따로 조회하는 형식으로 표현 했지만,
지금 생각하기론 이름 검색이라던지, 관리자여부라던지는 추가로 썼으면 더 좋지 않았을까 하는 생각이 든다.
<h3>- 연체된 책 별 회원 정보</h3>
만약에 도서가 연체되어 회원에게 메세지를 보낼 떄라던지 사용할 수 도 있다 생각되어 따로 목록을 뽑았다,
도서별로 검색이 되기 때문에 한 회원이 연체된 책을 2권이 있다면 2번 나오도록 되었다.
<h3>- 관리자 변경</h3>
관리가 번호를 매길 때 일반관리자와 최상위 관리자를 따로 두었는데 최상위 관리자만이 실행할 수 있는 메뉴이다.
단순히 회원번호를 입력해 그 사람을 회원<->관리자로 변경해주는 메뉴이다


-------------------------------------------------------------------------------------------------------------------------------------------------------

<h1>소감</h1>

생각치도 못한 변수와 함께 어찌저찌 초급프로젝트가 마무리 되었지만
아직 아쉬움이 남긴한다. 위에서 언급한 문제라거나 관리자는 책 대출이 불가능하다는 점,
만약에 관리자를 임명했던 사람이 대출을 한상태라면 반납을 못하는 허점 등등

다음에는 더욱더 처음부터 꼼꼼히 무엇을 파악해야하는지 이후 다른 것을 업데이트를 해야되는 지부터 꼼꼼히 기록하여
처음부터 작성하는 것으로 끝내도록 하는 것을 목표로 하며,
꼭 귀찮다고 직접 실행해보지 않고 무작정 코드만 작성해보지 않고 하나하나 확인하며 더 꼼꼼히 설계하도록 노력해야겠다는 생각이 들었다.

7일정도 짧다면 짧고 길다면 긴 시간이었지만 그래도 어찌저찌 프로젝트는 잘 마무리 된 것같아 뿌듯하다.




   
