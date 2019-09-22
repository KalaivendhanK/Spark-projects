package com.home.testing

import org.scalatest.{ FunSuite, Matchers }
import com.home.collections.SetG

class SetGTestSuite extends FunSuite with Matchers {

  test("test to check the apply functionality for creating Sets") {
    val a = SetG(1, 2, 3)

    a(1) shouldBe true
    a(2) shouldBe true
    a(3) shouldBe true
    a(4) shouldBe false
  }

  test("check the functionality of foreach") {
    val a = randomString
    val b = randomString
    val c = randomString

    var accum1: Int = 0
    var accum2: Int = 0

    val validSet = SetG(a, b, c).foreach { elem: String =>
      accum1 += elem.length
    }
    val twoElementSet = SetG(a, b, c).remove(b).foreach { elem: String =>
      accum2 += elem.length
    }

    accum1 shouldBe 15
    accum2 shouldBe 10

    val elementSet = SetG(a, b).add(c).add(b)
    elementSet(c) shouldBe true

    elementSet.size shouldBe 3
  }

  test("adding an element on an empty set should produce a NonEmpty set") {
    val first = randomString
    val second = randomString

    val setWithOneElement = SetG.empty.add(first)
    setWithOneElement(first) shouldBe true
    setWithOneElement(second) shouldBe false

    val setWithTwoElements = SetG.empty.add(first).add(second)
    setWithTwoElements(first) shouldBe true
    setWithTwoElements(second) shouldBe true
  }

  test("remove on an empty set should produce an Empty set") {
    val first = randomString
    val second = randomString
    val third = randomString
    val fourth = randomString

    SetG.empty.remove(first).size shouldBe 0

    val removedOneElement = SetG.empty.add(first).remove(first)
    removedOneElement(first) shouldBe false
    removedOneElement(second) shouldBe false

    val removedTwoElements = SetG.empty.add(first).add(second).remove(first)
    removedTwoElements(first) shouldBe false
    removedTwoElements(second) shouldBe true

    val setToCheckTheNullElements = SetG.empty.add(first).add(second).add(second).add(third).add(fourth)
    // setToCheckTheNullElements(null) shouldBe false
    setToCheckTheNullElements.size shouldBe 4

  }

  test("Check the functionality of singleton") {
    val first = randomString
    val second = randomString

    val someSet = SetG(first)
    someSet.isSingleton shouldBe true

    val emptySet = SetG.empty
    emptySet.isSingleton shouldBe false

    val validSet = SetG(first, second)
    validSet.isSingleton shouldBe false
    validSet.remove(first).isSingleton shouldBe true
    validSet.remove(first).remove(second).isSingleton shouldBe false

  }

  test("test the map functionality") {
    val a = "three"
    val b = "one"
    val c = "four"

    val validSet1 = SetG(a, b, c)

    val mappedSet1 = validSet1.map { (x: String) =>
      x.length
    }

    mappedSet1 shouldBe an[SetG.NonEmpty[Int]]

    var acc: Int = 0
    val mappedSet2 = validSet1.map { (x: String) =>
      x.length
    }.foreach(acc += _)

    acc shouldBe 12
  }

  test("test the functionality of flat map by making it to create a chessboard") {
    val x: SetG[Char] = SetG('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    val y: SetG[Int] = SetG(1, 2, 3, 4, 5, 6, 7, 8)

    val chessboard: SetG[(Char, Int)] = x.flatMap { x =>
      y.map { y =>
        (x -> y)
      }
    }
    // chessboard.foreach(println)
    chessboard.size shouldBe 64

    val sampleChessboardValue = 'b' -> 5
    chessboard(sampleChessboardValue) shouldBe true
    chessboard(('z' -> 3)) shouldBe false

  }

  test("test the union for the foreach implementation") {
    val a = randomString
    val b = randomString
    val c = randomString
    val d = randomString
    val e = randomString

    val validSet1 = SetG(a, b)
    val validSet2 = SetG(c, d)
    val unionizedSet = validSet1.union(validSet2)

    unionizedSet(a) shouldBe true
    unionizedSet(b) shouldBe true
    unionizedSet(c) shouldBe true
    unionizedSet(d) shouldBe true
    unionizedSet(e) shouldBe false

    val unionWithRemove = unionizedSet.remove(a).remove(b)

    unionWithRemove(a) shouldBe false
    unionWithRemove(b) shouldBe false

    val removedUnionWithAdd = unionWithRemove.add(a)

    removedUnionWithAdd(a) shouldBe true
    removedUnionWithAdd(b) shouldBe false

    val validSet3 = SetG('a', 'b')
    val mixedSet = validSet1.union(validSet3)

    mixedSet('a') shouldBe true
    mixedSet('b') shouldBe true
    mixedSet(a) shouldBe true
    mixedSet(b) shouldBe true

  }

  test("test the intersection for the foreach implementation") {
    val a = randomString
    val b = randomString
    val c = randomString
    val d = randomString
    val e = randomString

    val validSet1 = SetG(a, b, c)
    val validSet2 = SetG(b, c, d)

    val intersectionizedSet = validSet1.intersection(validSet2)

    intersectionizedSet(a) shouldBe false
    intersectionizedSet(b) shouldBe true
    intersectionizedSet(c) shouldBe true
    intersectionizedSet(d) shouldBe false
    intersectionizedSet(e) shouldBe false

    val intersectionWithRemove = intersectionizedSet.remove(a).remove(b)

    intersectionWithRemove(a) shouldBe false
    intersectionWithRemove(b) shouldBe false

    val removedintersectionWithAdd = intersectionWithRemove.add(a)

    removedintersectionWithAdd(a) shouldBe true
    removedintersectionWithAdd(b) shouldBe false
  }

  test("Check the functionality of filter") {
    val a = randomString
    val b = randomString
    val c = randomString

    val validSet1 = SetG(a, b, c)

    val filteredSet = validSet1.filter(_ == a)

    filteredSet(a) shouldBe true
    filteredSet(b) shouldBe false
    filteredSet(c) shouldBe false
  }

  test("check the functionality of equals") {
    val a = randomString
    val b = randomString

    val validSet1 = SetG(a, b)
    val otherSet = SetG('a', 'b')

    validSet1 should not be otherSet
    validSet1 shouldBe SetG(a, b)
  }

  test("Sets should be able to behave as functions") {
    val setToTest = SetG("Kalai", "vendhan", "IT Professional", "Cognizant")

      def realFunct(input: String): Boolean = input == "Kalai" || input == "vendhan"
    val functSet = SetG("Kalai", "vendhan")

    setToTest.filter(realFunct) shouldBe SetG("Kalai", "vendhan")
    setToTest.filter(realFunct) shouldBe setToTest.filter(functSet)

    setToTest.filter(functSet)("Kalai") shouldBe true
    setToTest.filter(functSet)("vendhan") shouldBe true
    setToTest.filter(functSet)("IT Professional") shouldBe false
    setToTest.filter(functSet)("Cognizant") shouldBe false

  }

  def randomString: String = scala.util.Random.alphanumeric.take(5).mkString
  def setMaker(elements: Int): (Seq[String], SetG[String]) = {
    val elems = (0 until elements).map(_ => randomString)
    elems -> SetG(elems)
  }
}