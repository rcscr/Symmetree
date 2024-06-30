package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class PreOrderBstIterator<K: Comparable<K>, V>(
    val bst: BalancedBinarySearchTree<K, V>
): Iterator<BstEntry<K, V>> {

    private val queue = ArrayDeque<BstNode<K, V>>()
    private val modCount = bst.modCount

    init {
        bst.root?.let { queue.add(it) }
    }

    override fun hasNext(): Boolean {
        return queue.isNotEmpty()
    }

    override fun next(): BstEntry<K, V> {
        if (modCount != bst.modCount) {
            throw ConcurrentModificationException()
        }

        val toReturn = queue.removeFirst()
        setNext(toReturn)
        return BstEntry(toReturn.key, toReturn.value)
    }

    private fun setNext(previousNext: BstNode<K, V>) {
        previousNext.left?.let { queue.add(it) }
        previousNext.right?.let { queue.add(it) }
    }
}