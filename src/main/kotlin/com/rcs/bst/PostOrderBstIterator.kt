package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class PostOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    private var next = leftMost(root)

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
                if (isLeftSubtree) {
                    var parentsRight = next!!.parent!!.right
                    if (parentsRight != null) {
                        var newNext = leftMost(parentsRight)
                        while (newNext == parentsRight && parentsRight!!.right != null) {
                            parentsRight = parentsRight.right
                            newNext = leftMost(parentsRight)
                        }
                        newNext
                    } else {
                        next!!.parent
                    }
                } else {
                    next!!.parent
                }
            }
        }
    }

    private fun leftMost(root: BstNode<K, V>?): BstNode<K, V>? {
        var leftMost = root
        while (leftMost?.left != null) {
            leftMost = leftMost.left!!
        }
        return leftMost
    }
}