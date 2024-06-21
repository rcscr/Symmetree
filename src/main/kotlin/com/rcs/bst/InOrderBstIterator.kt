package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class InOrderBstIterator<K, V>(start: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    companion object {

        fun <K, V> fromStart(start: BstNode<K, V>?): InOrderBstIterator<K, V> {
            val it = InOrderBstIterator<K, V>(null)
            it.next = start
            return it
        }
    }

    internal var next = start

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
            next = BstUtils.leftMost(next?.right)
        } else {
            while (next?.parent != null && next == next?.parent?.right) {
                next = next?.parent
            }
            next = next?.parent
        }
    }
}