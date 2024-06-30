package org.example.com.rcs.bst

import kotlin.math.max

data class TreeNode<K, V>(
    var key: K,
    var value: V,
    var left: TreeNode<K, V>?,
    var right: TreeNode<K, V>?,
    var parent: TreeNode<K, V>?
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