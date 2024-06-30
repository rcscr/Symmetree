package com.rcs.bst

import org.example.com.rcs.bst.TreeNode

class PreOrderTreeIterator<K: Comparable<K>, V>(
    val bst: AvlTree<K, V>
): Iterator<TreeEntry<K, V>> {

    private val queue = ArrayDeque<TreeNode<K, V>>()
    private val modCount = bst.modCount

    init {
        bst.root?.let { queue.add(it) }
    }

    override fun hasNext(): Boolean {
        return queue.isNotEmpty()
    }

    override fun next(): TreeEntry<K, V> {
        if (modCount != bst.modCount) {
            throw ConcurrentModificationException()
        }

        val toReturn = queue.removeFirst()
        setNext(toReturn)
        return TreeEntry(toReturn.key, toReturn.value)
    }

    private fun setNext(previousNext: TreeNode<K, V>) {
        previousNext.left?.let { queue.add(it) }
        previousNext.right?.let { queue.add(it) }
    }
}