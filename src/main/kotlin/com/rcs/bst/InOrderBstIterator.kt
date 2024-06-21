package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class InOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

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
        if (next?.right != null) {
            next = leftMost(next?.right)
        } else {
            while (next?.parent != null && next == next?.parent?.right) {
                next = next?.parent
            }
            next = next?.parent
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