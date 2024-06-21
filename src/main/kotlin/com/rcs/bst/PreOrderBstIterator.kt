package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class PreOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    private val queue = ArrayDeque<BstNode<K, V>>()

    init {
        root?.let { queue.add(it) }
    }

    override fun hasNext(): Boolean {
        return queue.isNotEmpty()
    }

    override fun next(): BstEntry<K, V> {
        val toReturn = queue.removeFirst()
        toReturn.left?.let { queue.add(it) }
        toReturn.right?.let { queue.add(it) }
        return BstEntry(toReturn.key, toReturn.value)
    }
}