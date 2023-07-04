package com.glacier.luckycardgamesofteer

// 카드 데이터 클래스. AnimalType은 enum으로 유니코드와 함께 관리, num은 카드 숫자
// enum으로 동물 이름과 유니코드를 함께 관리하면 카드 생성시에 훨씬 직관적일 것 같아서 이렇게 구현!
data class Card(val animalType: AnimalInfo, val num: Int)
