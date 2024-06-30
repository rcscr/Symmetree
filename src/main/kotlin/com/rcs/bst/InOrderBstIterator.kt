package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class InOrderBstIterator<K: Comparable<K>, V>(
    val bst: BalancedBinarySearchTree<K, V>
): Iterator<BstEntry<K, V>> {

    companion object {
        fun <K: Comparable<K>, V> fromStart(bst: BalancedBinarySearchTree<K, V>, start: BstNode<K, V>?): InOrderBstIterator<K, V> {
            val it = InOrderBstIterator(bst)
            it.next = start
            return it
        }
    }

    private var next = BstUtils.leftMost(bst.root)
    private val modCount = bst.modCount

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): BstEntry<K, V> {
        if (modCount != bst.modCount) {
            throw ConcurrentModificationException()
        }

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