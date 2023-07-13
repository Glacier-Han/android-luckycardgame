# android-luckycardgame
Android 학습 프로젝트 #1

> ## 1. 게임 보드 만들기

> 230704 03:15 PM
> <img width="369" alt="스크린샷 2023-07-06 오전 10 48 40" src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/7420e6c1-6e8b-4b94-97fa-092801df6cfd">
> - ConstraintLayout, LinearLayout을 활용하여 화면을 구성하였다.
> - 화면을 크게 3분류로 나누었다. (노란 뷰 / 카드 뷰 / 하단 뷰)
> - 뒤에 기획을 잠시 보니, 하단 뷰가 카드뷰 갯수에 따라 크기가 자유롭게 바뀌어서, ConstraintLayout을 활용하여 3개의 뷰를 묶어 배치하였다.
> - 카드뷰의 constraintBottom_toTopOf 속성과 하단뷰의 constraintTop_toBottomOf로 설정하여 카드뷰의 크기에 따라 하단뷰도 크기가 변경되게 하였다.
---------
> ## 2. 럭키카드 클래스 구현하기
> 230704 05:03 PM
> ### 요구사항
> - #### 객체지향 프로그래밍 방식에 충실하게 카드 클래스(class)를 설계한다.
>   - Card 객체를 data class로 만들었으며, 동물의 종류와 이모티콘(유니코드)를 관리하기 위해 enum class를 사용하였다.
>   - enum 으로 관리하여 카드를 생성할때 동물과 유니코드를 함께 관리할 수 있어 코드가 직관적인 효과를 얻었다.
> - #### 카드 객체 인스턴스를 생성하는 곳에서 문자열로 콘솔에 출력한다
>   - <img width="645" alt="image" src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/d1770d3d-c973-4619-8029-493b29d376ee">
>   - pickCard함수를 제작하여, 카드를 섞고 뽑은 후 콘솔에 출력하게 제작하였다.
>   - String.format("%s%02d, ", card.animalType.unicode, card.num) 로 출력하였다.
>   - 위와같이 출력시 마지막에 쉼표와 공백이 한번 더 생기는 현상을 results.dropLast(2) 로 해결하였다.
> - #### 데이터를 처리하는 코드와 출력하는 코드를 분리한다
>   - MainActivity에서 initCard 함수로 카드 인스턴스들을 생성하고, pickCard로 카드를 뽑고 출력하게 분리하였다.
---------
> ## 3. 카드 나눠주기
> 230705 06:30 PM
> - 각각 참가자 카드덱은 CardView 안에 RecyclerView를 넣어 구현하였다.
> - 앞면/뒷면을 구분하기 위해 CardAdapter 생성자에 isFront: Boolean을 추가하여 앞면뒷면 상태에 따라 View들의 Visibility를 제어하였다.
> - 카드가 많을 경우 겹쳐보이게 하기 위하여 OverlapDecoration 클래스를 구현 후 recyclerview.addItemDecoration을 사용하여 구현하였다.
> - 겹치는 정도가 조절되게 하고싶어 overlapParam을 생성자로 뺐지만, 리사이클러뷰에 적용하는 addItemDecoration 함수가 실행시 마다 중첩되는 이슈가 발생하였다.
> - recyclerView의 itemDecoration 상태를 초기화할 수 있는 함수는 지원되지 않는 것 같다. 조금 더 찾아보고 다른 방법을 생각해보아야겠다.
> 230706 11:10 AM
> - 겹치는 정도의 조절은 임시로 getItemOffsets의 style.itemCount를 받아와서 리사이클러뷰 내 카드 갯수에 따라 카드 겹침 간격을 조절하게 구현하였다.
> - 마찬가지로 남은카드 리사이클러뷰도 getItemOffsets로 outRect값을 조절하여 카드간의 간격을 벌리게 구현하였다.

![res1](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/18c69579-5d60-4246-a5a4-7d0da63eafb5) | ![res2](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/717e2587-66dc-45ed-8e9f-4cbfed0b8abd) | ![res3](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/a00fa477-0db0-4670-91e1-4cb771c52383)
--- | --- | --- |
---------
> ## 4. 게임로직 구현
> 230707 11:20 AM
> - 기존에 게임 로직 관련 코드가 모두 MainActivity에 있어서 UnitTest가 곤란한 상황이였다.
> - 4번 과제 요구사항에 맞게, LuckyGame 클래스를 생성하여 게임로직 관련 코드는 모두 이 클래스 안에 넣고, 카드나 참가자들 또한 여기서 관리하게 수정하였다.
> - 테스트 케이스에도 네이밍 컨벤션이 많은데, 그 중 제일 직관적인 test + 테스트할 기능 설명 으로 결정.
> ### UnitTest - LuckyGameTest.kt
> - ```initCard_Count_36```
    >   TC1. 카드 생성후 갯수가 36개가 맞는지?
> - ```initCard_Has3Type_True```
    >   TC2. 카드 36개중에 같은 동물이 12개씩 있는지? (동물이 3종류임으로 한동물당 12개씩)
> - ```shareCard_3ParticipantsCount_4```
    >   TC3. 참가자 인원에 맞게 생성되는지?
> - ```shareCard_3ParticipantsHas12_False```
    >   TC4. 3명이 참가했을 시 participant가 12번 카드를 가지고 있으면 안됨. 가지고 있는지 검사
> - ```shareCard_5ParticipantsCount_6```
    >   TC5. 5명이 참가했을 시 participantList.size가 6이 맞는지?
> - ```sortCardAscend_IsVaild_True```
    >   TC6. A참가자를 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
> - ```sortRemainAscend_IsValid_True```
    >   TC7. 남은 카드 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
> - ```isSameCardInParticipants_isValid_True```
    >   TC8. 참가자 중 같은 숫자 카드 3개를 가진 경우가 있는지 판단
> - ```isSameCardInSpecificCase_IsValid_True```
    >   TC9. 1번 참가자와 3번 참가자 카드 중에 가장 낮은숫자 기준으로 3개가 같은지 판단
> <img width="396" alt="image" src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/c929cf29-5749-492c-9f30-895f78444b2f">

---------
> ## 5. 게임규칙 추가하기
> 230711 10:30 AM
> - 새로 추가된 요구사항을 만족하기 위해 LuckyGame 클래스의 추가 메소드를 작성하였다.
> - 카드 뒤집힘 상태를 구분하기 위해 Card 클래스에 ```var isBack: Boolean```  변수를 추가하였다.
> - 새로 추가된 메소드를 테스트하기 위해, UnitTest를 진행하며 동작을 검증하였다.
> ### UnitTest - LuckyGameTest.kt
> - ```isCardCanFlip_IsValid_True```
    >   TC1. 카드를 뒤집을 수 있는 상태인지 검증 (정렬된 상태로 가장 작은 숫자가 있는 왼쪽 또는 가장 큰 숫자가 있는 오른쪽 카드만 터치가 가능)
> - ```isCardCanFlip_IsValid_False```
    >   TC2. 카드를 뒤집을 수 없는 상태에서 뒤집기 동작을 했을 때 False가 나오는지 검증
> - ```checkWhen3CardSameStatus_IsValid_True```
    >   TC3. 1번 참가자가 같은 숫자를 3개 뽑은 상황을 구현한 뒤, 표시화면에서 사라지고 결과화면에 카드가 추가되는 상황을 검증
> - ```checkSumDiff7_IsValid_02```
    >   TC4. 각 참가자가 뽑은 숫자들중 합이나 차가 7이 되는 상황을 검증. 합이나 차가 7이되는 숫자를 갖고 있는 참가자 번호를 리턴함
> - ```checkSumDiff7_IsValid_1010```
    >   TC5. 각 참가자가 뽑은 숫자들중 합이나 차가 7이 되지 않는 상황에서, False를 뜻하는 (10,10)이 나오는지 검증
> - ```checkFinishState_When1and8_02```
    >   TC6. 1번참가자가 1을 모았고 3번참가자가 8을 모아서 종료조건이 되었고, 승리자가 1,3번 참가자로 나오는지 검증
> - ```checkFinishState_When7_0```
    >   TC7. 1번참가자가 7을 모아서 게임이 바로 끝나는 상황을 검증. 승리자는 1번참가자로 나와야함.
> <img width="398" alt="스크린샷 2023-07-11 오전 10 39 35" src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/cc01a0a5-2fbe-49d1-a811-c5798d765ec7">
---------
> ## 6. 결과화면 만들기
> 230713 11:30 AM
> - 참가자들이 한 턴씩 돌아가면서 카드를 3개씩 뽑고, 게임이 끝나면 보여줄 결과화면을 제작하였다.
> - LuckyGame 클래스에 5번 과정에서 구현한 메소드들을 적극적으로 사용하여 게임 전체 로직을 구현하였다.
> - 참가자들이 3개의 카드를 뽑고, 다음 턴으로 넘기는 과정을 구현하기 위해 mutableMapOf<Int, Int>()를 사용하였다.
> - 참가자 번호와 각 참가자당 뽑은 횟수를 Map 자료형으로 관리하고, 현재 턴 참가자도 Int형 변수로 따로 관리하여 게임로직을 운영하였다.
> - LuckyGame 클래스에 현재 참가자별 카드보유상황을 모두 기록하고 있는 participantList와, 현재 참가자별 결과를 모두 기록하고 있는 participantResultList를 따로 관리하였다.
> - 같은 카드를 3개 뽑는 순간 setResult 함수를 실행하여 participantList에서 해당 카드를 찾아 삭제하고, participantResultList 에서 카드를 뽑은 참가자에 해당하는 번호에 3개의 카드를 추가시킨다.
> - 한 턴이 돌때 마다 checkFinishState 함수를 실행하여 종료조건임을 판단하고, 종료조건이 되었을 경우 결과화면으로 넘긴다.
> - 결과화면으로 넘길 때 Intent로 여러가지 정보를 전달한다.
>   - ```isWinner```: ```Boolean```
>   - ```WinnerList```: ```ArrayList<Int>```
>   - ```WinnerDB```:```ArrayList<Participant>```
> - 이렇게 객체의 리스트를 전달하려고 Participant에 Serializable를 상속하였다.
> 
> - 아래는 결과화면. 여러가지 승자가 나올 수 있는 State를 테스트하였다. (빠른 테스트를 위하여 CardList의 Shuffle은 진행하지 않았다)
![1](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/c5f8f75c-fd24-4618-bd1a-183c1920564c) | ![3](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/9c744b47-1927-4658-b412-af434da40e55) | ![4](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/470b3b97-13ec-4c2b-8f13-0dbb4b71cd14)
--- | --- | --- |
![5](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/ab082235-19ed-4940-b703-08d6b947857d) | ![6](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/5c111b48-1432-40ab-b7b0-fc8878ae807a) | ![7](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/c0c9ac25-a614-4ea7-b38a-01c767ea72ff)
--- | --- | --- |