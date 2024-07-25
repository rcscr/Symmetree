package com.rcs.bst

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.Executors
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log2
import kotlin.random.Random

class AvlTreeTest {

    @Test
    fun `test getMin`() {
        // Arrange
        val bst = commonTree()

        // Act
        val min = bst.getMin()

        // Assert
        assertThat(min).isEqualTo(TreeEntry(3, Unit))
        assertThat(bst.contains(3)).isTrue()
    }

    @Test
    fun `test popMin`() {
        // Arrange
        val bst = commonTree()

        // Act
        val min = bst.popMin()

        // Assert
        assertThat(min).isEqualTo(TreeEntry(3, Unit))
        assertThat(bst.contains(3)).isFalse()
    }

    @Test
    fun `test getMax`() {
        // Arrange
        val bst = commonTree()

        // Act
        val max = bst.getMax()

        // Assert
        assertThat(max).isEqualTo(TreeEntry(17, Unit))
        assertThat(bst.contains(17)).isTrue()
    }

    @Test
    fun `test popMax`() {
        // Arrange
        val bst = commonTree()

        // Act
        val max = bst.popMax()

        // Assert
        assertThat(max).isEqualTo(TreeEntry(17, Unit))
        assertThat(bst.contains(17)).isFalse()
    }

    @Test
    fun `test simple update`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")
        bst.add(-2, "minus two")

        // Act
        val previousValue = bst.add(-2, "minus deux")

        // Assert
        assertThat(previousValue).isEqualTo("minus two")
        val currentValue = bst.get(-2)
        assertThat(currentValue).isEqualTo("minus deux")
    }

    @Test
    fun `test remove non-existent`() {
        // Arrange
        val bst = commonTree()

        // Act
        val removed = bst.remove(-1)

        // Assert
        assertThat(removed).isNull()
        assertThat(bst.height).isEqualTo(4)
    }

    @Test
    fun `test remove root with children`() {
        // Arrange
        val bst = commonTree()

        // Act
        bst.remove(10)

        // Assert
        assertThat(bst.height).isEqualTo(4)
        assertThat(bst.contains(10)).isFalse()
        assertThat(bst.get(10)).isNull()
        assertThat(bst.root!!.key).isEqualTo(7)
        assertThat(bst.root!!.left!!.key).isEqualTo(4)
        assertThat(bst.root!!.left!!.left!!.key).isEqualTo(3)
        assertThat(bst.root!!.left!!.right!!.key).isEqualTo(5)
        assertThat(bst.root!!.right!!.key).isEqualTo(15)
    }

    @Test
    fun `test remove root without children`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")

        // Act
        bst.remove(0)

        // Assert
        assertThat(bst.height).isEqualTo(0)
        assertThat(bst.contains(0)).isFalse()
        assertThat(bst.get(0)).isNull()
        assertThat(bst.root).isNull()
    }

    @Test
    fun `test remove left leaf`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")

        // Act
        bst.remove(-1)

        // Assert
        assertThat(bst.height).isEqualTo(2)
        assertThat(bst.contains(-1)).isFalse()
        assertThat(bst.get(-1)).isNull()
        assertThat(bst.root!!.key).isEqualTo(0)
        assertThat(bst.root!!.left).isNull()
        assertThat(bst.root!!.right!!.key).isEqualTo(1)
    }

    @Test
    fun `test remove right leaf`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")

        // Act
        bst.remove(1)

        // Assert
        assertThat(bst.height).isEqualTo(2)
        assertThat(bst.contains(1)).isFalse()
        assertThat(bst.get(1)).isNull()
        assertThat(bst.root!!.key).isEqualTo(0)
        assertThat(bst.root!!.right).isNull()
        assertThat(bst.root!!.left!!.key).isEqualTo(-1)
    }

    @Test
    fun `test remove leaf with left node`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")
        bst.add(-2, "minus two")

        // Act
        bst.remove(-1)

        // Assert
        assertThat(bst.height).isEqualTo(2)
        assertThat(bst.contains(-1)).isFalse()
        assertThat(bst.get(-1)).isNull()
        assertThat(bst.root!!.key).isEqualTo(0)
        assertThat(bst.root!!.left!!.key).isEqualTo(-2)
        assertThat(bst.root!!.right!!.key).isEqualTo(1)
    }

    @Test
    fun `test remove leaf with right node`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")
        bst.add(2, "two")

        // Act
        bst.remove(1)

        // Assert
        assertThat(bst.height).isEqualTo(2)
        assertThat(bst.contains(1)).isFalse()
        assertThat(bst.get(1)).isNull()
        assertThat(bst.root!!.key).isEqualTo(0)
        assertThat(bst.root!!.left!!.key).isEqualTo(-1)
        assertThat(bst.root!!.right!!.key).isEqualTo(2)
    }

    @Test
    fun `test get recursive`() {
        // Arrange
        val bst = AvlTree<Int, String>()
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(1, "one")

        // Act
        val value = bst.get(-1)

        // Assert
        assertThat(value).isEqualTo("minus one")
    }

    @Test
    fun `test add left-skewed balances`() {
        // Arrange
        val bst = AvlTree<Int, String>()

        // Act
        bst.add(0, "zero")
        bst.add(-1, "minus one")
        bst.add(-2, "minus two")

        // Assert
        assertThat(bst.root!!.key).isEqualTo(-1)
        assertThat(bst.root!!.value).isEqualTo("minus one")
        assertThat(bst.root!!.parent).isNull()

        assertThat(bst.root!!.left!!.key).isEqualTo(-2)
        assertThat(bst.root!!.left!!.value).isEqualTo("minus two")
        assertThat(bst.root!!.left!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.left!!.left).isNull()
        assertThat(bst.root!!.left!!.right).isNull()

        assertThat(bst.root!!.right!!.key).isEqualTo(0)
        assertThat(bst.root!!.right!!.value).isEqualTo("zero")
        assertThat(bst.root!!.right!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.right!!.left).isNull()
        assertThat(bst.root!!.right!!.right).isNull()
    }

    @Test
    fun `test add right-skewed balances`() {
        // Arrange
        val bst = AvlTree<Int, String>()

        // Act
        bst.add(0, "zero")
        bst.add(1, "one")
        bst.add(2, "two")

        // Assert
        assertThat(bst.root!!.key).isEqualTo(1)
        assertThat(bst.root!!.value).isEqualTo("one")
        assertThat(bst.root!!.parent).isNull()

        assertThat(bst.root!!.left!!.key).isEqualTo(0)
        assertThat(bst.root!!.left!!.value).isEqualTo("zero")
        assertThat(bst.root!!.left!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.left!!.left).isNull()
        assertThat(bst.root!!.left!!.right).isNull()

        assertThat(bst.root!!.right!!.key).isEqualTo(2)
        assertThat(bst.root!!.right!!.value).isEqualTo("two")
        assertThat(bst.root!!.right!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.right!!.left).isNull()
        assertThat(bst.root!!.right!!.right).isNull()
    }

    @Test
    fun `test left-right rotation`() {
        // Arrange
        val bst = AvlTree<Int, String>()

        // Act
        bst.add(30, "thirty")
        bst.add(20, "twenty")
        bst.add(25, "twenty five")

        // Assert
        assertThat(bst.root!!.key).isEqualTo(25)
        assertThat(bst.root!!.value).isEqualTo("twenty five")
        assertThat(bst.root!!.parent).isNull()

        assertThat(bst.root!!.left!!.key).isEqualTo(20)
        assertThat(bst.root!!.left!!.value).isEqualTo("twenty")
        assertThat(bst.root!!.left!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.left!!.left).isNull()
        assertThat(bst.root!!.left!!.right).isNull()

        assertThat(bst.root!!.right!!.key).isEqualTo(30)
        assertThat(bst.root!!.right!!.value).isEqualTo("thirty")
        assertThat(bst.root!!.right!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.right!!.left).isNull()
        assertThat(bst.root!!.right!!.right).isNull()
    }

    @Test
    fun `test right-left rotation`() {
        // Arrange
        val bst = AvlTree<Int, String>()

        // Act
        bst.add(30, "thirty")
        bst.add(40, "forty")
        bst.add(35, "thirty five")

        // Assert
        assertThat(bst.root!!.key).isEqualTo(35)
        assertThat(bst.root!!.value).isEqualTo("thirty five")
        assertThat(bst.root!!.parent).isNull()

        assertThat(bst.root!!.left!!.key).isEqualTo(30)
        assertThat(bst.root!!.left!!.value).isEqualTo("thirty")
        assertThat(bst.root!!.left!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.left!!.left).isNull()
        assertThat(bst.root!!.left!!.right).isNull()

        assertThat(bst.root!!.right!!.key).isEqualTo(40)
        assertThat(bst.root!!.right!!.value).isEqualTo("forty")
        assertThat(bst.root!!.right!!.parent).isEqualTo(bst.root)
        assertThat(bst.root!!.right!!.left).isNull()
        assertThat(bst.root!!.right!!.right).isNull()
    }

    @Test
    fun `test default (InOrder) iterator`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act
        val iterated = mutableListOf<TreeEntry<Int, Unit>>()
        for (entry in bst) {
            iterated.add(entry)
        }

        // Assert
        assertThat(iterated.map { it.key }).isEqualTo(values.sorted().distinct())
    }

    @Test
    fun `test ReverseOrder iterator`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act
        val iterated = mutableListOf<TreeEntry<Int, Unit>>()
        for (entry in bst.reverseOrderIterator()) {
            iterated.add(entry)
        }

        // Assert
        assertThat(iterated.map { it.key }).isEqualTo(values.sorted().reversed().distinct())
    }

    @Test
    fun `test PreOrder iterator`() {
        // Arrange
        val bst = commonTree()

        // Act
        val iterated = mutableListOf<TreeEntry<Int, Unit>>()
        for (entry in bst.preOrderIterator()) {
            iterated.add(entry)
        }

        // Assert
        assertThat(iterated.map { it.key }).containsExactly(10, 5, 15, 4, 7, 14, 16, 3, 17)
    }

    @Test
    fun `test PostOrder iterator`() {
        // Arrange
        val bst = commonTree()

        // Act
        val iterated = mutableListOf<TreeEntry<Int, Unit>>()
        for (entry in bst.postOrderIterator()) {
            iterated.add(entry)
        }

        // Assert
        assertThat(iterated.map { it.key }).containsExactly(3, 4, 7, 5, 14, 17, 16, 15, 10)
    }

    @Test
    fun `test default (InOrder) iterator - ConcurrentModificationException`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act & Assert
        assertThrows<ConcurrentModificationException> {
            for (entry in bst) {
                bst.remove(entry.key)
            }
        }
    }

    @Test
    fun `test ReverseOrder iterator - ConcurrentModificationException`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act & Assert
        assertThrows<ConcurrentModificationException> {
            for (entry in bst.reverseOrderIterator()) {
                bst.remove(entry.key)
            }
        }
    }

    @Test
    fun `test PostOrder iterator - ConcurrentModificationException`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act & Assert
        assertThrows<ConcurrentModificationException> {
            for (entry in bst.postOrderIterator()) {
                bst.remove(entry.key)
            }
        }
    }

    @Test
    fun `test PreOrder iterator - ConcurrentModificationException`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<1_000).map { Random.nextInt() }
        values.forEach { bst.add(it, Unit) }

        // Act & Assert
        assertThrows<ConcurrentModificationException> {
            for (entry in bst.postOrderIterator()) {
                bst.remove(entry.key)
            }
        }
    }

    @Test
    fun `test range query`() {
        // Arrange
        val bst = commonTree()

        // Act
        val result = bst.rangeQuery(6, 16)

        // Assert
        assertThat(result.map { it.key }).containsExactly(7, 10, 14, 15)
    }

    @Test
    fun `stress test balancing maintains correct height`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<10_000).map { Random.nextInt() }

        values.forEach {
            bst.add(it, Unit)
        }

        // Act
        val height = bst.height

        // Assert
        assertThat(height).isBetween(
            minHeightOfBalancedBinaryTree(values.size),
            maxHeightOfBalancedBinaryTree(values.size))
    }

    @Test
    fun `test concurrent adds and removals`() {
        // Arrange
        val bst = AvlTree<Int, Unit>()

        val values = (0..<10_000).map { Random.nextInt(Integer.MIN_VALUE, -1) }

        val executorService = Executors.newFixedThreadPool(8)

        val futures = values.map { executorService.submit { bst.add(it, Unit) } }
        futures.forEach { it.get() }

        // Act
        val height = bst.height

        // Assert
        assertThat(height).isBetween(
            minHeightOfBalancedBinaryTree(values.size),
            maxHeightOfBalancedBinaryTree(values.size))

        // Act (again) - add new and different values while concurrently removing old ones
        val newValues = (0..<10_000).map { Random.nextInt(1, Integer.MAX_VALUE) }

        val addFutures = newValues.map { executorService.submit { bst.add(it, Unit) } }
        val removeFutures = values.map { executorService.submit { bst.remove(it) } }
        addFutures.forEach { it.get() }
        removeFutures.forEach { it.get() }

        val heightAgain = bst.height

        val remaining = mutableListOf<TreeEntry<Int, Unit>>()
        for (entry in bst) {
            remaining.add(entry)
        }

        // Assert (again)
        assertThat(heightAgain).isBetween(
            minHeightOfBalancedBinaryTree(newValues.size),
            maxHeightOfBalancedBinaryTree(newValues.size))

        assertThat(isInAscendingOrder(remaining)).isTrue()
        assertThat(remaining.map { it.key }).isEqualTo(newValues.sorted().distinct())
    }

    private fun minHeightOfBalancedBinaryTree(n: Int): Int {
        return ceil(log2(n + 1.0)).toInt()
    }

    private fun maxHeightOfBalancedBinaryTree(n: Int): Int {
        return floor(1.44 * log2(n + 2.0)-0.328).toInt()
    }

    private fun isInAscendingOrder(iterated: List<TreeEntry<Int, Unit>>): Boolean {
        for (index in 0..<iterated.size-1) {
            if (iterated[index].key > iterated[index + 1].key) {
                return false
            }
        }
        return true
    }

    private fun commonTree(): AvlTree<Int, Unit> {
        val bst = AvlTree<Int, Unit>()

        //        10
        //       /  \
        //      5    15
        //     / \   / \
        //    4   7 14  16
        //   /           \
        //  3             17
        bst.add(10, Unit)
        bst.add(5, Unit)
        bst.add(15, Unit)
        bst.add(7, Unit)
        bst.add(14, Unit)
        bst.add(4, Unit)
        bst.add(16, Unit)
        bst.add(3, Unit)
        bst.add(17, Unit)

        return bst
    }
}