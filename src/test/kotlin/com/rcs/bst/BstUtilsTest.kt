package com.rcs.bst

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BstUtilsTest {

    @Test
    fun `test leftMost with left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.leftMost(bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(3)
    }

    @Test
    fun `test leftMost with no left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.leftMost(bst.root!!.left!!.right)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(7)
    }

    @Test
    fun `test find start with exact match left of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.findStart(4, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(4)
    }

    @Test
    fun `test find start with exact match right of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.findStart(14, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(15)
    }

    @Test
    fun `test find start with inexact match left of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.findStart(14, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(15)
    }

    @Test
    fun `test find start with inexact match right of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = BstUtils.findStart(6, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(7)
    }

    private fun commonTree(): BalancedBinarySearchTree<Int, Unit> {
        val bst = BalancedBinarySearchTree<Int, Unit>()

        //        10
        //       /  \
        //      5    15
        //     / \   / \
        //    4   7 13  16
        //   /           \
        //  3             17
        bst.add(10, Unit)
        bst.add(5, Unit)
        bst.add(15, Unit)
        bst.add(7, Unit)
        bst.add(13, Unit)
        bst.add(4, Unit)
        bst.add(16, Unit)
        bst.add(3, Unit)
        bst.add(17, Unit)

        return bst
    }
}