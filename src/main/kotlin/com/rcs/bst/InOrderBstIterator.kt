package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class InOrderBstIterator<K, V>(start: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    companion object {

        fun <K, V> fromRoot(root: BstNode<K, V>?): InOrderBstIterator<K, V> {
            return InOrderBstIterator(leftMost(root))
        }

        private fun <K, V> leftMost(root: BstNode<K, V>?): BstNode<K, V>? {
            var leftMost = root
            while (leftMost?.left != null) {
                leftMost = leftMost.left!!
            }
            return leftMost
        }
    }

    private var next = start

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
}