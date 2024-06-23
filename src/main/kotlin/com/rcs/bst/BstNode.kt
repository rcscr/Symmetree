package org.example.com.rcs.bst

import kotlin.math.max

data class BstNode<K, V>(
    var key: K,
    var value: V,
    var left: BstNode<K, V>?,
    var right: BstNode<K, V>?,
    var parent: BstNode<K, V>?
) {

    fun isRoot(): Boolean {
        return parent == null
    }

    fun isLeaf(): Boolean {
        return left == null && right == null
    }

    fun height(): Int {
        return 1 + max(left?.height() ?: 0, right?.height() ?: 0)
    }
}