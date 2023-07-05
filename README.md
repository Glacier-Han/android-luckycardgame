# android-luckycardgame
Android 학습 프로젝트 #1

> > 1. 게임 보드 만들기

> 230704 03:15 PM
![Screenshot_20230704_153654](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/eb18d03f-3e75-4da9-933e-1ac069492cf1)
> - ConstraintLayout, LinearLayout을 활용하여 화면을 구성하였다.
> - 화면을 크게 3분류로 나누었다. (노란 뷰 / 카드 뷰 / 하단 뷰)
> - 뒤에 기획을 잠시 보니, 하단 뷰가 카드뷰 갯수에 따라 크기가 자유롭게 바뀌어서, ConstraintLayout을 활용하여 3개의 뷰를 묶어 배치하였다.
> - 카드뷰의 constraintBottom_toTopOf 속성과 하단뷰의 constraintTop_toBottomOf로 설정하여 카드뷰의 크기에 따라 하단뷰도 크기가 변경되게 하였다.

> > 2. 럭키카드 클래스 구현하기
> 230704 05:03 PM


> > 3. 카드 나눠주기
> 230705 06:30 PM
> - 각각 참가자 카드덱은 CardView 안에 RecyclerView를 넣어 구현하였다.
> - 앞면/뒷면을 구분하기 위해 CardAdapter 생성자에 isFront: Boolean을 추가하여 앞면뒷면 상태에 따라 View들의 Visibility를 제어하였다.
> - 카드가 많을 경우 겹쳐보이게 하기 위하여 OverlapDecoration 클래스를 구현 후 recyclerview.addItemDecoration을 사용하여 구현하였다.
> - 겹치는 정도가 조절되게 하고싶어 overlapParam을 생성자로 뺐지만, 리사이클러뷰에 적용하는 addItemDecoration 함수가 실행시 마다 중첩되는 이슈가 발생하였다.
> - recyclerView의 itemDecoration 상태를 초기화할 수 있는 함수는 지원되지 않는 것 같다. 조금 더 찾아보고 다른 방법을 생각해보아야겠다.