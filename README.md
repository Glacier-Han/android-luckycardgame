# android-luckycardgame
Android 학습 프로젝트 #1

> > 1. 게임 보드 만들기

> 230704 03:15 PM
![Screenshot_20230704_153654](https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/61905052/eb18d03f-3e75-4da9-933e-1ac069492cf1)
> - ConstraintLayout, LinearLayout을 활용하여 화면을 구성하였다.
> - 화면을 크게 3분류로 나누었다. (노란 뷰 / 카드 뷰 / 하단 뷰)
> - 뒤에 기획을 잠시 보니, 하단 뷰가 카드뷰 갯수에 따라 크기가 자유롭게 바뀌어서, ConstraintLayout을 활용하여 3개의 뷰를 묶어 배치하였다.
> - 카드뷰의 constraintBottom_toTopOf 속성과 하단뷰의 constraintTop_toBottomOf로 설정하여 카드뷰의 크기에 따라 하단뷰도 크기가 변경되게 하였다.

