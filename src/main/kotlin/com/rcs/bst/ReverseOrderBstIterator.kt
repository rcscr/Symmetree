package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class ReverseOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    internal var next = BstUtils.rightMost(root)

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): BstEntry<K, V> {
        val toReturn = next!!
        setNext()
        return BstEntry(toReturn.key, toReturn.value)
    }

    private fun setNext() {
        if (next?.left != null) {
            next = BstUtils.rightMost(next?.left)
        } else {
            while (next?.parent != null && next == next?.parent?.left) {
                next = next?.parent
            }
            next = next?.parent
        }
    }
}