package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class PostOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    private var next = BstUtils.leftMost(root)

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): BstEntry<K, V> {
        val toReturn = next!!
        setNext()
        return BstEntry(toReturn.key, toReturn.value)
    }

    private fun setNext() {
        next = when {
            next!!.isRoot() -> null
            else -> {
                val isLeftSubtree = next!!.parent!!.left == next
                var parentsRight = next!!.parent!!.right
                if (isLeftSubtree && parentsRight != null) {
                    var newNext = BstUtils.leftMost(parentsRight)
                    while (newNext == parentsRight && parentsRight!!.right != null) {
                        parentsRight = parentsRight.right
                        newNext = BstUtils.leftMost(parentsRight)
                    }
                    newNext
                } else {
                    next!!.parent
                }
            }
        }
    }
}