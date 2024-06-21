package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class BstUtils {

    companion object {

        fun <K, V> leftMost(node: BstNode<K, V>?): BstNode<K, V>? {
            var leftMost = node
            while (leftMost?.left != null) {
                leftMost = leftMost.left!!
            }
            return leftMost
        }
    }
}