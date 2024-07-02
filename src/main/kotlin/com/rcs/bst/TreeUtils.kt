package com.rcs.bst

import org.example.com.rcs.bst.TreeNode

class TreeUtils {

    companion object {

        const val LESS = -1
        const val EQUAL = 0
        const val GREATER = 1

        fun <K, V> leftMost(node: TreeNode<K, V>?): TreeNode<K, V>? {
            var leftMost = node
            while (leftMost?.left != null) {
                leftMost = leftMost.left!!
            }
            return leftMost
        }

        fun <K, V> rightMost(node: TreeNode<K, V>?): TreeNode<K, V>? {
            var rightMost = node
            while (rightMost?.right != null) {
                rightMost = rightMost.right!!
            }
            return rightMost
        }

        fun <K, V> predecessor(node: TreeNode<K, V>): TreeNode<K, V>? {
            return rightMost(node.left)
        }

        fun <K, V> successor(node: TreeNode<K, V>): TreeNode<K, V>? {
            return leftMost(node.right)
        }

        fun <K: Comparable<K>, V> findStart(startInclusive: K, node: TreeNode<K, V>?): TreeNode<K, V>? {
            return node?.let {
                when (startInclusive.compareTo(it.key)) {
                    LESS -> {
                        if (it.left == null || (it.left!!.key < startInclusive && it.left!!.right == null)) {
                            it
                        } else {
                            findStart(startInclusive, it.left)
                        }
                    }
                    EQUAL -> it
                    GREATER -> {
                        if (it.right == null || (it.right!!.key < startInclusive && it.right!!.left == null)) {
                            it
                        } else {
                            findStart(startInclusive, it.right)
                        }
                    }
                    else -> throw AssertionError()
                }
            }
        }

        fun <K, V> replace(node: TreeNode<K, V>, replacement: TreeNode<K, V>) {
            node.key = replacement.key
            node.value = replacement.value
            if (replacement.isLeaf()) {
                when (replacement) {
                    replacement.parent?.left -> replacement.parent!!.left = null
                    replacement.parent?.right -> replacement.parent!!.right = null
                }
            } else {
                replace(replacement, (predecessor(replacement) ?: successor(replacement))!!)
            }
        }

        fun <K, V> rotateRight(node: TreeNode<K, V>): TreeNode<K, V> {
            // new root
            val leftChild = node.left!!

            node.left = leftChild.right
            if (leftChild.right != null) {
                leftChild.right!!.parent = node
            }

            leftChild.right = node
            leftChild.parent = node.parent

            if (node.parent != null) {
                if (node.parent!!.left == node) {
                    node.parent!!.left = leftChild
                } else {
                    node.parent!!.right = leftChild
                }
            }

            node.parent = leftChild

            return leftChild
        }

        fun <K, V> rotateLeft(node: TreeNode<K, V>): TreeNode<K, V> {
            // new root
            val rightChild = node.right!!

            node.right = rightChild.left
            if (rightChild.left != null) {
                rightChild.left!!.parent = node
            }

            rightChild.left = node
            rightChild.parent = node.parent

            if (node.parent != null) {
                if (node.parent!!.left == node) {
                    node.parent!!.left = rightChild
                } else {
                    node.parent!!.right = rightChild
                }
            }

            node.parent = rightChild

            return rightChild
        }
    }
}