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
> - ```testInitCardCount```
    >   -  TC1. 카드 생성후 갯수가 36개가 맞는지?
> - ```testInitCardTypeVaild```
    >   -  TC2. 카드 36개중에 같은 동물이 12개씩 있는지? (동물이 3종류임으로 한동물당 12개씩)
> - ```testShareCardCountWhen3```
    >   -  TC3. 참가자 인원에 맞게 생성되는지?
> - ```testShareCard3PeopleVaild```
    >   -  TC4. 3명이 참가했을 시 participant가 12번 카드를 가지고 있으면 안됨. 가지고 있는지 검사
> - ```testShareCardCountWhen5```
    >   -  TC5. 5명이 참가했을 시 participantList.size가 6이 맞는지?
> - ```testSortCardAscend```
    >   -  TC6. A참가자를 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
> - ```testSortRemainAscend```
    >   -  TC7. 남은 카드 대상으로 오름차순 정렬을 했을 때 제대로 수행되었는지 검사
> - ```testIsSameCardInParticipants```
    >   -  TC8. 참가자 중 같은 숫자 카드 3개를 가진 경우가 있는지 판단
> - ```testIsSameCardInSpecificCase```
    >   -  TC9. 1번 참가자와 3번 참가자 카드 중에 가장 낮은숫자 기준으로 3개가 같은지 판단
> <img width="396" alt="image" src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/c929cf29-5749-492c-9f30-895f78444b2f">

