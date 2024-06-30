package com.rcs.bst

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TreeUtilsTest {

    @Test
    fun `test predecessor with no right subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val predecessor = TreeUtils.predecessor(bst.root!!.left!!)

        // Assert
        assertThat(predecessor!!.key).isEqualTo(4)
    }

    @Test
    fun `test successor with no left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val successor = TreeUtils.successor(bst.root!!.right!!)

        // Assert
        assertThat(successor!!.key).isEqualTo(16)
    }

    @Test
    fun `test predecessor with right subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val predecessor = TreeUtils.predecessor(bst.root!!)

        // Assert
        assertThat(predecessor!!.key).isEqualTo(7)
    }

    @Test
    fun `test successor with left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val successor = TreeUtils.successor(bst.root!!)

        // Assert
        assertThat(successor!!.key).isEqualTo(13)
    }

    @Test
    fun `test leftMost with left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.leftMost(bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(3)
    }

    @Test
    fun `test leftMost with no left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.leftMost(bst.root!!.left!!.right)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(7)
    }


    @Test
    fun `test rightMost with left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val rightMost = TreeUtils.rightMost(bst.root)

        // Assert
        assertThat(rightMost!!.key).isEqualTo(17)
    }

    @Test
    fun `test rightMost with no left subtree`() {
        // Arrange
        val bst = commonTree()

        // Act
        val rightMost = TreeUtils.leftMost(bst.root!!.right!!.left)

        // Assert
        assertThat(rightMost!!.key).isEqualTo(13)
    }

    @Test
    fun `test find start with exact match left of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.findStart(4, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(4)
    }

    @Test
    fun `test find start with exact match right of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.findStart(14, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(15)
    }

    @Test
    fun `test find start with inexact match left of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.findStart(14, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(15)
    }

    @Test
    fun `test find start with inexact match right of root`() {
        // Arrange
        val bst = commonTree()

        // Act
        val leftmost = TreeUtils.findStart(6, bst.root)

        // Assert
        assertThat(leftmost!!.key).isEqualTo(7)
    }

    @Test
    fun `test replace with leaf node`() {
        // Arrange
        val bst = commonTree()
        val node = bst.findRecursively(5, bst.root)!!
        val replacement = bst.findRecursively(3, bst.root)!!

        // Act
        TreeUtils.replace(node, replacement)

        // Assert
        assertThat(bst.root!!.left!!.key).isEqualTo(3)
        assertThat(bst.root!!.left!!.left!!.key).isEqualTo(4)
        assertThat(bst.root!!.left!!.right!!.key).isEqualTo(7)
        assertThat(bst.root!!.left!!.left!!.isLeaf()).isTrue()
    }

    @Test
    fun `test replace with non-leaf node`() {
        // Arrange
        val bst = commonTree()
        val node = bst.findRecursively(5, bst.root)!!
        val replacement = bst.findRecursively(15, bst.root)!!

        // Act
        TreeUtils.replace(node, replacement)

        // Assert
        assertThat(bst.root!!.left!!.key).isEqualTo(15)
        assertThat(bst.root!!.left!!.left!!.key).isEqualTo(4)
        assertThat(bst.root!!.left!!.right!!.key).isEqualTo(7)
        assertThat(bst.root!!.right!!.key).isEqualTo(13)
        assertThat(bst.root!!.right!!.left).isNull()
        assertThat(bst.root!!.right!!.right!!.key).isEqualTo(16)
    }

    private fun commonTree(): AvlTree<Int, Unit> {
        val bst = AvlTree<Int, Unit>()

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