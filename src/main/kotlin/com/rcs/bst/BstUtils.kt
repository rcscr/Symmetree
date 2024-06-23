package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class BstUtils {

    companion object {

        val LESS = -1
        val EQUAL = 0
        val GREATER = 1

        fun <K, V> leftMost(node: BstNode<K, V>?): BstNode<K, V>? {
            var leftMost = node
            while (leftMost?.left != null) {
                leftMost = leftMost.left!!
            }
            return leftMost
        }

        fun <K, V> rightMost(node: BstNode<K, V>?): BstNode<K, V>? {
            var rightMost = node
            while (rightMost?.right != null) {
                rightMost = rightMost.right!!
            }
            return rightMost
        }

        fun <K, V> predecessor(node: BstNode<K, V>): BstNode<K, V> {
            return node.left?.let {
                rightMost(it)
            } ?: node
        }

        fun <K, V> successor(node: BstNode<K, V>): BstNode<K, V> {
            return node.right?.let {
                leftMost(it)
            } ?: node
        }

        fun <K: Comparable<K>, V> findStart(startInclusive: K, node: BstNode<K, V>?): BstNode<K, V>? {
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
    }
}